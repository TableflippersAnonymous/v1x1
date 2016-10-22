package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class PartCommand extends IrcStanza {
    public static PartCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.PartCommand partCommand) {
        final String channel = partCommand.getChannel();
        return new PartCommand(rawLine, tags, source, rawArgs, args, channel);
    }

    private final String channel;

    public PartCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel) {
        super(rawLine, tags, source, IrcCommand.PART, rawArgs, args);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.PART)
                .setExtension(IRC.PartCommand.data, toProtoCommand());
    }

    private IRC.PartCommand toProtoCommand() {
        return IRC.PartCommand.newBuilder()
                .setChannel(channel)
                .build();
    }
}
