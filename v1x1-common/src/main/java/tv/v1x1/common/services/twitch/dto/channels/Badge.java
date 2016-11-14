package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Badge {
    @JsonProperty
    private String alpha;
    @JsonProperty
    private String image;
    @JsonProperty
    private String svg;

    public Badge() {
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(final String alpha) {
        this.alpha = alpha;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(final String svg) {
        this.svg = svg;
    }
}
