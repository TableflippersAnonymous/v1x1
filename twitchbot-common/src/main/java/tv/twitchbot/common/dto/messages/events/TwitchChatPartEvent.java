package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.core.User;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.PartCommand;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link tv.twitchbot.common.dto.core.TwitchUser} leaves a {@link tv.twitchbot.common.dto.core.TwitchChannel}
 * @author Cobi
 */
public class TwitchChatPartEvent extends ChatPartEvent {
    public static TwitchChatPartEvent fromProto(final Module module, final UUID uuid, final long timestamp, final User user, final Channel channel, final EventOuterClass.TwitchChatPartEventOrBuilder twitchChatPartEvent) {
        final PartCommand partCommand = (PartCommand) IrcStanza.fromProto(twitchChatPartEvent.getPartCommand());
        return new TwitchChatPartEvent(module, uuid, timestamp, user, channel, partCommand);
    }

    private final PartCommand partCommand;

    public TwitchChatPartEvent(final Module from, final User user, final Channel channel, final PartCommand partCommand) {
        super(from, user, channel);
        this.partCommand = partCommand;
    }

    public TwitchChatPartEvent(final Module from, final UUID messageId, final long timestamp, final User user, final Channel channel, final PartCommand partCommand) {
        super(from, messageId, timestamp, user, channel);
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
