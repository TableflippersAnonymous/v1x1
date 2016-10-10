package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.EmoteSetIrcStanza;
import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class UserStateCommand extends EmoteSetIrcStanza {
    public static UserStateCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.UserStateCommand userStateCommand) {
        String channel = userStateCommand.getChannel();
        return new UserStateCommand(rawLine, tags, source, rawArgs, args, channel);
    }

    private String channel;

    public UserStateCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel) {
        super(rawLine, tags, source, IrcCommand.USERSTATE, rawArgs, args);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.USERSTATE)
                .setExtension(IRC.UserStateCommand.data, toProtoCommand());
    }

    private IRC.UserStateCommand toProtoCommand() {
        return IRC.UserStateCommand.newBuilder()
                .setChannel(channel)
                .setEmoteSetStanza(toProtoEmoteSet())
                .build();
    }
}
