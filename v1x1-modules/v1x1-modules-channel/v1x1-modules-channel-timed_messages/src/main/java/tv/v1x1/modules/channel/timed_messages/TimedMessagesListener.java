package tv.v1x1.modules.channel.timed_messages;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
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
        // TODO: Deal with timers that come in and the module is tenant/channel disabled
        if(type.equals("tenant")) {
            final Tenant tenant;
            try {
                tenant = Tenant.fromProto(ChannelOuterClass.Tenant.parseFrom(keys[2]));
            } catch(InvalidProtocolBufferException e) {
                LOG.warn("Failed to deserialize tenant from scheduler payload; cancelling schedule", e);
                module.disableTimer(uuid);
                return;
            }
            LOG.trace("incoming UUID is {}", uuid.getValue());
            Timer t = module.getTenantConfiguration(tenant).getTimer(timerName);
            if(t == null) {
                LOG.warn("Got a timer payload for a non-existent timer; disabling it...");
                module.disableTimer(uuid);
            }
            LOG.trace("saved UUID is {}", t.getActiveTimer());
            int cursor = module.getCursor(uuid);
            LOG.trace("Just got cursor. Cursor is {}", cursor);
            if(cursor == -1) {
                LOG.warn("Got a timer with no cursor; disabling it...");
                module.disableTimer(uuid);
                return;
            }
            if(cursor >= t.getEntries().size())
                cursor = 0;
            final TimerEntry nextEntry = t.getEntry(cursor++);
            if(nextEntry == null) {
                LOG.warn("Running, and therefore skipping, an empty timer");
                return;
            }
            final String message = nextEntry.getMessage();
            module.saveCursor(uuid, cursor);
            for(Channel channel : tenant.getChannels()) {
                Chat.message(module, channel, message);
            }
        } else {
            LOG.warn("Got unknown timer type {}", type);
        }
    }

    @EventHandler
    public void onChatMessage(ChatMessageEvent ev) {
        if(module.isEnabled(ev.getChatMessage().getChannel()))
            module.delegator.handleChatMessage(ev);
    }
}
