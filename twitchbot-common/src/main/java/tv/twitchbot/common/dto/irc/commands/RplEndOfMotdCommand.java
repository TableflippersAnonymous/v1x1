package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class RplEndOfMotdCommand extends IrcStanza {
    public static RplEndOfMotdCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.RplEndOfMotdCommand rplEndOfMotdCommand) {
        return new RplEndOfMotdCommand(rawLine, tags, source, rawArgs, args);
    }

    public RplEndOfMotdCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args) {
        super(rawLine, tags, source, IrcCommand.RPL_ENDOFMOTD, rawArgs, args);
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.RPL_ENDOFMOTD)
                .setExtension(IRC.RplEndOfMotdCommand.data, toProtoCommand());
    }

    private IRC.RplEndOfMotdCommand toProtoCommand() {
        return IRC.RplEndOfMotdCommand.newBuilder()
                .build();
    }
}
