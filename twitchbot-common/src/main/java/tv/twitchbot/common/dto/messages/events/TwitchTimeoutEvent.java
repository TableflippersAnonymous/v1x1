package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.ClearChatCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 10/9/2016.
 */
public class TwitchTimeoutEvent extends Event {
    public static TwitchTimeoutEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchTimeoutEvent twitchTimeoutEvent) {
        TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchTimeoutEvent.getChannel());
        TwitchUser user = (TwitchUser) User.fromProto(twitchTimeoutEvent.getUser());
        ClearChatCommand clearChatCommand = (ClearChatCommand) IrcStanza.fromProto(twitchTimeoutEvent.getClearChatCommand());
        return new TwitchTimeoutEvent(module, uuid, timestamp, channel, user, clearChatCommand);
    }

    private TwitchChannel channel;
    private TwitchUser user;

    private ClearChatCommand clearChatCommand;

    public TwitchTimeoutEvent(Module from, TwitchChannel channel, TwitchUser user, ClearChatCommand clearChatCommand) {
        super(from);
        this.channel = channel;
        this.user = user;
        this.clearChatCommand = clearChatCommand;
    }

    public TwitchTimeoutEvent(Module from, UUID messageId, long timestamp, TwitchChannel channel, TwitchUser user, ClearChatCommand clearChatCommand) {
        super(from, messageId, timestamp);
        this.channel = channel;
        this.user = user;
        this.clearChatCommand = clearChatCommand;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public TwitchUser getUser() {
        return user;
    }

    public ClearChatCommand getClearChatCommand() {
        return clearChatCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_TIMEOUT)
                .setExtension(EventOuterClass.TwitchTimeoutEvent.data, EventOuterClass.TwitchTimeoutEvent.newBuilder()
                        .setChannel(channel.toProto())
                        .setUser(user.toProto())
                        .setClearChatCommand(clearChatCommand.toProto())
                        .build()
                );
    }
}
