package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;
import tv.twitchbot.common.dto.proto.core.UserOuterClass;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a {@link User} on Discord
 * @author Naomi
 */
public class DiscordUser extends User {
    public static DiscordUser fromProto(UserOuterClass.User user) {
        String id = user.getId();
        GlobalUser globalUser = GlobalUser.fromProto(user.getGlobalUser());
        String displayName = user.getDisplayName();
        return new DiscordUser(id, globalUser, displayName);
    }

    public static DiscordUser fromProto(GlobalUser globalUser, UserOuterClass.GlobalUserEntry globalUserEntry) {
        String id = globalUserEntry.getId();
        String displayName = globalUserEntry.getDisplayName();
        return new DiscordUser(id, globalUser, displayName);
    }

    public DiscordUser(String id, GlobalUser globalUser, String displayName) {
        super(id, globalUser, displayName);
    }

    @Override
    public UserOuterClass.User toProto() {
        return UserOuterClass.User.newBuilder()
                .setPlatform(PlatformOuterClass.Platform.DISCORD)
                .setId(getId())
                .setDisplayName(getDisplayName())
                .setGlobalUser(getGlobalUser().toProto())
                .build();
    }

    @Override
    public UserOuterClass.GlobalUserEntry toProtoGlobalUserEntry() {
        return UserOuterClass.GlobalUserEntry.newBuilder()
                .setPlatform(PlatformOuterClass.Platform.DISCORD)
                .setId(getId())
                .setDisplayName(getDisplayName())
                .build();
    }

    @Override
    public tv.twitchbot.common.dto.db.GlobalUser.Entry toDBGlobalUser() {
        return new tv.twitchbot.common.dto.db.GlobalUser.Entry(Platform.DISCORD, getDisplayName(), getId());
    }
}
