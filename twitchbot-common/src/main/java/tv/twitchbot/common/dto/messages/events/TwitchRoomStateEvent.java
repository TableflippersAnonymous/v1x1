package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.RoomStateCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 10/9/2016.
 */
public class TwitchRoomStateEvent extends Event {
    public static TwitchRoomStateEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchRoomStateEvent twitchRoomStateEvent) {
        TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchRoomStateEvent.getChannel());
        RoomStateCommand roomStateCommand = (RoomStateCommand) IrcStanza.fromProto(twitchRoomStateEvent.getRoomStateCommand());
        return new TwitchRoomStateEvent(module, uuid, timestamp, channel, roomStateCommand);
    }

    private TwitchChannel channel;

    private RoomStateCommand roomStateCommand;

    public TwitchRoomStateEvent(Module from, TwitchChannel channel, RoomStateCommand roomStateCommand) {
        super(from);
        this.channel = channel;
        this.roomStateCommand = roomStateCommand;
    }

    public TwitchRoomStateEvent(Module from, UUID messageId, long timestamp, TwitchChannel channel, RoomStateCommand roomStateCommand) {
        super(from, messageId, timestamp);
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
