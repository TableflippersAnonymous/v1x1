package tv.v1x1.common.dto.irc.commands;

import tv.v1x1.common.dto.irc.IrcSource;
import tv.v1x1.common.dto.irc.MessageTaggedIrcStanza;
import tv.v1x1.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class UserNoticeCommand extends MessageTaggedIrcStanza {
    public static UserNoticeCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.UserNoticeCommand userNoticeCommand) {
        final String channel = userNoticeCommand.getChannel();
        final String message = userNoticeCommand.getMessage();
        return new UserNoticeCommand(rawLine, tags, source, rawArgs, args, channel, message);
    }

    public enum MessageId {
        SUB, RESUB, CHARITY;

        public IRC.UserNoticeCommand.MessageId toProto() {
            switch(this) {
                case RESUB: return IRC.UserNoticeCommand.MessageId.RESUB;
                case SUB: return IRC.UserNoticeCommand.MessageId.SUB;
                case CHARITY: return IRC.UserNoticeCommand.MessageId.CHARITY;
                default: throw new IllegalStateException("Unknown MessageId: " + this);
            }
        }
    }

    private final String channel;
    private final String message;

    private MessageId messageId;
    private int months;
    private String systemMessage;
    private String login;
    private String planLevel;
    private String planName;

    public UserNoticeCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String channel, final String message) {
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
        if(tags.containsKey("msg-param-sub-plan") && !tags.get("msg-param-sub-plan").isEmpty())
            planLevel = tags.get("msg-param-sub-plan");
        if(tags.containsKey("msg-param-sub-plan-name") && !tags.get("msg-param-sub-plan-name").isEmpty())
            planName = tags.get("msg-param-sub-plan-name");
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

    public String getPlanLevel() {
        return planLevel;
    }

    public String getPlanName() {
        return planName;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.USERNOTICE)
                .setExtension(IRC.UserNoticeCommand.data, toProtoCommand());
    }

    private IRC.UserNoticeCommand toProtoCommand() {
        final IRC.UserNoticeCommand.Builder builder = IRC.UserNoticeCommand.newBuilder()
                .setChannel(channel)
                .setMessage(message);
        if(messageId != null)
            builder.setMessageId(messageId.toProto());
        builder.setMonths(months);
        if(systemMessage != null)
            builder.setSystemMessage(systemMessage);
        if(login != null)
            builder.setLogin(login);
        if(planLevel != null)
            builder.setPlanLevel(planLevel);
        if(planName != null)
            builder.setPlanName(planName);
        builder.setMessageTaggedStanza(toProtoMessageTagged());
        return builder.build();
    }
}
