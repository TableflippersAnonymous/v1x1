package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.*;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.RoomStateCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when we receive the information about a {@link TwitchChannel}
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#roomstate-1">Twitch-API Documentation</a>
 * @author Naomi
 */
public class TwitchRoomStateEvent extends Event {
    public static TwitchRoomStateEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.TwitchRoomStateEvent twitchRoomStateEvent) {
        final TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchRoomStateEvent.getChannel());
        final RoomStateCommand roomStateCommand = (RoomStateCommand) IrcStanza.fromProto(twitchRoomStateEvent.getRoomStateCommand());
        return new TwitchRoomStateEvent(module, uuid, timestamp, context, channel, roomStateCommand);
    }

    private final TwitchChannel channel;

    private final RoomStateCommand roomStateCommand;

    public TwitchRoomStateEvent(final Module from, final TwitchChannel channel, final RoomStateCommand roomStateCommand) {
        super(from);
        this.channel = channel;
        this.roomStateCommand = roomStateCommand;
    }

    public TwitchRoomStateEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final TwitchChannel channel, final RoomStateCommand roomStateCommand) {
        super(from, messageId, timestamp, context);
        this.channel = channel;
        this.roomStateCommand = roomStateCommand;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public RoomStateCommand getRoomStateCommand() {
        return roomStateCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_ROOM_STATE)
                .setExtension(EventOuterClass.TwitchRoomStateEvent.data, EventOuterClass.TwitchRoomStateEvent.newBuilder()
                        .setChannel(channel.toProto())
                        .setRoomStateCommand(roomStateCommand.toProto())
                        .build()
                );
    }
}
