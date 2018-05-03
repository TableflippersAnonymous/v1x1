package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

import java.util.List;

public class ChannelListResponse extends Response {
    public static class Metadata {
        @JsonProperty("next_cursor")
        private String nextCursor;

        public Metadata() {
        }

        public Metadata(final String nextCursor) {
            this.nextCursor = nextCursor;
        }

        public String getNextCursor() {
            return nextCursor;
        }

        public void setNextCursor(final String nextCursor) {
            this.nextCursor = nextCursor;
        }
    }

    @JsonProperty
    private List<Channel> channels;
    @JsonProperty("response_metadata")
    private Metadata responseMetadata;

    public ChannelListResponse() {
        super(true);
    }

    public ChannelListResponse(final List<Channel> channels, final Metadata responseMetadata) {
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

    public Metadata getResponseMetadata() {
        return responseMetadata;
    }

    public void setResponseMetadata(final Metadata responseMetadata) {
        this.responseMetadata = responseMetadata;
    }
}
