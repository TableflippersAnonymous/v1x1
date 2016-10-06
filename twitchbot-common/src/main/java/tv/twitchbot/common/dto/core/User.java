package tv.twitchbot.common.dto.core;

import com.google.common.collect.ImmutableSet;
import tv.twitchbot.common.dto.proto.core.UserOuterClass;

import java.util.Set;

/**
 * Created by cobi on 10/5/16.
 */
public abstract class User {
    public static User fromProto(UserOuterClass.User user) {
        switch(user.getType()) {
            case TWITCH: return TwitchUser.fromProto(user);
            case DISCORD: return DiscordUser.fromProto(user);
            default: throw new IllegalStateException("Unknown user type " + user.getType().name());
        }
    }

    private String name;
    private Set<Permission> permissions;

    protected User(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public Set<Permission> getPermissions() {
        return ImmutableSet.copyOf(permissions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public abstract UserOuterClass.User toProto();
}
