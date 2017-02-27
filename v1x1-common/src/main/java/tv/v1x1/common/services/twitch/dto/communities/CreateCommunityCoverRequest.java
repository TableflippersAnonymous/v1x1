package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCommunityCoverRequest {
    @JsonProperty("cover_image")
    private String coverImage;

    public CreateCommunityCoverRequest() {
    }

    public CreateCommunityCoverRequest(final String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(final String coverImage) {
        this.coverImage = coverImage;
    }
}
