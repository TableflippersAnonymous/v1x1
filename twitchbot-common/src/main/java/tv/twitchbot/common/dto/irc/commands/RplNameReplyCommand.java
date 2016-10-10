package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/8/2016.
 */
public class RplNameReplyCommand extends IrcStanza {
    public static RplNameReplyCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.RplNameReplyCommand rplNameReplyCommand) {
        String channel = rplNameReplyCommand.getChannel();
        List<Member> members = rplNameReplyCommand.getMembersList().stream().map(Member::fromProto).collect(Collectors.toList());
        return new RplNameReplyCommand(rawLine, tags, source, rawArgs, args, channel, members);
    }

    public static class Member {
        public static Member fromProto(IRC.RplNameReplyCommand.Member member) {
            String nickname = member.getNickname();
            boolean isOp = member.getIsOp();
            return new Member(nickname, isOp);
        }

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

        public IRC.RplNameReplyCommand.Member toProto() {
            return IRC.RplNameReplyCommand.Member.newBuilder()
                    .setNickname(nickname)
                    .setIsOp(isOp)
                    .build();
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
