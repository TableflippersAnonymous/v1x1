package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by naomi on 10/9/2016.
 */
public class PingCommand extends IrcStanza {
    public static PingCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.PingCommand pingCommand) {
        final String token = pingCommand.getToken();
        return new PingCommand(rawLine, tags, source, rawArgs, args, token);
    }

    private final String token;

    public PingCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String token) {
        super(rawLine, tags, source, IrcCommand.PING, rawArgs, args);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.PING)
                .setExtension(IRC.PingCommand.data, toProtoCommand());
    }

    private IRC.PingCommand toProtoCommand() {
        return IRC.PingCommand.newBuilder()
                .setToken(token)
                .build();
    }
}
