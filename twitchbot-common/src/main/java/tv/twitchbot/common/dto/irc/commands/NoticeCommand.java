package tv.twitchbot.common.dto.irc.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.lang.invoke.MethodHandles;
import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class NoticeCommand extends IrcStanza {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static NoticeCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.NoticeCommand noticeCommand) {
        final String channel = noticeCommand.getChannel();
        final String message = noticeCommand.getMessage();
        return new NoticeCommand(rawLine, tags, source, rawArgs, args, channel, message);
    }

    public enum MessageId {
        SUBS_ON, ALREADY_SUBS_ON, SUBS_OFF, ALREADY_SUBS_OFF,
        SLOW_ON, SLOW_OFF,
        R9K_ON, ALREADY_R9K_ON, R9K_OFF, ALREADY_R9K_OFF,
        HOST_ON, BAD_HOST_HOSTING, HOST_OFF, HOSTS_REMAINING,
        EMOTE_ONLY_ON, ALREADY_EMOTE_ONLY_ON, EMOTE_ONLY_OFF, ALREADY_EMOTE_ONLY_OFF,
        MSG_CHANNEL_SUSPENDED,
        TIMEOUT_SUCCESS, BAN_SUCCESS, UNBAN_SUCCESS, BAD_UNBAN_NO_BAN, ALREADY_BANNED,
        UNRECOGNIZED_COMMAND, MSG_DUPLICATE, MSG_RATELIMIT
    }

    private final String channel;
    private final String message;

    private MessageId messageId;

    public NoticeCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel, final String message) {
        super(rawLine, tags, source, IrcCommand.NOTICE, rawArgs, args);
        this.channel = channel;
        this.message = message;
        if(tags.containsKey("msg-id") && !tags.get("msg-id").isEmpty())
            try {
                messageId = MessageId.valueOf(tags.get("msg-id").toUpperCase());
            } catch(final IllegalArgumentException e) {
                LOG.warn("Unknown msg-id: {}", tags.get("msg-id"), e);
            }
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public MessageId getMessageId() {
        return messageId;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.NOTICE)
                .setExtension(IRC.NoticeCommand.data, toProtoCommand());
    }

    private IRC.NoticeCommand toProtoCommand() {
        final IRC.NoticeCommand.Builder builder = IRC.NoticeCommand.newBuilder()
                .setChannel(channel)
                .setMessage(message);
        if(messageId != null)
            builder.setMessageId(IRC.NoticeCommand.MessageId.valueOf(messageId.name()));
        return builder.build();
    }
}
