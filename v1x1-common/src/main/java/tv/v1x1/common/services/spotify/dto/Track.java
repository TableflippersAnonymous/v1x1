package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track extends SimpleTrack {
    @JsonProperty
    private SimpleAlbum album;
    @JsonProperty("external_ids")
    private Map<String, String> externalIds;
    @JsonProperty
    private long popularity;

    public Track() {
    }

    public Track(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                 final String id, final List<SimpleArtist> artists, final List<String> availableMarkets,
                 final long discNumber, final long durationMs, final boolean explicit, final boolean isPlayable,
                 final TrackLink linkedFrom, final Restrictions restrictions, final String name,
                 final String previewUrl, final long trackNumber, final boolean isLocal, final SimpleAlbum album,
                 final Map<String, String> externalIds, final long popularity) {
        super(type, href, externalUrls, uri, id, artists, availableMarkets, discNumber, durationMs, explicit,
                isPlayable, linkedFrom, restrictions, name, previewUrl, trackNumber, isLocal);
        this.album = album;
        this.externalIds = externalIds;
        this.popularity = popularity;
    }

    public SimpleAlbum getAlbum() {
        return album;
    }

    public void setAlbum(final SimpleAlbum album) {
        this.album = album;
    }

    public Map<String, String> getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(final Map<String, String> externalIds) {
        this.externalIds = externalIds;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(final long popularity) {
        this.popularity = popularity;
    }
}
