package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityPermissions {
    @JsonProperty
    private boolean ban;
    @JsonProperty
    private boolean timeout;
    @JsonProperty
    private boolean edit;

    public CommunityPermissions() {
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(final boolean ban) {
        this.ban = ban;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(final boolean timeout) {
        this.timeout = timeout;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(final boolean edit) {
        this.edit = edit;
    }
}
