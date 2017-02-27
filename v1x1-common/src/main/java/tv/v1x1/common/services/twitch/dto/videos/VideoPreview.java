package tv.v1x1.common.services.twitch.dto.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoPreview {
    @JsonProperty
    private String large;
    @JsonProperty
    private String medium;
    @JsonProperty
    private String small;
    @JsonProperty
    private String template;

    public VideoPreview() {
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(final String large) {
        this.large = large;
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

    public String getTemplate() {
        return template;
    }

    public void setTemplate(final String template) {
        this.template = template;
    }
}
