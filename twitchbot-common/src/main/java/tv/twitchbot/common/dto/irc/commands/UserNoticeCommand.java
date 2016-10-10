package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.MessageTaggedIrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class UserNoticeCommand extends MessageTaggedIrcStanza {
    public static UserNoticeCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.UserNoticeCommand userNoticeCommand) {
        String channel = userNoticeCommand.getChannel();
        String message = userNoticeCommand.getMessage();
        return new UserNoticeCommand(rawLine, tags, source, rawArgs, args, channel, message);
    }

    public enum MessageId {
        RESUB;

        public IRC.UserNoticeCommand.MessageId toProto() {
            switch(this) {
                case RESUB: return IRC.UserNoticeCommand.MessageId.RESUB;
                default: throw new IllegalStateException("Unknown MessageId: " + this);
            }
        }
    }

    private String channel;
    private String message;

    private MessageId messageId;
    private int months;
    private String systemMessage;
    private String login;

    public UserNoticeCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel, String message) {
        super(rawLine, tags, source, IrcCommand.USERNOTICE, rawArgs, args);
        this.channel = channel;
        this.message = message;
        if(tags.containsKey("msg-id") && !tags.get("msg-id").isEmpty())
            messageId = MessageId.valueOf(tags.get("msg-id").toUpperCase());
        if(tags.containsKey("msg-param-months") && !tags.get("msg-param-months").isEmpty())
            months = Integer.valueOf(tags.get("msg-param-months"));
        if(tags.containsKey("system-msg") && !tags.get("system-msg").isEmpty())
            systemMessage = tags.get("system-msg");
        if(tags.containsKey("login") && !tags.get("login").isEmpty())
            login = tags.get("login");
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

    public int getMonths() {
        return months;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public String getLogin() {
        return login;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.USERNOTICE)
                .setExtension(IRC.UserNoticeCommand.data, toProtoCommand());
    }

    private IRC.UserNoticeCommand toProtoCommand() {
        IRC.UserNoticeCommand.Builder builder = IRC.UserNoticeCommand.newBuilder()
                .setChannel(channel)
                .setMessage(message);
        if(messageId != null)
            builder.setMessageId(messageId.toProto());
        builder.setMonths(months);
        if(systemMessage != null)
            builder.setSystemMessage(systemMessage);
        if(login != null)
            builder.setLogin(login);
        builder.setMessageTaggedStanza(toProtoMessageTagged());
        return builder.build();
    }
}
