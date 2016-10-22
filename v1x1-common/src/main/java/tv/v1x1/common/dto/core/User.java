package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.proto.core.UserOuterClass;

/**
 * Represents a single name on a single platform
 * @author Cobi
 */
public abstract class User {
    public static User fromProto(final UserOuterClass.User user) {
        switch(user.getPlatform()) {
            case TWITCH: return TwitchUser.fromProto(user);
            case DISCORD: return DiscordUser.fromProto(user);
            default: throw new IllegalStateException("Unknown user platform " + user.getPlatform().name());
        }
    }

    public static User fromProto(final GlobalUser globalUser, final UserOuterClass.GlobalUserEntry globalUserEntry) {
        switch(globalUserEntry.getPlatform()) {
            case TWITCH: return TwitchUser.fromProto(globalUser, globalUserEntry);
            case DISCORD: return DiscordUser.fromProto(globalUser, globalUserEntry);
            default: throw new IllegalStateException("Unknown user platform " + globalUserEntry.getPlatform().name());
        }
    }


    private final String id;
    private final GlobalUser globalUser;
    private final String displayName;

    public User(final String id, final GlobalUser globalUser, final String displayName) {
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public abstract UserOuterClass.User toProto();
    public abstract UserOuterClass.GlobalUserEntry toProtoGlobalUserEntry();
    public abstract tv.v1x1.common.dto.db.GlobalUser.Entry toDBGlobalUser();
}
