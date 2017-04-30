package tv.v1x1.modules.channel.event_messages;

import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.irc.commands.UserNoticeCommand;
import tv.v1x1.common.dto.messages.events.TwitchUserEvent;
import tv.v1x1.common.modules.eventhandler.EventListener;

/**
 * @author Josh
 */
public class EventMessagesListener implements EventListener {
    private EventMessagesModule module;

    EventMessagesListener(final EventMessagesModule module) {
        this.module = module;
    }

    public void onTwitchUser(TwitchUserEvent ev) {
        final TwitchChannel channel = ev.getChannel();
        if(!module.isEnabled(channel))
            return;
        final UserNoticeCommand.MessageId type = ev.getUserNoticeCommand().getMessageId();
        if(type.equals(UserNoticeCommand.MessageId.SUB) || type.equals(UserNoticeCommand.MessageId.RESUB)) {
            final boolean isResub = type.equals(UserNoticeCommand.MessageId.RESUB);
            module.sendTwitchSub(channel, ev.getUser(), isResub, ev.getUserNoticeCommand().getPlanLevel(), ev.getUserNoticeCommand().getPlanName(), ev.getUserNoticeCommand().getMonths());
        }
    }
}
