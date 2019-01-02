package tv.v1x1.modules.channel.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.modules.BasicUserConfiguration;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.ModuleConfig;
import tv.v1x1.common.scanners.config.Permission;
import tv.v1x1.common.scanners.config.TenantPermission;
import tv.v1x1.common.scanners.config.Type;
import tv.v1x1.common.scanners.config.Version;

@ModuleConfig("spotify")
@DisplayName("Spotify Song Requests")
@Description("Allow users to request songs via Spotify")
@TenantPermission(Permission.READ_WRITE)
@Version(0)
public class SpotifyUserConfiguration extends BasicUserConfiguration {
    @DisplayName("Spotify Account")
    @Description("SPOTIFY")
    @Type(ConfigType.OAUTH)
    @TenantPermission(Permission.WRITE_ONLY)
    @JsonProperty("refresh_token")
    private String refreshToken;

    @DisplayName("Default Playlist")
    @Description("This is the Spotify URI of the default playlist to play when no songs are queued")
    @Type(ConfigType.STRING)
    @TenantPermission(Permission.READ_WRITE)
    @JsonProperty("default_playlist")
    private String defaultPlaylist;

    @DisplayName("Always On")
    @Description("If this is enabled, play queue will not stop after going offline")
    @Type(ConfigType.BOOLEAN)
    @TenantPermission(Permission.READ_WRITE)
    @JsonProperty("always_on")
    private boolean alwaysOn;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getDefaultPlaylist() {
        return defaultPlaylist;
    }

    public void setDefaultPlaylist(final String defaultPlaylist) {
        this.defaultPlaylist = defaultPlaylist;
    }

    public boolean isAlwaysOn() {
        return alwaysOn;
    }

    public void setAlwaysOn(final boolean alwaysOn) {
        this.alwaysOn = alwaysOn;
    }
}
