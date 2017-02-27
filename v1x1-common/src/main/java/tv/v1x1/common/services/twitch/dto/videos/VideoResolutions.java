package tv.v1x1.common.services.twitch.dto.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoResolutions {
    @JsonProperty
    private String chunked;
    @JsonProperty
    private String high;
    @JsonProperty
    private String low;
    @JsonProperty
    private String medium;
    @JsonProperty
    private String mobile;

    public VideoResolutions() {
    }

    public String getChunked() {
        return chunked;
    }

    public void setChunked(final String chunked) {
        this.chunked = chunked;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(final String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(final String low) {
        this.low = low;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(final String medium) {
        this.medium = medium;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }
}
