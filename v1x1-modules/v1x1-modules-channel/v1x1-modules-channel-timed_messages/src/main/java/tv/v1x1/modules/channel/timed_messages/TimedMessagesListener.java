package tv.v1x1.modules.channel.timed_messages;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.modules.channel.timed_messages.config.TimedMessagesUserConfiguration;

import java.lang.invoke.MethodHandles;
import java.util.Map;

/**
 * @author Josh
 */
public class TimedMessagesListener implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TimedMessages module;

    TimedMessagesListener(final TimedMessages module) {
        this.module = module;
    }

    @EventHandler
    public void onScheduledNotify(SchedulerNotifyEvent ev) {
        if(!ev.getModule().equals(module.toDto()))
            return;
        // payload comes from module.unpauseTimer();
        final UUID uuid = ev.getId();
        final byte[][] keys = CompositeKey.getKeys(ev.getPayload());
        final String timerName = new String(keys[0]);
        final String type = new String(keys[1]);
        // TODO: Deal with timers that come in and the module is channel disabled
        if(type.equals("tenant")) {
            final Tenant tenant;
            try {
                tenant = Tenant.fromProto(ChannelOuterClass.Tenant.parseFrom(keys[2]));
            } catch(final InvalidProtocolBufferException e) {
                LOG.warn("Failed to deserialize tenant from scheduler payload; cancelling schedule", e);
                module.pauseTimer(uuid);
                return;
            }
            MDC.put("tenant", tenant.getId().toString());
            LOG.trace("incoming UUID is {}", uuid.getValue());
            if(!module.getConfiguration(tenant).isEnabled()) {
                LOG.debug("Module is disabled, pausing timer...");
                module.pauseTimer(uuid);
                MDC.remove("tenant");
                return;
            }
            final Timer t = module.getConfiguration(tenant).getTimer(timerName);
            if(t == null) {
                LOG.warn("Got a timer payload for a non-existent timer id {}; pausing it...", uuid.getValue().toString());
                module.pauseTimer(uuid);
                MDC.remove("tenant");
                return;
            }
            LOG.trace("saved UUID is {}", t.getActiveTimer());
            if(t.getActiveTimer() == null) {
                LOG.warn("Got a timer with a null active timer, but is enabled.  Re-scheduling.");
                module.pauseTimer(uuid);
                module.unpauseTimer(tenant, timerName);
                MDC.remove("tenant");
                return;
            }
            if(!t.getActiveTimer().equals(uuid.getValue())) {
                LOG.info("Got a timer {} which is no longer the active timer UUID {}; pausing it.", uuid.getValue().toString(), t.getActiveTimer().toString());
                module.pauseTimer(uuid);
                MDC.remove("tenant");
                return;
            }
            int cursor = module.getCursor(uuid);
            LOG.trace("Just got cursor. Cursor is {}", cursor);
            if(cursor == -1) {
                LOG.warn("Got a timer with no cursor; resetting it...");
                cursor = 0;
            }
            if(cursor >= t.getEntries().size())
                cursor = 0;
            final TimerEntry nextEntry = t.getEntry(cursor++);
            if(nextEntry == null) {
                LOG.debug("Running, and therefore skipping, an empty timer");
                MDC.remove("tenant");
                return;
            }
            final String message = nextEntry.getMessage();
            module.saveCursor(uuid, cursor);
            for(final Channel channel : tenant.getChannels()) {
                if(channel.getChannelGroup().getPlatform().equals(Platform.TWITCH)) {
                    if(!(t.isAlwaysOn() || module.isStreaming(channel))) {
                        LOG.trace("Skipping non-streaming channel {}", channel.getDisplayName());
                        continue;
                    }
                }
                Chat.message(module, channel, message);
            }
        } else {
            LOG.error("Got unknown timer type {}", type);
        }
    }

    @EventHandler
    public void onChatMessage(ChatMessageEvent ev) {
        if(module.isEnabled(ev.getChatMessage().getChannel()))
            module.delegator.handleChatMessage(ev);
    }

    @EventHandler
    public void onTenantConfigChange(TenantConfigChangeEvent ev) {
        final TimedMessagesUserConfiguration config = module.getConfiguration(ev.getTenant());
        if(config.getTimers() != null)
            for(final Map.Entry<String, Timer> entry : config.getTimers().entrySet())
                if(config.isEnabled() && entry.getValue().isEnabled()) {
                    module.unpauseTimer(ev.getTenant(), entry.getKey());
                } else {
                    if (entry.getValue().getActiveTimer() != null)
                        module.pauseTimer(new UUID(entry.getValue().getActiveTimer()));
                }
    }
}
