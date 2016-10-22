package tv.v1x1.common.dto.irc.commands;

import tv.v1x1.common.dto.irc.EmoteSetIrcStanza;
import tv.v1x1.common.dto.irc.IrcSource;
import tv.v1x1.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class UserStateCommand extends EmoteSetIrcStanza {
    public static UserStateCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.UserStateCommand userStateCommand) {
        final String channel = userStateCommand.getChannel();
        return new UserStateCommand(rawLine, tags, source, rawArgs, args, channel);
    }

    private final String channel;

    public UserStateCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel) {
        super(rawLine, tags, source, IrcCommand.USERSTATE, rawArgs, args);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.USERSTATE)
                .setExtension(IRC.UserStateCommand.data, toProtoCommand());
    }

    private IRC.UserStateCommand toProtoCommand() {
        return IRC.UserStateCommand.newBuilder()
                .setChannel(channel)
                .setEmoteSetStanza(toProtoEmoteSet())
                .build();
    }
}
