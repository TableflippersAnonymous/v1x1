package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class HostTargetCommand extends IrcStanza {
    public static HostTargetCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.HostTargetCommandOrBuilder hostTargetCommand) {
        final String channel = hostTargetCommand.getChannel();
        final String targetChannel = hostTargetCommand.getTargetChannel();
        return new HostTargetCommand(rawLine, tags, source, rawArgs, args, channel, targetChannel);
    }

    private final String channel;
    private final String targetChannel;

    public HostTargetCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel, final String targetChannel) {
        super(rawLine, tags, source, IrcCommand.HOSTTARGET, rawArgs, args);
        this.channel = channel;
        this.targetChannel = targetChannel;
    }

    public String getChannel() {
        return channel;
    }

    public String getTargetChannel() {
        return targetChannel;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.HOSTTARGET)
                .setExtension(IRC.HostTargetCommand.data, toProtoCommand());
    }

    private IRC.HostTargetCommand toProtoCommand() {
        return IRC.HostTargetCommand.newBuilder()
                .setChannel(channel)
                .setTargetChannel(targetChannel)
                .build();
    }
}
