package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.UserOuterClass;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/5/16.
 */
public class TwitchUser extends User {
    public static TwitchUser fromProto(UserOuterClass.User user) {
        String name = user.getName();
        Set<Permission> permissions = user.getPermissionsList().stream().map(Permission::fromProto).collect(Collectors.toSet());
        return new TwitchUser(name, permissions);
    }

    public TwitchUser(String name, Set<Permission> permissions) {
        super(name, permissions);
    }

    @Override
    public UserOuterClass.User toProto() {
        return UserOuterClass.User.newBuilder()
                .setName(getName())
                .setType(UserOuterClass.User.UserType.TWITCH)
                .addAllPermissions(getPermissions().stream().map(Permission::toProto).collect(Collectors.toSet()))
                .build();
    }
}
