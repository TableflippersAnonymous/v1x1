package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommercialRequest {
    @JsonProperty
    private int length;

    public CommercialRequest() {
    }

    public CommercialRequest(final int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }
}
