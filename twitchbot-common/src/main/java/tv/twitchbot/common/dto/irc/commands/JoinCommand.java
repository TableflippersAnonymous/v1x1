package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;


/**
 * Created by cobi on 10/8/2016.
 */
public class JoinCommand extends IrcStanza {
    public static JoinCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.JoinCommand joinCommand) {
        final String channel = joinCommand.getChannel();
        return new JoinCommand(rawLine, tags, source, rawArgs, args, channel);
    }

    private final String channel;

    public JoinCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel) {
        super(rawLine, tags, source, IrcCommand.JOIN, rawArgs, args);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.JOIN)
                .setExtension(IRC.JoinCommand.data, toProtoCommand());
    }

    private IRC.JoinCommand toProtoCommand() {
        return IRC.JoinCommand.newBuilder()
                .setChannel(channel)
                .build();
    }
}
