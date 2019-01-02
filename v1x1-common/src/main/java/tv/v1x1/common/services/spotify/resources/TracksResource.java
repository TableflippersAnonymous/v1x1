package tv.v1x1.common.services.spotify.resources;

import com.google.common.base.Joiner;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.AudioAnalysis;
import tv.v1x1.common.services.spotify.dto.AudioFeatures;
import tv.v1x1.common.services.spotify.dto.Track;
import tv.v1x1.common.services.spotify.dto.Tracks;

import javax.ws.rs.client.WebTarget;
import java.util.List;
import java.util.Optional;

public class TracksResource {
    private final WebTarget tracks;
    private final WebTarget audioFeatures;
    private final WebTarget audioAnalysis;

    public TracksResource(final WebTarget tracks, final WebTarget audioFeatures, final WebTarget audioAnalysis) {
        this.tracks = tracks;
        this.audioFeatures = audioFeatures;
        this.audioAnalysis = audioAnalysis;
    }

    public AudioAnalysis getAudioAnalysis(final String id) {
        return audioAnalysis.path(id)
                .request(SpotifyApi.ACCEPT)
                .get(AudioAnalysis.class);
    }

    public AudioFeatures getAudioFeatures(final String id) {
        return audioFeatures.path(id)
                .request(SpotifyApi.ACCEPT)
                .get(AudioFeatures.class);
    }

    public Tracks getTracks(final List<String> ids, final Optional<String> market) {
        return tracks
                .queryParam("ids", Joiner.on(",").join(ids))
                .queryParam("market", market.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Tracks.class);
    }

    public Track getTrack(final String id, final Optional<String> market) {
        return tracks.path(id)
                .queryParam("market", market.orElse(null))
                .request(SpotifyApi.ACCEPT)
                .get(Track.class);
    }
}
