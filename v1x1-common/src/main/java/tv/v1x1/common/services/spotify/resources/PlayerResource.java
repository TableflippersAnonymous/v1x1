package tv.v1x1.common.services.spotify.resources;

import tv.v1x1.common.services.spotify.dto.Devices;
import tv.v1x1.common.services.twitch.TwitchApi;

import javax.ws.rs.client.WebTarget;

public class PlayerResource {
    private final WebTarget player;

    public PlayerResource(final WebTarget player) {
        this.player = player;
    }

    public Devices getDevices() {
        return player.path("devices")
                .request(TwitchApi.ACCEPT)
                .get(Devices.class);
    }
}
