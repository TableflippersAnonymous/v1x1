package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist extends SimpleArtist {
    @JsonProperty
    private Followers followers;
    @JsonProperty
    private List<String> genres;
    @JsonProperty
    private List<Image> images;
    @JsonProperty
    private int popularity;

    public Artist() {
    }

    public Artist(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                  final String id, final String name, final Followers followers, final List<String> genres,
                  final List<Image> images, final int popularity) {
        super(type, href, externalUrls, uri, id, name);
        this.followers = followers;
        this.genres = genres;
        this.images = images;
        this.popularity = popularity;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(final Followers followers) {
        this.followers = followers;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(final List<String> genres) {
        this.genres = genres;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(final List<Image> images) {
        this.images = images;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(final int popularity) {
        this.popularity = popularity;
    }
}
