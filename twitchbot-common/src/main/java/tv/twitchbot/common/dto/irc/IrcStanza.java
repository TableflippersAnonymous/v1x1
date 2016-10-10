package tv.twitchbot.common.dto.irc;

import tv.twitchbot.common.dto.irc.commands.*;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public abstract class IrcStanza {
    public static IrcStanza fromProto(IRC.IrcStanza stanza) {
        String rawLine = stanza.getRawLine();
        Map<String, String> tags = stanza.getTagsMap();
        IrcSource source = IrcSource.fromProto(stanza.getSource());
        String rawArgs = stanza.getRawArgs();
        String[] args = stanza.getArgsList().toArray(new String[] {});
        switch(stanza.getCommand()) {
            case CLEARCHAT: return ClearChatCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.ClearChatCommand.data));
            case GLOBALUSERSTATE: return GlobalUserStateCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.GlobalUserStateCommand.data));
            case HOSTTARGET: return HostTargetCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.HostTargetCommand.data));
            case JOIN: return JoinCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.JoinCommand.data));
            case MODE: return ModeCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.ModeCommand.data));
            case NOTICE: return NoticeCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.NoticeCommand.data));
            case PART: return PartCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.PartCommand.data));
            case PING: return PingCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.PingCommand.data));
            case PRIVMSG: return PrivmsgCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.PrivmsgCommand.data));
            case RECONNECT: return ReconnectCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.ReconnectCommand.data));
            case ROOMSTATE: return RoomStateCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.RoomStateCommand.data));
            case RPL_ENDOFMOTD: return RplEndOfMotdCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.RplEndOfMotdCommand.data));
            case RPL_NAMREPLY: return RplNameReplyCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.RplNameReplyCommand.data));
            case USERNOTICE: return UserNoticeCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.UserNoticeCommand.data));
            case USERSTATE: return UserStateCommand.fromProto(rawLine, tags, source, rawArgs, args, stanza.getExtension(IRC.UserStateCommand.data));
            default: throw new IllegalStateException("Unknown serialized IRC command: " + stanza.getCommand());
        }
    }

    public enum IrcCommand {
        JOIN, PART, PRIVMSG,
        RPL_NAMREPLY, RPL_ENDOFMOTD,
        MODE, NOTICE, HOSTTARGET,
        CLEARCHAT, USERSTATE,
        RECONNECT, ROOMSTATE,
        USERNOTICE, GLOBALUSERSTATE,
        PING;
    }

    private String rawLine;
    private Map<String, String> tags;
    private IrcSource source;
    private IrcCommand command;
    private String rawArgs;
    private String[] args;

    public IrcStanza(String rawLine, Map<String, String> tags, IrcSource source, IrcCommand command, String rawArgs, String[] args) {
        this.rawLine = rawLine;
        this.tags = tags;
        this.source = source;
        this.command = command;
        this.rawArgs = rawArgs;
        this.args = args;
    }

    public String getRawLine() {
        return rawLine;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public IrcSource getSource() {
        return source;
    }

    public IrcCommand getCommand() {
        return command;
    }

    public String getRawArgs() {
        return rawArgs;
    }

    public String[] getArgs() {
        return args;
    }

    public IRC.IrcStanza toProto() {
        return toProtoBuilder().build();
    }

    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return IRC.IrcStanza.newBuilder()
                .setRawLine(rawLine)
                .putAllTags(tags)
                .setSource(source.toProto())
                .setRawArgs(rawArgs)
                .addAllArgs(Arrays.asList(args));
    }
}
