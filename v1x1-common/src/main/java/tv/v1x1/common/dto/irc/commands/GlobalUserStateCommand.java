package tv.v1x1.common.dto.irc.commands;

import tv.v1x1.common.dto.irc.EmoteSetIrcStanza;
import tv.v1x1.common.dto.irc.IrcSource;
import tv.v1x1.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class GlobalUserStateCommand extends EmoteSetIrcStanza {
    public static GlobalUserStateCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.GlobalUserStateCommand globalUserStateCommand) {
        return new GlobalUserStateCommand(rawLine, tags, source, rawArgs, args);
    }

    private int userId;

    public GlobalUserStateCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args) {
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
