package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.MessageTaggedIrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.*;
import java.util.UUID;

/**
 * Created by cobi on 10/8/2016.
 */
public class PrivmsgCommand extends MessageTaggedIrcStanza {
    public static PrivmsgCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.PrivmsgCommand privmsgCommand) {
        String channel = privmsgCommand.getChannel();
        String message = privmsgCommand.getMessage();
        return new PrivmsgCommand(rawLine, tags, source, rawArgs, args, channel, message);
    }

    private String channel;
    private String message;

    private UUID id;
    private int bits;

    public PrivmsgCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel, String message) {
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
        IRC.PrivmsgCommand.Builder builder = IRC.PrivmsgCommand.newBuilder()
                .setChannel(channel)
                .setMessage(message);
        if(id != null)
            builder.setId(new tv.twitchbot.common.dto.core.UUID(id).toProto());
        return builder.setBits(bits)
                .setMessageTaggedStanza(toProtoMessageTagged())
                .build();
    }
}
