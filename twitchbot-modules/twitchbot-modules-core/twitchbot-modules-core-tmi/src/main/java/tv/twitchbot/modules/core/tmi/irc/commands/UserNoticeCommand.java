package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.MessageTaggedIrcStanza;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class UserNoticeCommand extends MessageTaggedIrcStanza {
    public enum MessageId {
        RESUB;
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
}
