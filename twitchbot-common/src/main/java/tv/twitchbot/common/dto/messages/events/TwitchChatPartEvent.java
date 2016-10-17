package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.core.User;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.PartCommand;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link tv.twitchbot.common.dto.core.TwitchUser} leaves a {@link tv.twitchbot.common.dto.core.TwitchChannel}
 * @author Naomi
 */
public class TwitchChatPartEvent extends ChatPartEvent {
    public static TwitchChatPartEvent fromProto(Module module, UUID uuid, long timestamp, User user, Channel channel, EventOuterClass.TwitchChatPartEvent twitchChatPartEvent) {
        PartCommand partCommand = (PartCommand) IrcStanza.fromProto(twitchChatPartEvent.getPartCommand());
        return new TwitchChatPartEvent(module, uuid, timestamp, user, channel, partCommand);
    }

    private PartCommand partCommand;

    public TwitchChatPartEvent(Module from, User user, Channel channel, PartCommand partCommand) {
        super(from, user, channel);
        this.partCommand = partCommand;
    }

    public TwitchChatPartEvent(Module from, UUID messageId, long timestamp, User user, Channel channel, PartCommand partCommand) {
        super(from, messageId, timestamp, user, channel);
        this.partCommand = partCommand;
    }

    public PartCommand getPartCommand() {
        return partCommand;
    }

    @Override
    protected EventOuterClass.ChatPartEvent.Builder toProtoChatPart() {
        return super.toProtoChatPart()
                .setType(EventOuterClass.ChatPartEvent.Type.TWITCH)
                .setExtension(EventOuterClass.TwitchChatPartEvent.data, EventOuterClass.TwitchChatPartEvent.newBuilder()
                        .setPartCommand(partCommand.toProto())
                        .build()
                );
    }
}
