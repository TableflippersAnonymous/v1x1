package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostRequest {
    @JsonProperty
    private String content;
    @JsonProperty
    private boolean share;

    public PostRequest() {
    }

    public PostRequest(final String content, final boolean share) {
        this.content = content;
        this.share = share;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(final boolean share) {
        this.share = share;
    }
}
