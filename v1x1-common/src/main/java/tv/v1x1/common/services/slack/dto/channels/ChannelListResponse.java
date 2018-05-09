package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;
import tv.v1x1.common.services.slack.dto.misc.ResponseMetadata;

import java.util.List;

public class ChannelListResponse extends Response {
    @JsonProperty
    private List<Channel> channels;
    @JsonProperty("response_metadata")
    private ResponseMetadata responseMetadata;

    public ChannelListResponse() {
        super(true);
    }

    public ChannelListResponse(final List<Channel> channels, final ResponseMetadata responseMetadata) {
        super(true);
        this.channels = channels;
        this.responseMetadata = responseMetadata;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(final List<Channel> channels) {
        this.channels = channels;
    }

    public ResponseMetadata getResponseMetadata() {
        return responseMetadata;
    }

    public void setResponseMetadata(final ResponseMetadata responseMetadata) {
        this.responseMetadata = responseMetadata;
    }
}
