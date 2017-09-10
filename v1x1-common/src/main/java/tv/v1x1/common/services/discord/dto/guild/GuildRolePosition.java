package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuildRolePosition {
    @JsonProperty("id")
    private String roleId;
    @JsonProperty
    private Integer position;

    public GuildRolePosition() {
    }

    public GuildRolePosition(final String roleId, final Integer position) {
        this.roleId = roleId;
        this.position = position;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(final String roleId) {
        this.roleId = roleId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }
}
