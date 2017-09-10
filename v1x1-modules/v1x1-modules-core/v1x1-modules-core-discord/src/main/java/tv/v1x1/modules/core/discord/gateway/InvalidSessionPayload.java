package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class InvalidSessionPayload extends Payload {
    @JsonProperty("d")
    private boolean resumable;

    public InvalidSessionPayload() {
    }

    public InvalidSessionPayload(final boolean resumable) {
        super(9);
        this.resumable = resumable;
    }

    public boolean isResumable() {
        return resumable;
    }

    public void setResumable(final boolean resumable) {
        this.resumable = resumable;
    }
}
