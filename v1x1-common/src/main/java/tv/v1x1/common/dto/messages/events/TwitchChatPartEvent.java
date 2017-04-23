package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.PartCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.proto.core.PlatformOuterClass;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link tv.v1x1.common.dto.core.TwitchUser} leaves a {@link tv.v1x1.common.dto.core.TwitchChannel}
 * @author Naomi
 */
public class TwitchChatPartEvent extends ChatPartEvent {
    public static TwitchChatPartEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final User user, final Channel channel, final EventOuterClass.TwitchChatPartEvent twitchChatPartEvent) {
        final PartCommand partCommand = (PartCommand) IrcStanza.fromProto(twitchChatPartEvent.getPartCommand());
        return new TwitchChatPartEvent(module, uuid, timestamp, context, user, channel, partCommand);
    }

    private final PartCommand partCommand;

    public TwitchChatPartEvent(final Module from, final User user, final Channel channel, final PartCommand partCommand) {
        super(from, user, channel);
        this.partCommand = partCommand;
    }

    public TwitchChatPartEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final User user, final Channel channel, final PartCommand partCommand) {
        super(from, messageId, timestamp, context, user, channel);
        this.partCommand = partCommand;
    }

    public PartCommand getPartCommand() {
        return partCommand;
    }

    @Override
    protected EventOuterClass.ChatPartEvent.Builder toProtoChatPart() {
        return super.toProtoChatPart()
                .setType(PlatformOuterClass.Platform.TWITCH)
                .setExtension(EventOuterClass.TwitchChatPartEvent.data, EventOuterClass.TwitchChatPartEvent.newBuilder()
                        .setPartCommand(partCommand.toProto())
                        .build()
                );
    }
}
