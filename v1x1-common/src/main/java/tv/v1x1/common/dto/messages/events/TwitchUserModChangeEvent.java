package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.*;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.ModeCommand;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when someone gained or lost Moderator status
 * @author Cobi
 */
public class TwitchUserModChangeEvent extends Event {
    public static TwitchUserModChangeEvent fromProto(final Module module, final UUID uuid, final long timestamp, final EventOuterClass.TwitchUserModChangeEvent twitchUserModChangeEvent) {
        final TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchUserModChangeEvent.getChannel());
        final TwitchUser user = (TwitchUser) User.fromProto(twitchUserModChangeEvent.getUser());
        final boolean isNowMod = twitchUserModChangeEvent.getIsNowMod();
        final ModeCommand modeCommand = (ModeCommand) IrcStanza.fromProto(twitchUserModChangeEvent.getModeCommand());
        return new TwitchUserModChangeEvent(module, uuid, timestamp, channel, user, isNowMod, modeCommand);
    }

    private final TwitchChannel channel;
    private final TwitchUser user;
    private final boolean isNowMod;

    private final ModeCommand modeCommand;

    public TwitchUserModChangeEvent(final Module from, final TwitchChannel channel, final TwitchUser user, final boolean isNowMod, final ModeCommand modeCommand) {
        super(from);
        this.channel = channel;
        this.user = user;
        this.isNowMod = isNowMod;
        this.modeCommand = modeCommand;
    }

    public TwitchUserModChangeEvent(final Module from, final UUID messageId, final long timestamp, final TwitchChannel channel, final TwitchUser user, final boolean isNowMod, final ModeCommand modeCommand) {
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
