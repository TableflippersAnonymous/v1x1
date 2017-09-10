package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.permissions.PermissionSet;

/**
 * Created by naomi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyGuildRoleRequest {
    @JsonProperty
    private String name;
    @JsonProperty
    private PermissionSet permissions;
    @JsonProperty
    private Integer color;
    @JsonProperty
    private Boolean hoist;
    @JsonProperty
    private Boolean mentionable;

    public ModifyGuildRoleRequest(final String name) {
        this.name = name;
    }

    public ModifyGuildRoleRequest(final String name, final PermissionSet permissions, final Integer color,
                                  final Boolean hoist, final Boolean mentionable) {
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

    public Boolean getHoist() {
        return hoist;
    }

    public void setHoist(final Boolean hoist) {
        this.hoist = hoist;
    }

    public Boolean getMentionable() {
        return mentionable;
    }

    public void setMentionable(final Boolean mentionable) {
        this.mentionable = mentionable;
    }
}
