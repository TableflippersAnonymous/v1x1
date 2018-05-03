package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class ChatPermalinkResponse extends Response {
    @JsonProperty("channel")
    private String channelId;
    @JsonProperty
    private String permalink;

    public ChatPermalinkResponse() {
        super(true);
    }

    public ChatPermalinkResponse(final String channelId, final String permalink) {
        super(true);
        this.channelId = channelId;
        this.permalink = permalink;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(final String permalink) {
        this.permalink = permalink;
    }
}
