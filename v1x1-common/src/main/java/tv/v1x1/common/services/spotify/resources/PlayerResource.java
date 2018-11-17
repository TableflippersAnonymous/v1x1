package tv.v1x1.common.services.spotify.resources;

import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.CurrentlyPlayingContext;
import tv.v1x1.common.services.spotify.dto.CursorPaging;
import tv.v1x1.common.services.spotify.dto.Devices;
import tv.v1x1.common.services.spotify.dto.PlayHistory;
import tv.v1x1.common.services.spotify.dto.PlayRequest;
import tv.v1x1.common.services.spotify.dto.TransferRequest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

public class PlayerResource {
    private final WebTarget player;

    public PlayerResource(final WebTarget player) {
        this.player = player;
    }

    public Devices getDevices() {
        return player.path("devices")
                .request(SpotifyApi.ACCEPT)
                .get(Devices.class);
    }

    public CurrentlyPlayingContext getCurrentlyPlayingContext(final Optional<String> market) {
        return player
                .queryParam("market", market.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(CurrentlyPlayingContext.class);
    }

    public CursorPaging<PlayHistory> getRecentlyPlayed(final Optional<Integer> limit, final Optional<Long> after,
                                                       final Optional<Long> before) {
        return player.path("recently-played")
                .queryParam("limit", limit.orElse(null))
                .queryParam("after", after.orElse(null))
                .queryParam("before", before.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(new GenericType<CursorPaging<PlayHistory>>() {});
    }

    public void pause(final Optional<String> deviceId) {
        player.path("pause")
                .queryParam("device_id", deviceId.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .put(null);
    }

    public void seek(final int positionMs, final Optional<String> deviceId) {
        player.path("seek")
                .queryParam("position_ms", positionMs)
                .queryParam("device_id", deviceId.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .put(null);
    }

    public void repeat(final String state, final Optional<String> deviceId) {
        player.path("repeat")
                .queryParam("state", state)
                .queryParam("device_id", deviceId.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .put(null);
    }

    public void volume(final int volumePercent, final Optional<String> deviceId) {
        player.path("volume")
                .queryParam("volume_percent", volumePercent)
                .queryParam("device_id", deviceId.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .put(null);
    }

    public void next(final Optional<String> deviceId) {
        player.path("next")
                .queryParam("device_id", deviceId.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .post(null);
    }

    public void previous(final Optional<String> deviceId) {
        player.path("previous")
                .queryParam("device_id", deviceId.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .post(null);
    }

    public void play(final Optional<String> deviceId, final PlayRequest playRequest) {
        player.path("play")
                .queryParam("device_id", deviceId.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .put(Entity.entity(playRequest, MediaType.APPLICATION_JSON));
    }

    public void shuffle(final boolean state, final Optional<String> deviceId) {
        player.path("shuffle")
                .queryParam("state", state)
                .queryParam("device_id", deviceId.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .put(null);
    }

    public void transfer(final TransferRequest transferRequest) {
        player.request(SpotifyApi.ACCEPT)
                .put(Entity.entity(transferRequest, MediaType.APPLICATION_JSON));
    }
}
