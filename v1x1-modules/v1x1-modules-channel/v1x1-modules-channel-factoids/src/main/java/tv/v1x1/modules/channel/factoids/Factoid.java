package tv.v1x1.modules.channel.factoids;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.TenantPermission;
import tv.v1x1.common.config.Type;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.db.Permission;

/**
 * @author Josh
 */
public class Factoid {
    // Kludge to know what we called this fact by
    @JsonIgnore
    @TenantPermission(tv.v1x1.common.config.Permission.NONE)
    private String id;
    @JsonProperty("hidden")
    @DisplayName("Hidden?")
    @Description("Hiding a fact stops people from asking for it")
    @Type(ConfigType.BOOLEAN)
    private boolean hidden;
    @JsonProperty("alias")
    @DisplayName("Alias?")
    @Description("Is this just the name for a different fact?")
    @Type(ConfigType.BOOLEAN)
    private boolean alias;
    @JsonProperty("data")
    @DisplayName("Message")
    @Description("The message we'll send when someone asks for the fact. If this is an alias, this is the name of the aliased fact")
    @Type(ConfigType.STRING)
    private String data;
    @JsonProperty("permission")
    @DisplayName("Permission required to ask for this fact")
    @Type(ConfigType.PERMISSION)
    private Permission permission;

    private Factoid() {
        // Intentionally empty
    }

    public Factoid(final Tenant tenant, final String data, final Permission permission, final boolean alias) {
        this.data = data;
        this.alias = alias;
        this.hidden = false;
        if(permission == null)
            this.permission = null;
        else
            this.permission = new tv.v1x1.common.dto.db.Permission(permission.getNode());
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id.toLowerCase();
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(final Permission permission) {
        this.permission = permission;
    }

    public boolean isAlias() {
        return alias;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
