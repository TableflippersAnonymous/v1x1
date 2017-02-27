package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Permissions {
    @JsonProperty("can_delete")
    private boolean canDelete;
    @JsonProperty("can_moderate")
    private boolean canModerate;
    @JsonProperty("can_reply")
    private boolean canReply;

    public Permissions() {
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(final boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanModerate() {
        return canModerate;
    }

    public void setCanModerate(final boolean canModerate) {
        this.canModerate = canModerate;
    }

    public boolean isCanReply() {
        return canReply;
    }

    public void setCanReply(final boolean canReply) {
        this.canReply = canReply;
    }
}
