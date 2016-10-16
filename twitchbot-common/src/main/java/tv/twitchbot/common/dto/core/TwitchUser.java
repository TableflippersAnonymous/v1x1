package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;
import tv.twitchbot.common.dto.proto.core.UserOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class TwitchUser extends User {
    public static TwitchUser fromProto(UserOuterClass.User user) {
        String id = user.getId();
        GlobalUser globalUser = GlobalUser.fromProto(user.getGlobalUser());
        String displayName = user.getDisplayName();
        return new TwitchUser(id, globalUser, displayName);
    }

    public static TwitchUser fromProto(GlobalUser globalUser, UserOuterClass.GlobalUserEntry globalUserEntry) {
        String id = globalUserEntry.getId();
        String displayName = globalUserEntry.getDisplayName();
        return new TwitchUser(id, globalUser, displayName);
    }

    public TwitchUser(String id, GlobalUser globalUser, String displayName) {
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
