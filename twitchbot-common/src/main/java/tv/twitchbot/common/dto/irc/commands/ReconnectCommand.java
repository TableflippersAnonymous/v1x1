package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class ReconnectCommand extends IrcStanza {
    public static ReconnectCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.ReconnectCommand reconnectCommand) {
        return new ReconnectCommand(rawLine, tags, source, rawArgs, args);
    }

    public ReconnectCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args) {
        super(rawLine, tags, source, IrcCommand.RECONNECT, rawArgs, args);
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.RECONNECT)
                .setExtension(IRC.ReconnectCommand.data, toProtoCommand());
    }

    private IRC.ReconnectCommand toProtoCommand() {
        return IRC.ReconnectCommand.newBuilder()
                .build();
    }
}
