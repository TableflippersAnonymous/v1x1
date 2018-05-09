package tv.v1x1.common.services.slack.dto.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class ConversationCloseResponse extends Response {
    @JsonProperty("no_op")
    private Boolean noOp;
    @JsonProperty("already_closed")
    private Boolean alreadyClosed;

    public ConversationCloseResponse() {
        super(true);
    }

    public ConversationCloseResponse(final Boolean noOp, final Boolean alreadyClosed) {
        super(true);
        this.noOp = noOp;
        this.alreadyClosed = alreadyClosed;
    }

    public Boolean isNoOp() {
        return noOp;
    }

    public void setNoOp(final Boolean noOp) {
        this.noOp = noOp;
    }

    public Boolean isAlreadyClosed() {
        return alreadyClosed;
    }

    public void setAlreadyClosed(final Boolean alreadyClosed) {
        this.alreadyClosed = alreadyClosed;
    }
}
