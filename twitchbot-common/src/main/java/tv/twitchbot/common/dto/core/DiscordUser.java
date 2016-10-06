package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.UserOuterClass;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/5/16.
 */
public class DiscordUser extends User {
    public static DiscordUser fromProto(UserOuterClass.User user) {
        String name = user.getName();
        Set<Permission> permissions = user.getPermissionsList().stream().map(Permission::fromProto).collect(Collectors.toSet());
        String mention = user.getMention();
        return new DiscordUser(name, permissions, mention);
    }

    private String mention;

    public DiscordUser(String name, Set<Permission> permissions, String mention) {
        super(name, permissions);
        this.mention = mention;
    }

    public String getMention() {
        return mention;
    }

    @Override
    public UserOuterClass.User toProto() {
        return UserOuterClass.User.newBuilder()
                .setName(getName())
                .setType(UserOuterClass.User.UserType.DISCORD)
                .setMention(mention)
                .addAllPermissions(getPermissions().stream().map(Permission::toProto).collect(Collectors.toSet()))
                .build();
    }
}
