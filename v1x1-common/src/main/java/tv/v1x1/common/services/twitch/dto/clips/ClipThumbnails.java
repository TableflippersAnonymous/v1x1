package tv.v1x1.common.services.twitch.dto.clips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/28/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClipThumbnails {
    @JsonProperty
    private String medium;
    @JsonProperty
    private String small;
    @JsonProperty
    private String tiny;

    public ClipThumbnails() {
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(final String medium) {
        this.medium = medium;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(final String small) {
        this.small = small;
    }

    public String getTiny() {
        return tiny;
    }

    public void setTiny(final String tiny) {
        this.tiny = tiny;
    }
}
