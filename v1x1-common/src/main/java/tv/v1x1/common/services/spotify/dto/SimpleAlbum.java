package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleAlbum extends Context {
    @JsonProperty("album_group")
    private String albumGroup;
    @JsonIgnoreProperties("album_type")
    private String albumType;
    @JsonIgnoreProperties
    private List<SimpleArtist> artists;
    @JsonIgnoreProperties("available_markets")
    private List<String> availableMarkets;
    @JsonProperty
    private String id;
    @JsonProperty
    private List<Image> images;
    @JsonProperty
    private String name;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("release_date_precision")
    private String releaseDatePrecision;
    @JsonProperty
    private Restrictions restrictions;

    public SimpleAlbum() {
    }

    public SimpleAlbum(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                       final String albumGroup, final String albumType, final List<SimpleArtist> artists,
                       final List<String> availableMarkets, final String id, final List<Image> images,
                       final String name, final String releaseDate, final String releaseDatePrecision,
                       final Restrictions restrictions) {
        super(type, href, externalUrls, uri);
        this.albumGroup = albumGroup;
        this.albumType = albumType;
        this.artists = artists;
        this.availableMarkets = availableMarkets;
        this.id = id;
        this.images = images;
        this.name = name;
        this.releaseDate = releaseDate;
        this.releaseDatePrecision = releaseDatePrecision;
        this.restrictions = restrictions;
    }

    public String getAlbumGroup() {
        return albumGroup;
    }

    public void setAlbumGroup(final String albumGroup) {
        this.albumGroup = albumGroup;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(final String albumType) {
        this.albumType = albumType;
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

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(final List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(final String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDatePrecision() {
        return releaseDatePrecision;
    }

    public void setReleaseDatePrecision(final String releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(final Restrictions restrictions) {
        this.restrictions = restrictions;
    }
}
