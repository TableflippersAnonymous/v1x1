package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.permissions.PermissionSet;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Overwrite {
    @JsonProperty
    private String id;
    @JsonProperty
    private OverwriteType type;
    @JsonProperty
    private PermissionSet allow;
    @JsonProperty
    private PermissionSet deny;

    public Overwrite() {
    }

    public Overwrite(final String id, final OverwriteType type, final PermissionSet allow, final PermissionSet deny) {
        this.id = id;
        this.type = type;
        this.allow = allow;
        this.deny = deny;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public OverwriteType getType() {
        return type;
    }

    public void setType(final OverwriteType type) {
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
}
