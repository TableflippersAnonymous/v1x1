package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.IrcStanza;

import java.util.List;
import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class RplNameReplyCommand extends IrcStanza {
    public static class Member {
        private String nickname;
        private boolean isOp;

        public Member(String encoded) {
            if(encoded.startsWith("@")) {
                nickname = encoded.substring(1);
                isOp = true;
            } else {
                nickname = encoded;
                isOp = false;
            }
        }

        public Member(String nickname, boolean isOp) {
            this.nickname = nickname;
            this.isOp = isOp;
        }
    }
    private String channel;
    private List<Member> members;

    public RplNameReplyCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel, List<Member> members) {
        super(rawLine, tags, source, IrcCommand.RPL_NAMREPLY, rawArgs, args);
        this.channel = channel;
        this.members = members;
    }

    public String getChannel() {
        return channel;
    }

    public List<Member> getMembers() {
        return members;
    }
}
