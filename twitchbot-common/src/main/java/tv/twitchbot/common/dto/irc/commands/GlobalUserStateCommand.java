package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.EmoteSetIrcStanza;
import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class GlobalUserStateCommand extends EmoteSetIrcStanza {
    public static GlobalUserStateCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.GlobalUserStateCommand globalUserStateCommand) {
        return new GlobalUserStateCommand(rawLine, tags, source, rawArgs, args);
    }

    private int userId;

    public GlobalUserStateCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args) {
        super(rawLine, tags, source, IrcCommand.GLOBALUSERSTATE, rawArgs, args);
        if(tags.containsKey("user-id") && !tags.get("user-id").isEmpty())
            userId = Integer.valueOf(tags.get("user-id"));
    }

    public int getUserId() {
        return userId;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.GLOBALUSERSTATE)
                .setExtension(IRC.GlobalUserStateCommand.data, toProtoCommand());
    }

    private IRC.GlobalUserStateCommand toProtoCommand() {
        return IRC.GlobalUserStateCommand.newBuilder()
                .setUserId(userId)
                .setEmoteSetStanza(toProtoEmoteSet())
                .build();
    }
}
