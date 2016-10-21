package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.MessageTaggedIrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;
import java.util.UUID;

/**
 * Created by cobi on 10/8/2016.
 */
public class PrivmsgCommand extends MessageTaggedIrcStanza {
    public static PrivmsgCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.PrivmsgCommandOrBuilder privmsgCommand) {
        final String channel = privmsgCommand.getChannel();
        final String message = privmsgCommand.getMessage();
        return new PrivmsgCommand(rawLine, tags, source, rawArgs, args, channel, message);
    }

    private final String channel;
    private final String message;

    private UUID id;
    private int bits;

    public PrivmsgCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel, final String message) {
        super(rawLine, tags, source, IrcCommand.PRIVMSG, rawArgs, args);
        this.channel = channel;
        this.message = message;

        if(tags.containsKey("id") && !tags.get("id").isEmpty())
            id = UUID.fromString(tags.get("id"));
        if(tags.containsKey("bits") && !tags.get("bits").isEmpty())
            bits = Integer.valueOf(tags.get("bits"));
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public UUID getId() {
        return id;
    }

    public int getBits() {
        return bits;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.PRIVMSG)
                .setExtension(IRC.PrivmsgCommand.data, toProtoCommand());
    }

    private IRC.PrivmsgCommand toProtoCommand() {
        final IRC.PrivmsgCommand.Builder builder = IRC.PrivmsgCommand.newBuilder()
                .setChannel(channel)
                .setMessage(message);
        if(id != null)
            builder.setId(new tv.twitchbot.common.dto.core.UUID(id).toProto());
        return builder.setBits(bits)
                .setMessageTaggedStanza(toProtoMessageTagged())
                .build();
    }
}
