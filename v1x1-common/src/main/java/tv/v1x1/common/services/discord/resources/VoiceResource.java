package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.voice.VoiceRegion;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

/**
 * Created by naomi on 9/17/2017.
 */
public class VoiceResource {
    private final WebTarget voice;

    public VoiceResource(final WebTarget voice) {
        this.voice = voice;
    }

    public List<VoiceRegion> listVoiceRegions() {
        return voice.path("regions")
                .request(DiscordApi.ACCEPT)
                .get()
                .readEntity(new GenericType<List<VoiceRegion>>() {});
    }
}
