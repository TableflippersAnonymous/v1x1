package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.List;
import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class ModeCommand extends IrcStanza {
    public static ModeCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.ModeCommandOrBuilder modeCommand) {
        final String channel = modeCommand.getChannel();
        final String modeString = modeCommand.getModeString();
        final List<String> nicknames = modeCommand.getNicknamesList();
        return new ModeCommand(rawLine, tags, source, rawArgs, args, channel, modeString, nicknames);
    }

    private final String channel;
    private final String modeString;
    private final List<String> nicknames;

    public ModeCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel, final String modeString, final List<String> nicknames) {
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

    public Iterable<String> getNicknames() {
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
