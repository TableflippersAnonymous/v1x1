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
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.data.CompositeKey;

import java.lang.invoke.MethodHandles;

/**
 * @author Josh
 */
public class TimedMessagesListener implements EventListener {
    public static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private TimedMessages module;

    TimedMessagesListener(TimedMessages module) {
        this.module = module;
    }

    @EventHandler
    public void onScheduledNotify(SchedulerNotifyEvent ev) {
        if(!ev.getModule().equals(module.toDto()))
            return;
        // payload comes from module.enableTimer();
        final UUID uuid = ev.getId();
        final byte[][] keys = CompositeKey.getKeys(ev.getPayload());
        final String timerName = new String(keys[0]);
        final String type = new String(keys[1]);
        // TODO: Deal with timers that come in and the module is channel disabled
        if(type.equals("tenant")) {
            final Tenant tenant;
            try {
                tenant = Tenant.fromProto(ChannelOuterClass.Tenant.parseFrom(keys[2]));
            } catch(InvalidProtocolBufferException e) {
                LOG.warn("Failed to deserialize tenant from scheduler payload; cancelling schedule", e);
                module.disableTimer(uuid);
                return;
            }
            MDC.put("tenant", tenant.getId().toString());
            LOG.trace("incoming UUID is {}", uuid.getValue());
            if(!module.getTenantConfiguration(tenant).isEnabled()) {
                LOG.debug("Module is disabled, disabling timer...");
                module.disableTimer(uuid);
                MDC.remove("tenant");
                return;
            }
            Timer t = module.getTenantConfiguration(tenant).getTimer(timerName);
            if(t == null) {
                LOG.warn("Got a timer payload for a non-existent timer id {}; disabling it...", uuid.getValue().toString());
                module.disableTimer(uuid);
                MDC.remove("tenant");
                return;
            }
            LOG.trace("saved UUID is {}", t.getActiveTimer());
            int cursor = module.getCursor(uuid);
            LOG.trace("Just got cursor. Cursor is {}", cursor);
            if(cursor == -1) {
                LOG.warn("Got a timer with no cursor; disabling it...");
                module.disableTimer(uuid);
                MDC.remove("tenant");
                return;
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
            for(Channel channel : tenant.getChannels()) {
                if(channel.getPlatform().equals(Platform.TWITCH)) {
                    if(t.isAlwaysOn() && !module.isStreaming(channel)) {
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
}
