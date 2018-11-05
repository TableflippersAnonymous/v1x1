package tv.v1x1.modules.core.tmi;

import brave.Span;
import brave.Tracer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import tv.v1x1.common.dto.irc.IrcServer;
import tv.v1x1.common.dto.irc.IrcSource;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.IrcUser;
import tv.v1x1.common.dto.irc.commands.ClearChatCommand;
import tv.v1x1.common.dto.irc.commands.GlobalUserStateCommand;
import tv.v1x1.common.dto.irc.commands.HostTargetCommand;
import tv.v1x1.common.dto.irc.commands.JoinCommand;
import tv.v1x1.common.dto.irc.commands.ModeCommand;
import tv.v1x1.common.dto.irc.commands.NoticeCommand;
import tv.v1x1.common.dto.irc.commands.PartCommand;
import tv.v1x1.common.dto.irc.commands.PingCommand;
import tv.v1x1.common.dto.irc.commands.PrivmsgCommand;
import tv.v1x1.common.dto.irc.commands.ReconnectCommand;
import tv.v1x1.common.dto.irc.commands.RoomStateCommand;
import tv.v1x1.common.dto.irc.commands.RplEndOfMotdCommand;
import tv.v1x1.common.dto.irc.commands.RplNameReplyCommand;
import tv.v1x1.common.dto.irc.commands.UserNoticeCommand;
import tv.v1x1.common.dto.irc.commands.UserStateCommand;
import tv.v1x1.common.dto.irc.commands.WhisperCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/8/2016.
 */
public class IrcParser {
    public static IrcStanza parse(final String line, final Tracer tracer, final Span rootSpan) {
        final Span span = tracer.newChild(rootSpan.context()).name("TMI parse").start();
        try {
            final String[] tagParts = splitTagFromIrc(line);
            final Map<String, String> tagMap = parseTags(tagParts[0]);
            final String[] parts = splitIrcLine(tagParts[1]);
            final IrcSource source = parseSource(parts[0]);
            final IrcStanza.IrcCommand command = parseCommand(parts[1]);
            if (command == null)
                return null;
            final String rawArgs = parts.length > 2 ? parts[2] : null;
            final List<String> args = parseArgs(rawArgs);
            return buildStanza(line, tagMap, source, command, rawArgs, args.toArray(new String[]{}));
        } finally {
            span.finish();
        }
    }

    private static IrcStanza buildStanza(final String rawLine, final Map<String, String> tags, final IrcSource source, final IrcStanza.IrcCommand command, final String rawArgs, final String[] args) {
        switch(command) {
            case CLEARCHAT: return new ClearChatCommand(rawLine, tags, source, rawArgs, args, args[0], args[1]);
            case GLOBALUSERSTATE: return new GlobalUserStateCommand(rawLine, tags, source, rawArgs, args);
            case HOSTTARGET: return new HostTargetCommand(rawLine, tags, source, rawArgs, args, args[0], args[1].split(" ")[0]);
            case JOIN: return new JoinCommand(rawLine, tags, source, rawArgs, args, args[0]);
            case MODE: return new ModeCommand(rawLine, tags, source, rawArgs, args, args[0], args[1], Arrays.asList(Arrays.copyOfRange(args, 2, args.length)));
            case NOTICE: return new NoticeCommand(rawLine, tags, source, rawArgs, args, args[0], args[1]);
            case PART: return new PartCommand(rawLine, tags, source, rawArgs, args, args[0]);
            case PING: return new PingCommand(rawLine, tags, source, rawArgs, args, args[0]);
            case PRIVMSG: return new PrivmsgCommand(rawLine, tags, source, rawArgs, args, args[0], args[1]);
            case RECONNECT: return new ReconnectCommand(rawLine, tags, source, rawArgs, args);
            case ROOMSTATE: return new RoomStateCommand(rawLine, tags, source, rawArgs, args, args[0]);
            case RPL_ENDOFMOTD: return new RplEndOfMotdCommand(rawLine, tags, source, rawArgs, args);
            case RPL_NAMREPLY: return new RplNameReplyCommand(rawLine, tags, source, rawArgs, args, args[2], Arrays.stream(args[3].split(" ")).map(RplNameReplyCommand.Member::new).collect(Collectors.toList()));
            case USERNOTICE: return new UserNoticeCommand(rawLine, tags, source, rawArgs, args, args[0], args.length < 2 ? tags.get("system-msg") : args[1]);
            case USERSTATE: return new UserStateCommand(rawLine, tags, source, rawArgs, args, args[0]);
            case WHISPER: return new WhisperCommand(rawLine, tags, source, rawArgs, args, args[0], args[1]);
            default: throw new IllegalStateException("Unknown command: " + command);
        }
    }

    private static IrcStanza.IrcCommand parseCommand(final String command) {
        if(command.equals("353"))
            return IrcStanza.IrcCommand.RPL_NAMREPLY;
        if(command.equals("376"))
            return IrcStanza.IrcCommand.RPL_ENDOFMOTD;
        try {
            return IrcStanza.IrcCommand.valueOf(command.toUpperCase());
        } catch(final IllegalArgumentException e) {
            /* Unsupported IRC command. */
            return null;
        }
    }

    private static String[] splitIrcLine(String line) {
        if(!line.startsWith(":"))
            line = ":tmi.twitch.tv " + line;
        return line.substring(1).split(" ", 3);
    }

    private static String[] splitTagFromIrc(String line) {
        /* [tags] <source> <command> [args] */
        String tags = null;
        if(line.startsWith("@")) {
            final String[] parts = line.split(" ", 2);
            tags = parts[0];
            line = parts[1];
        }
        return new String[] { tags, line };
    }

    private static IrcSource parseSource(final String source) {
        final IrcSource ircSource;
        if(source.contains("!")) {
            final String[] nameParts = source.split("!", 2);
            final String nickname = nameParts[0];
            final String[] userParts = nameParts[1].split("@", 2);
            final String username = userParts[0];
            final String hostname = userParts[1];
            ircSource = new IrcUser(nickname, username, hostname);
        } else {
            ircSource = new IrcServer(source);
        }
        return ircSource;
    }

    private static Map<String, String> parseTags(String tags) {
        if(tags == null)
            return ImmutableMap.of();
        if(tags.startsWith("@"))
            tags = tags.substring(1);
        final Map<String, String> tagMap = new HashMap<>();
        final String[] tagArray = tags.split(";");
        for(final String tag : tagArray) {
            final String[] tagParts = tag.split("=", 2);
            final String key = tagParts[0];
            final String value = unescape(tagParts[1]);
            tagMap.put(key, value);
        }
        return tagMap;
    }

    private static String unescape(final CharSequence escaped) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for(; i < escaped.length() - 1; i++) {
            if(escaped.charAt(i) == '\\') {
                switch(escaped.charAt(i + 1)) {
                    case ':': sb.append(';'); break;
                    case '\\': sb.append('\\'); break;
                    case 's': sb.append(' '); break;
                    case 'r': sb.append('\r'); break;
                    case 'n': sb.append('\n'); break;
                    default:
                        sb.append(escaped.charAt(i));
                        sb.append(escaped.charAt(i + 1));
                }
                i++;
            } else {
                sb.append(escaped.charAt(i));
            }
        }
        if(i < escaped.length())
            sb.append(escaped.charAt(i));
        return sb.toString();
    }

    private static List<String> parseArgs(final String args) {
        if(args == null || args.isEmpty())
            return ImmutableList.of();
        final List<String> argList = new ArrayList<>();
        final String[] splitArgs = args.split(" ");
        for(int i = 0; i < splitArgs.length; i++) {
            if(splitArgs[i].isEmpty())
                throw new RuntimeException("Error parsing args: <middle> is not allowed to be empty.  args = " + args);
            if(splitArgs[i].charAt(0) == ':') {
                final String rest = args.split(" ", i + 1)[i];
                argList.add(rest.substring(1));
                break;
            } else {
                argList.add(splitArgs[i]);
            }
        }
        return argList;
    }
}
