package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCommunityAvatarRequest {
    @JsonProperty("avatar_image")
    private String avatarImage;

    public CreateCommunityAvatarRequest() {
    }

    public CreateCommunityAvatarRequest(final String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(final String avatarImage) {
        this.avatarImage = avatarImage;
    }
}
