package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.HostTargetCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link TwitchChannel} starts hosting the video feed of another channel or stops hosting other channels
 * @author Cobi
 */
public class TwitchHostEvent extends Event {
    public static TwitchHostEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.TwitchHostEvent twitchHostEvent) {
        final TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchHostEvent.getChannel());
        final TwitchChannel targetChannel = twitchHostEvent.hasTargetChannel() ? (TwitchChannel) Channel.fromProto(twitchHostEvent.getTargetChannel()) : null;
        final HostTargetCommand hostTargetCommand = (HostTargetCommand) IrcStanza.fromProto(twitchHostEvent.getHostTargetCommand());
        return new TwitchHostEvent(module, uuid, timestamp, context, channel, targetChannel, hostTargetCommand);
    }

    private final TwitchChannel channel;
    private final TwitchChannel targetChannel;

    private final HostTargetCommand hostTargetCommand;

    public TwitchHostEvent(final Module from, final TwitchChannel channel, final TwitchChannel targetChannel, final HostTargetCommand hostTargetCommand) {
        super(from);
        this.channel = channel;
        this.targetChannel = targetChannel;
        this.hostTargetCommand = hostTargetCommand;
    }

    public TwitchHostEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final TwitchChannel channel, final TwitchChannel targetChannel, final HostTargetCommand hostTargetCommand) {
        super(from, messageId, timestamp, context);
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
        final EventOuterClass.TwitchHostEvent.Builder builder = EventOuterClass.TwitchHostEvent.newBuilder()
                .setChannel(channel.toProto());
        if(targetChannel != null)
            builder.setTargetChannel(targetChannel.toProto());
        return builder.setHostTargetCommand(hostTargetCommand.toProto())
                .build();
    }

}
