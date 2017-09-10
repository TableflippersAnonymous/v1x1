package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.permissions.PermissionSet;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGuildRoleRequest {
    @JsonProperty
    private String name;
    @JsonProperty
    private PermissionSet permissions;
    @JsonProperty
    private Integer color;
    @JsonProperty
    private boolean hoist;
    @JsonProperty
    private boolean mentionable;

    public CreateGuildRoleRequest() {
    }

    public CreateGuildRoleRequest(final String name) {
        this.name = name;
    }

    public CreateGuildRoleRequest(final String name, final PermissionSet permissions, final Integer color,
                                  final boolean hoist, final boolean mentionable) {
        this.name = name;
        this.permissions = permissions;
        this.color = color;
        this.hoist = hoist;
        this.mentionable = mentionable;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public PermissionSet getPermissions() {
        return permissions;
    }

    public void setPermissions(final PermissionSet permissions) {
        this.permissions = permissions;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(final Integer color) {
        this.color = color;
    }

    public boolean isHoist() {
        return hoist;
    }

    public void setHoist(final boolean hoist) {
        this.hoist = hoist;
    }

    public boolean isMentionable() {
        return mentionable;
    }

    public void setMentionable(final boolean mentionable) {
        this.mentionable = mentionable;
    }
}
