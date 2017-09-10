package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.permissions.PermissionSet;

/**
 * Created by cobi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditChannelPermissionsRequest {
    @JsonProperty
    private PermissionSet allow;
    @JsonProperty
    private PermissionSet deny;
    @JsonProperty
    private OverwriteType type;

    public EditChannelPermissionsRequest() {
    }

    public EditChannelPermissionsRequest(final PermissionSet allow, final PermissionSet deny,
                                         final OverwriteType type) {
        this.allow = allow;
        this.deny = deny;
        this.type = type;
    }

    public PermissionSet getAllow() {
        return allow;
    }

    public void setAllow(final PermissionSet allow) {
        this.allow = allow;
    }

    public PermissionSet getDeny() {
        return deny;
    }

    public void setDeny(final PermissionSet deny) {
        this.deny = deny;
    }

    public OverwriteType getType() {
        return type;
    }

    public void setType(final OverwriteType type) {
        this.type = type;
    }
}
