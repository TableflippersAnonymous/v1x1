package tv.v1x1.common.scanners.permission;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import tv.v1x1.common.dto.db.Platform;

import java.util.Map;
import java.util.Set;

public enum DefaultGroup {
    OWNER("Owner", ImmutableSet.copyOf(Platform.values()), ImmutableMap.of()),
    WEB_ALL("Web All", ImmutableSet.copyOf(Platform.values()), ImmutableMap.of()),
    WEB_EDIT("Web Edit", ImmutableSet.copyOf(Platform.values()), ImmutableMap.of()),
    WEB_READ("Web Read", ImmutableSet.copyOf(Platform.values()), ImmutableMap.of()),

    /* Twitch */
    BROADCASTER("Broadcaster", ImmutableSet.of(Platform.TWITCH), ImmutableMap.of(Platform.TWITCH, ImmutableSet.of("BROADCASTER"))),
    MODS("Mods", ImmutableSet.of(Platform.TWITCH), ImmutableMap.of(Platform.TWITCH, ImmutableSet.of("MODERATOR", "ADMIN", "STAFF", "GLOBAL_MOD"))),
    SUBS("Subs", ImmutableSet.of(Platform.TWITCH), ImmutableMap.of(Platform.TWITCH, ImmutableSet.of("SUBSCRIBER"))),

    EVERYONE("Everyone", ImmutableSet.of(Platform.TWITCH, Platform.DISCORD), ImmutableMap.of(
            Platform.TWITCH, ImmutableSet.of("_DEFAULT_"),
            Platform.DISCORD, ImmutableSet.of("_DEFAULT_"))),
    ;

    private final String groupName;
    private final Map<Platform, Set<String>> platformMap;
    private final Set<Platform> platforms;

    DefaultGroup(final String groupName, final Set<Platform> platforms, final Map<Platform, Set<String>> platformMap) {
        this.groupName = groupName;
        this.platforms = platforms;
        this.platformMap = platformMap;
    }

    public String getGroupName() {
        return groupName;
    }

    public Map<Platform, Set<String>> getPlatformMap() {
        return platformMap;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }
}
