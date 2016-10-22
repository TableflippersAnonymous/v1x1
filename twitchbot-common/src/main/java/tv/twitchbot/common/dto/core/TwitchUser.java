package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;
import tv.twitchbot.common.dto.proto.core.UserOuterClass;

/**
 * Represents a {@link User} on the Twitch platform
 * @author Naomi
 */
public class TwitchUser extends User {
    public static TwitchUser fromProto(final UserOuterClass.User user) {
        final String id = user.getId();
        final GlobalUser globalUser = GlobalUser.fromProto(user.getGlobalUser());
        final String displayName = user.getDisplayName();
        return new TwitchUser(id, globalUser, displayName);
    }

    public static TwitchUser fromProto(final GlobalUser globalUser, final UserOuterClass.GlobalUserEntry globalUserEntry) {
        final String id = globalUserEntry.getId();
        final String displayName = globalUserEntry.getDisplayName();
        return new TwitchUser(id, globalUser, displayName);
    }

    public TwitchUser(final String id, final GlobalUser globalUser, final String displayName) {
        super(id, globalUser, displayName);
    }

    @Override
    public UserOuterClass.User toProto() {
        return UserOuterClass.User.newBuilder()
                .setPlatform(PlatformOuterClass.Platform.TWITCH)
                .setId(getId())
                .setDisplayName(getDisplayName())
                .setGlobalUser(getGlobalUser().toProto())
                .build();
    }

    @Override
    public UserOuterClass.GlobalUserEntry toProtoGlobalUserEntry() {
        return UserOuterClass.GlobalUserEntry.newBuilder()
                .setPlatform(PlatformOuterClass.Platform.TWITCH)
                .setId(getId())
                .setDisplayName(getDisplayName())
                .build();
    }

    @Override
    public tv.twitchbot.common.dto.db.GlobalUser.Entry toDBGlobalUser() {
        return new tv.twitchbot.common.dto.db.GlobalUser.Entry(Platform.TWITCH, getDisplayName(), getId());
    }
}
