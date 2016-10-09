package tv.twitchbot.modules.core.tmi.irc;

import tv.twitchbot.modules.core.tmi.irc.commands.PrivmsgCommand;

import java.util.Map;

/**
 * Created by naomi on 10/9/2016.
 */
public class TaggedIrcStanza extends IrcStanza {
    public enum UserType {
        MOD, GLOBAL_MOD, ADMIN, STAFF;
    }

    private String color;
    private String displayName;
    private boolean mod, subscriber, turbo;
    private UserType userType;

    public TaggedIrcStanza(String rawLine, Map<String, String> tags, IrcSource source, IrcCommand command, String rawArgs, String[] args) {
        super(rawLine, tags, source, command, rawArgs, args);
        if(tags.containsKey("color") && !tags.get("color").isEmpty())
            color = tags.get("color");
        if(tags.containsKey("display-name") && !tags.get("display-name").isEmpty())
            displayName = tags.get("display-name");
        if(tags.containsKey("mod") && !tags.get("mod").isEmpty())
            mod = tags.get("mod").equals("1");
        if(tags.containsKey("subscriber") && !tags.get("subscriber").isEmpty())
            subscriber = tags.get("subscriber").equals("1");
        if(tags.containsKey("turbo") && !tags.get("turbo").isEmpty())
            turbo = tags.get("turbo").equals("1");
        if(tags.containsKey("user-type") && !tags.get("user-type").isEmpty())
            userType = UserType.valueOf(tags.get("user-type").toUpperCase());
    }

    public String getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isMod() {
        return mod;
    }

    public boolean isSubscriber() {
        return subscriber;
    }

    public boolean isTurbo() {
        return turbo;
    }

    public UserType getUserType() {
        return userType;
    }
}
