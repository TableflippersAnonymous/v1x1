package tv.v1x1.common.services.slack.dto.misc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMetadata {
    @JsonProperty("next_cursor")
    private String nextCursor;

    public ResponseMetadata() {
    }

    public ResponseMetadata(final String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(final String nextCursor) {
        this.nextCursor = nextCursor;
    }
}
