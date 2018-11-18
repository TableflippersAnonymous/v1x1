package tv.v1x1.modules.channel.spotify;

import org.codehaus.jackson.annotate.JsonProperty;
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
    @DisplayName("Spotify OAuth Token")
    @Description("Spotify OAuth Token")
    @Type(ConfigType.STRING)
    @TenantPermission(Permission.WRITE_ONLY)
    @JsonProperty("spotify_oauth")
    private String spotifyOAuth;

    public SpotifyUserConfiguration() {
    }

    public SpotifyUserConfiguration(final String spotifyOAuth) {
        this.spotifyOAuth = spotifyOAuth;
    }

    public String getSpotifyOAuth() {
        return spotifyOAuth;
    }

    public void setSpotifyOAuth(final String spotifyOAuth) {
        this.spotifyOAuth = spotifyOAuth;
    }
}
