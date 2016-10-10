package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.ModeCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 10/9/2016.
 */
public class TwitchUserModChangeEvent extends Event {
    public static TwitchUserModChangeEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchUserModChangeEvent twitchUserModChangeEvent) {
        TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchUserModChangeEvent.getChannel());
        TwitchUser user = (TwitchUser) User.fromProto(twitchUserModChangeEvent.getUser());
        boolean isNowMod = twitchUserModChangeEvent.getIsNowMod();
        ModeCommand modeCommand = (ModeCommand) IrcStanza.fromProto(twitchUserModChangeEvent.getModeCommand());
        return new TwitchUserModChangeEvent(module, uuid, timestamp, channel, user, isNowMod, modeCommand);
    }

    private TwitchChannel channel;
    private TwitchUser user;
    private boolean isNowMod;

    private ModeCommand modeCommand;

    public TwitchUserModChangeEvent(Module from, TwitchChannel channel, TwitchUser user, boolean isNowMod, ModeCommand modeCommand) {
        super(from);
        this.channel = channel;
        this.user = user;
        this.isNowMod = isNowMod;
        this.modeCommand = modeCommand;
    }

    public TwitchUserModChangeEvent(Module from, UUID messageId, long timestamp, TwitchChannel channel, TwitchUser user, boolean isNowMod, ModeCommand modeCommand) {
        super(from, messageId, timestamp);
        this.channel = channel;
        this.user = user;
        this.isNowMod = isNowMod;
        this.modeCommand = modeCommand;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public TwitchUser getUser() {
        return user;
    }

    public boolean isNowMod() {
        return isNowMod;
    }

    public ModeCommand getModeCommand() {
        return modeCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_USER_MOD_CHANGE)
                .setExtension(EventOuterClass.TwitchUserModChangeEvent.data, EventOuterClass.TwitchUserModChangeEvent.newBuilder()
                        .setChannel(channel.toProto())
                        .setUser(user.toProto())
                        .setIsNowMod(isNowMod)
                        .setModeCommand(modeCommand.toProto())
                        .build()
                );
    }
}
