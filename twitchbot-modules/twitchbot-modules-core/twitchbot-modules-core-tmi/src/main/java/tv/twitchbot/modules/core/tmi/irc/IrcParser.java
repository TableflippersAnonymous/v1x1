package tv.twitchbot.modules.core.tmi.irc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import tv.twitchbot.modules.core.tmi.irc.commands.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/8/2016.
 */
public class IrcParser {
    public static IrcStanza parse(String line) {
        String[] tagParts = splitTagFromIrc(line);
        Map<String, String> tagMap = parseTags(tagParts[0]);
        String[] parts = splitIrcLine(tagParts[1]);
        IrcSource source = parseSource(parts[0]);
        IrcStanza.IrcCommand command = parseCommand(parts[1]);
        String rawArgs = parts[2];
        List<String> args = parseArgs(rawArgs);
        return buildStanza(line, tagMap, source, command, rawArgs, args.toArray(new String[] {}));
    }

    private static IrcStanza buildStanza(String rawLine, Map<String, String> tags, IrcSource source, IrcStanza.IrcCommand command, String rawArgs, String[] args) {
        switch(command) {
            case CLEARCHAT: return new ClearChatCommand(rawLine, tags, source, rawArgs, args, args[0], args[1]);
            case GLOBALUSERSTATE: return new GlobalUserStateCommand(rawLine, tags, source, rawArgs, args);
            case HOSTTARGET: return new HostTargetCommand(rawLine, tags, source, rawArgs, args, args[0], args[1]);
            case JOIN: return new JoinCommand(rawLine, tags, source, rawArgs, args, args[0]);
            case MODE: return new ModeCommand(rawLine, tags, source, rawArgs, args, args[0], args[1], Arrays.asList(Arrays.copyOfRange(args, 2, args.length + 1)));
            case NOTICE: return new NoticeCommand(rawLine, tags, source, rawArgs, args, args[0], args[1]);
            case PART: return new PartCommand(rawLine, tags, source, rawArgs, args, args[0]);
            case PING: return new PingCommand(rawLine, tags, source, rawArgs, args, args[0]);
            case PRIVMSG: return new PrivmsgCommand(rawLine, tags, source, rawArgs, args, args[0], args[1]);
            case RECONNECT: return new ReconnectCommand(rawLine, tags, source, rawArgs, args);
            case ROOMSTATE: return new RoomStateCommand(rawLine, tags, source, rawArgs, args, args[0]);
            case RPL_ENDOFMOTD: return new RplEndOfMotdCommand(rawLine, tags, source, rawArgs, args);
            case RPL_NAMREPLY: return new RplNameReplyCommand(rawLine, tags, source, rawArgs, args, args[0], Arrays.asList(Arrays.copyOfRange(args, 1, args.length + 1)).stream().map(RplNameReplyCommand.Member::new).collect(Collectors.toList()));
            case USERNOTICE: return new UserNoticeCommand(rawLine, tags, source, rawArgs, args, args[0], args[1]);
            case USERSTATE: return new UserStateCommand(rawLine, tags, source, rawArgs, args, args[0]);
            default: throw new IllegalStateException("Unknown command: " + command);
        }
    }

    private static IrcStanza.IrcCommand parseCommand(String command) {
        if(command.equals("353"))
            return IrcStanza.IrcCommand.RPL_NAMREPLY;
        if(command.equals("376"))
            return IrcStanza.IrcCommand.RPL_ENDOFMOTD;
        return IrcStanza.IrcCommand.valueOf(command.toUpperCase());
    }

    private static String[] splitIrcLine(String line) {
        if(!line.startsWith(":"))
            throw new RuntimeException("Unparsable TMI line: " + line);
        return line.substring(1).split(" ", 3);
    }

    private static String[] splitTagFromIrc(String line) {
        /* [tags] <source> <command> [args] */
        String tags = null;
        if(line.startsWith("@")) {
            String[] parts = line.split(" ", 2);
            tags = parts[0];
            line = parts[1];
        }
        return new String[] { tags, line };
    }

    private static IrcSource parseSource(String source) {
        IrcSource ircSource;
        if(source.contains("!")) {
            String[] nameParts = source.split("!", 2);
            String nickname = nameParts[0];
            String[] userParts = nameParts[1].split("@", 2);
            String username = userParts[0];
            String hostname = userParts[1];
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
        Map<String, String> tagMap = new HashMap<>();
        String[] tagArray = tags.split(";");
        for(String tag : tagArray) {
            String[] tagParts = tag.split("=", 2);
            String key = tagParts[0];
            String value = unescape(tagParts[1]);
            tagMap.put(key, value);
        }
        return tagMap;
    }

    private static String unescape(String escaped) {
        StringBuilder sb = new StringBuilder();
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

    private static List<String> parseArgs(String args) {
        if(args == null || args.isEmpty())
            return ImmutableList.of();
        List<String> argList = new ArrayList<>();
        String[] splitArgs = args.split(" ");
        for(int i = 0; i < splitArgs.length; i++) {
            if(splitArgs[i].isEmpty())
                throw new RuntimeException("Error parsing args: <middle> is not allowed to be empty.  args = " + args);
            if(splitArgs[i].charAt(0) == ':') {
                String rest = args.split(" ", i + 1)[i];
                argList.add(rest.substring(1));
                break;
            } else {
                argList.add(splitArgs[i]);
            }
        }
        return argList;
    }
}
