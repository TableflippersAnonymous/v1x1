package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityBannedUser {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty
    private String name;
    @JsonProperty
    private String bio;
    @JsonProperty("avatar_image_url")
    private String avatarImageUrl;
    @JsonProperty("start_timestamp")
    private long startTimestamp;

    public CommunityBannedUser() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(final String bio) {
        this.bio = bio;
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public void setAvatarImageUrl(final String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(final long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }
}
