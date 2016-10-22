package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/8/2016.
 */
public class RplNameReplyCommand extends IrcStanza {
    public static RplNameReplyCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.RplNameReplyCommand rplNameReplyCommand) {
        final String channel = rplNameReplyCommand.getChannel();
        final List<Member> members = rplNameReplyCommand.getMembersList().stream().map(Member::fromProto).collect(Collectors.toList());
        return new RplNameReplyCommand(rawLine, tags, source, rawArgs, args, channel, members);
    }

    public static class Member {
        public static Member fromProto(final IRC.RplNameReplyCommand.Member member) {
            final String nickname = member.getNickname();
            final boolean isOp = member.getIsOp();
            return new Member(nickname, isOp);
        }

        private final String nickname;
        private final boolean isOp;

        public Member(final String encoded) {
            if(encoded.startsWith("@")) {
                nickname = encoded.substring(1);
                isOp = true;
            } else {
                nickname = encoded;
                isOp = false;
            }
        }

        public Member(final String nickname, final boolean isOp) {
            this.nickname = nickname;
            this.isOp = isOp;
        }

        public String getNickname() {
            return nickname;
        }

        public boolean isOp() {
            return isOp;
        }

        public IRC.RplNameReplyCommand.Member toProto() {
            return IRC.RplNameReplyCommand.Member.newBuilder()
                    .setNickname(nickname)
                    .setIsOp(isOp)
                    .build();
        }
    }

    private final String channel;
    private final List<Member> members;

    public RplNameReplyCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel, final List<Member> members) {
        super(rawLine, tags, source, IrcCommand.RPL_NAMREPLY, rawArgs, args);
        this.channel = channel;
        this.members = members;
    }

    public String getChannel() {
        return channel;
    }

    public Collection<Member> getMembers() {
        return members;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.RPL_NAMREPLY)
                .setExtension(IRC.RplNameReplyCommand.data, toProtoCommand());
    }

    private IRC.RplNameReplyCommand toProtoCommand() {
        return IRC.RplNameReplyCommand.newBuilder()
                .setChannel(channel)
                .addAllMembers(members.stream().map(Member::toProto).collect(Collectors.toList()))
                .build();
    }
}
