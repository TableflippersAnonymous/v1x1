package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimplePlaylist extends Context {
    @JsonProperty
    private boolean collaborative;
    @JsonProperty
    private String id;
    @JsonProperty
    private List<Image> images;
    @JsonProperty
    private String name;
    @JsonProperty
    private PublicUser owner;
    @JsonProperty("public")
    private boolean isPublic;
    @JsonProperty("snapshot_id")
    private String snapshotId;
    @JsonProperty
    private SimplePaging tracks;

    public SimplePlaylist() {
    }

    public SimplePlaylist(final String type, final String href, final Map<String, String> externalUrls,
                          final String uri, final boolean collaborative, final String id, final List<Image> images,
                          final String name, final PublicUser owner, final boolean isPublic, final String snapshotId,
                          final SimplePaging tracks) {
        super(type, href, externalUrls, uri);
        this.collaborative = collaborative;
        this.id = id;
        this.images = images;
        this.name = name;
        this.owner = owner;
        this.isPublic = isPublic;
        this.snapshotId = snapshotId;
        this.tracks = tracks;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(final boolean collaborative) {
        this.collaborative = collaborative;
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

    public PublicUser getOwner() {
        return owner;
    }

    public void setOwner(final PublicUser owner) {
        this.owner = owner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(final boolean aPublic) {
        isPublic = aPublic;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(final String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public SimplePaging getTracks() {
        return tracks;
    }

    public void setTracks(final SimplePaging tracks) {
        this.tracks = tracks;
    }
}
