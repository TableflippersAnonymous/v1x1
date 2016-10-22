package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class ClearChatCommand extends IrcStanza {
    public static ClearChatCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.ClearChatCommand clearChatCommand) {
        final String channel = clearChatCommand.getChannel();
        final String nickname = clearChatCommand.getNickname();
        return new ClearChatCommand(rawLine, tags, source, rawArgs, args, channel, nickname);
    }

    private final String channel;
    private final String nickname;

    private int banDuration;
    private String banReason;

    public ClearChatCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel, final String nickname) {
        super(rawLine, tags, source, IrcCommand.CLEARCHAT, rawArgs, args);
        this.channel = channel;
        this.nickname = nickname;
        if(tags.containsKey("ban-duration") && !tags.get("ban-duration").isEmpty())
            banDuration = Integer.valueOf(tags.get("ban-duration"));
        if(tags.containsKey("ban-reason") && !tags.get("ban-reason").isEmpty())
            banReason = tags.get("ban-reason");
    }

    public String getChannel() {
        return channel;
    }

    public String getNickname() {
        return nickname;
    }

    public int getBanDuration() {
        return banDuration;
    }

    public String getBanReason() {
        return banReason;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.CLEARCHAT)
                .setExtension(IRC.ClearChatCommand.data, toProtoCommand());
    }

    private IRC.ClearChatCommand toProtoCommand() {
        final IRC.ClearChatCommand.Builder builder = IRC.ClearChatCommand.newBuilder()
                .setChannel(channel)
                .setNickname(nickname)
                .setBanDuration(banDuration);
        if(banReason != null)
            builder.setBanReason(banReason);
        return builder.build();
    }
}
