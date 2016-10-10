package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class HostTargetCommand extends IrcStanza {
    public static HostTargetCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.HostTargetCommand hostTargetCommand) {
        String channel = hostTargetCommand.getChannel();
        String targetChannel = hostTargetCommand.getTargetChannel();
        return new HostTargetCommand(rawLine, tags, source, rawArgs, args, channel, targetChannel);
    }

    private String channel;
    private String targetChannel;

    public HostTargetCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel, String targetChannel) {
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
