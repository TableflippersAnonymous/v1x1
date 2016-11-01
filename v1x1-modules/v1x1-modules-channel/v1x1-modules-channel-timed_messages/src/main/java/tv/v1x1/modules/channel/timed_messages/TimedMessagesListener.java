package tv.v1x1.modules.channel.timed_messages;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.data.CompositeKey;

/**
 * @author Josh
 */
public class TimedMessagesListener implements EventListener {
    private TimedMessages module;

    TimedMessagesListener(TimedMessages module) {
        this.module = module;
    }

    @EventHandler
    public void onScheduledNotify(SchedulerNotifyEvent ev) {
        if(!ev.getModule().equals(module.toDto()))
            return;
        // payload comes from module.enableTimer();
        final byte[][] keys = CompositeKey.getKeys(ev.getPayload());
        final String timerName = new String(keys[0]);
        final String type = new String(keys[1]);
        // TODO: Deal with timers that come in and the module is tenant/channel disabled
        if(type.equals("tenant")) {
            final Tenant tenant;
            try {
                tenant = Tenant.fromProto(ChannelOuterClass.Tenant.parseFrom(keys[1]));
            } catch(InvalidProtocolBufferException e) {
                TimedMessages.LOG.warn("Failed to deserialize tenant from scheduler payload");
                e.printStackTrace();
                return;
            }
            final UUID uuid = ev.getId();
            Timer t = module.getTenantConfiguration(tenant).getTimer(timerName);
            int cursor = module.getCursor(uuid);
            if(cursor == -1) {
                TimedMessages.LOG.warn("Got a timer with no cursor; deleting it...");
                module.disableTimer(tenant, ev.getId());
                return;
            }
            final String message = t.nextEntry(cursor).getMessage();
            // TODO: Save cursor back to Redis
            module.saveCursor(uuid, cursor);
            for(Channel channel : tenant.getChannels()) {
                Chat.message(module, channel, message);
            }
        } else {
            TimedMessages.LOG.warn("Got unknown timer type {}", type);
        }
    }
}
