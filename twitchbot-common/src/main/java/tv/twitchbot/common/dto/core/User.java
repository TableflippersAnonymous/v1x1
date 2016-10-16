package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.UserOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public abstract class User {
    public static User fromProto(UserOuterClass.User user) {
        switch(user.getPlatform()) {
            case TWITCH: return TwitchUser.fromProto(user);
            case DISCORD: return DiscordUser.fromProto(user);
            default: throw new IllegalStateException("Unknown user platform " + user.getPlatform().name());
        }
    }

    public static User fromProto(GlobalUser globalUser, UserOuterClass.GlobalUserEntry globalUserEntry) {
        switch(globalUserEntry.getPlatform()) {
            case TWITCH: return TwitchUser.fromProto(globalUser, globalUserEntry);
            case DISCORD: return DiscordUser.fromProto(globalUser, globalUserEntry);
            default: throw new IllegalStateException("Unknown user platform " + globalUserEntry.getPlatform().name());
        }
    }


    private String id;
    private GlobalUser globalUser;
    private String displayName;

    public User(String id, GlobalUser globalUser, String displayName) {
        this.id = id;
        this.globalUser = globalUser;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public GlobalUser getGlobalUser() {
        return globalUser;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public abstract UserOuterClass.User toProto();
    public abstract UserOuterClass.GlobalUserEntry toProtoGlobalUserEntry();
    public abstract tv.twitchbot.common.dto.db.GlobalUser.Entry toDBGlobalUser();
}
