package tv.v1x1.common.services.twitch.dto.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoFps {
    @JsonProperty
    private double chunked;
    @JsonProperty
    private double high;
    @JsonProperty
    private double low;
    @JsonProperty
    private double medium;
    @JsonProperty
    private double mobile;

    public VideoFps() {
    }

    public double getChunked() {
        return chunked;
    }

    public void setChunked(final double chunked) {
        this.chunked = chunked;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(final double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(final double low) {
        this.low = low;
    }

    public double getMedium() {
        return medium;
    }

    public void setMedium(final double medium) {
        this.medium = medium;
    }

    public double getMobile() {
        return mobile;
    }

    public void setMobile(final double mobile) {
        this.mobile = mobile;
    }
}
