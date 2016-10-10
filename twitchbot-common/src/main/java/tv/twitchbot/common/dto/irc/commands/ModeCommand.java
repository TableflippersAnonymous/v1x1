package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.List;
import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class ModeCommand extends IrcStanza {
    public static ModeCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.ModeCommand modeCommand) {
        String channel = modeCommand.getChannel();
        String modeString = modeCommand.getModeString();
        List<String> nicknames = modeCommand.getNicknamesList();
        return new ModeCommand(rawLine, tags, source, rawArgs, args, channel, modeString, nicknames);
    }

    private String channel;
    private String modeString;
    private List<String> nicknames;

    public ModeCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel, String modeString, List<String> nicknames) {
        super(rawLine, tags, source, IrcCommand.MODE, rawArgs, args);
        this.channel = channel;
        this.modeString = modeString;
        this.nicknames = nicknames;
    }

    public String getChannel() {
        return channel;
    }

    public String getModeString() {
        return modeString;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.MODE)
                .setExtension(IRC.ModeCommand.data, toProtoCommand());
    }

    private IRC.ModeCommand toProtoCommand() {
        return IRC.ModeCommand.newBuilder()
                .setChannel(channel)
                .setModeString(modeString)
                .addAllNicknames(nicknames)
                .build();
    }
}
