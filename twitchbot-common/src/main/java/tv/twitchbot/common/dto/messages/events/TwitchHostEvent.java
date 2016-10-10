package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.HostTargetCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

import java.util.stream.Collectors;

/**
 * Created by naomi on 10/9/2016.
 */
public class TwitchHostEvent extends Event {
    public static TwitchHostEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchHostEvent twitchHostEvent) {
        TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchHostEvent.getChannel());
        TwitchChannel targetChannel = (TwitchChannel) Channel.fromProto(twitchHostEvent.getTargetChannel());
        HostTargetCommand hostTargetCommand = (HostTargetCommand) IrcStanza.fromProto(twitchHostEvent.getHostTargetCommand());
        return new TwitchHostEvent(module, uuid, timestamp, channel, targetChannel, hostTargetCommand);
    }

    private TwitchChannel channel;
    private TwitchChannel targetChannel;

    private HostTargetCommand hostTargetCommand;

    public TwitchHostEvent(Module from, TwitchChannel channel, TwitchChannel targetChannel, HostTargetCommand hostTargetCommand) {
        super(from);
        this.channel = channel;
        this.targetChannel = targetChannel;
        this.hostTargetCommand = hostTargetCommand;
    }

    public TwitchHostEvent(Module from, UUID messageId, long timestamp, TwitchChannel channel, TwitchChannel targetChannel, HostTargetCommand hostTargetCommand) {
        super(from, messageId, timestamp);
        this.channel = channel;
        this.targetChannel = targetChannel;
        this.hostTargetCommand = hostTargetCommand;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public TwitchChannel getTargetChannel() {
        return targetChannel;
    }

    public HostTargetCommand getHostTargetCommand() {
        return hostTargetCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_HOST)
                .setExtension(EventOuterClass.TwitchHostEvent.data, toProtoHostEvent());
    }

    private EventOuterClass.TwitchHostEvent toProtoHostEvent() {
        EventOuterClass.TwitchHostEvent.Builder builder = EventOuterClass.TwitchHostEvent.newBuilder()
                .setChannel(channel.toProto());
        if(targetChannel != null)
            builder.setTargetChannel(targetChannel.toProto());
        return builder.setHostTargetCommand(hostTargetCommand.toProto())
                .build();
    }

}
