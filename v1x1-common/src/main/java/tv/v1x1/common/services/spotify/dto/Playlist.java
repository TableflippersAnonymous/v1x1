package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlist extends SimplePlaylist {
    @JsonProperty
    private String description;
    @JsonProperty
    private Followers followers;
    @JsonProperty
    private Paging<PlaylistTrack> tracks;

    public Playlist() {
    }

    public Playlist(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                    final boolean collaborative, final String id, final List<Image> images, final String name,
                    final PublicUser owner, final boolean isPublic, final String snapshotId,
                    final Paging<PlaylistTrack> tracks, final String description, final Followers followers) {
        super(type, href, externalUrls, uri, collaborative, id, images, name, owner, isPublic, snapshotId, tracks);
        this.description = description;
        this.followers = followers;
        this.tracks = tracks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(final Followers followers) {
        this.followers = followers;
    }

    @Override
    public Paging<PlaylistTrack> getTracks() {
        return tracks;
    }

    public void setTracks(final Paging<PlaylistTrack> tracks) {
        super.setTracks(tracks);
        this.tracks = tracks;
    }
}
