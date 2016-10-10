package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by naomi on 10/9/2016.
 */
public class PingCommand extends IrcStanza {
    public static PingCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.PingCommand pingCommand) {
        String token = pingCommand.getToken();
        return new PingCommand(rawLine, tags, source, rawArgs, args, token);
    }

    private String token;

    public PingCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String token) {
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
