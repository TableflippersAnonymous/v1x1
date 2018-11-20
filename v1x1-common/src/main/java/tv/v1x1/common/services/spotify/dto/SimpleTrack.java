package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleTrack extends TrackLink {
    @JsonProperty
    private List<SimpleArtist> artists;
    @JsonProperty("available_markets")
    private List<String> availableMarkets;
    @JsonProperty("disc_number")
    private long discNumber;
    @JsonProperty("duration_ms")
    private long durationMs;
    @JsonProperty
    private boolean explicit;
    @JsonProperty("is_playable")
    private boolean isPlayable;
    @JsonProperty("linked_from")
    private TrackLink linkedFrom;
    @JsonProperty
    private Restrictions restrictions;
    @JsonProperty
    private String name;
    @JsonProperty("preview_url")
    private String previewUrl;
    @JsonProperty("track_number")
    private long trackNumber;
    @JsonProperty("is_local")
    private boolean isLocal;

    public SimpleTrack() {
    }

    public SimpleTrack(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                       final String id, final List<SimpleArtist> artists, final List<String> availableMarkets,
                       final long discNumber, final long durationMs, final boolean explicit, final boolean isPlayable,
                       final TrackLink linkedFrom, final Restrictions restrictions, final String name,
                       final String previewUrl, final long trackNumber, final boolean isLocal) {
        super(type, href, externalUrls, uri, id);
        this.artists = artists;
        this.availableMarkets = availableMarkets;
        this.discNumber = discNumber;
        this.durationMs = durationMs;
        this.explicit = explicit;
        this.isPlayable = isPlayable;
        this.linkedFrom = linkedFrom;
        this.restrictions = restrictions;
        this.name = name;
        this.previewUrl = previewUrl;
        this.trackNumber = trackNumber;
        this.isLocal = isLocal;
    }

    public List<SimpleArtist> getArtists() {
        return artists;
    }

    public void setArtists(final List<SimpleArtist> artists) {
        this.artists = artists;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public void setAvailableMarkets(final List<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public long getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(final long discNumber) {
        this.discNumber = discNumber;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(final long durationMs) {
        this.durationMs = durationMs;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(final boolean explicit) {
        this.explicit = explicit;
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public void setPlayable(final boolean playable) {
        isPlayable = playable;
    }

    public TrackLink getLinkedFrom() {
        return linkedFrom;
    }

    public void setLinkedFrom(final TrackLink linkedFrom) {
        this.linkedFrom = linkedFrom;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(final Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(final String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public long getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(final long trackNumber) {
        this.trackNumber = trackNumber;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(final boolean local) {
        isLocal = local;
    }
}
