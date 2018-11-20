package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Album extends SimpleAlbum {
    @JsonProperty
    private List<Copyright> copyrights;
    @JsonProperty("external_ids")
    private Map<String, String> externalIds;
    @JsonProperty
    private List<String> genres;
    @JsonProperty
    private String label;
    @JsonProperty
    private long popularity;
    @JsonProperty
    private Paging<SimpleTrack> tracks;

    public Album() {
    }

    public Album(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                 final String albumGroup, final String albumType, final List<SimpleArtist> artists,
                 final List<String> availableMarkets, final String id, final List<Image> images, final String name,
                 final String releaseDate, final String releaseDatePrecision, final Restrictions restrictions,
                 final List<Copyright> copyrights, final Map<String, String> externalIds, final List<String> genres,
                 final String label, final long popularity, final Paging<SimpleTrack> tracks) {
        super(type, href, externalUrls, uri, albumGroup, albumType, artists, availableMarkets, id, images, name,
                releaseDate, releaseDatePrecision, restrictions);
        this.copyrights = copyrights;
        this.externalIds = externalIds;
        this.genres = genres;
        this.label = label;
        this.popularity = popularity;
        this.tracks = tracks;
    }

    public List<Copyright> getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(final List<Copyright> copyrights) {
        this.copyrights = copyrights;
    }

    public Map<String, String> getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(final Map<String, String> externalIds) {
        this.externalIds = externalIds;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(final List<String> genres) {
        this.genres = genres;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(final long popularity) {
        this.popularity = popularity;
    }

    public Paging<SimpleTrack> getTracks() {
        return tracks;
    }

    public void setTracks(final Paging<SimpleTrack> tracks) {
        this.tracks = tracks;
    }
}
