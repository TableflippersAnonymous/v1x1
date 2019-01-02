package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicUser extends Context {
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty
    private Followers followers;
    @JsonProperty
    private String id;
    @JsonProperty
    private List<Image> images;

    public PublicUser() {
    }

    public PublicUser(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                      final String displayName, final Followers followers, final String id, final List<Image> images) {
        super(type, href, externalUrls, uri);
        this.displayName = displayName;
        this.followers = followers;
        this.id = id;
        this.images = images;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(final Followers followers) {
        this.followers = followers;
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
}
