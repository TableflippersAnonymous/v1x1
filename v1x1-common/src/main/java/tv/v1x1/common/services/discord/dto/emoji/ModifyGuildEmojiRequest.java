package tv.v1x1.common.services.discord.dto.emoji;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyGuildEmojiRequest {
    @JsonProperty
    private String name;
    @JsonProperty("roles")
    private List<String> roleIds;

    public ModifyGuildEmojiRequest() {
    }

    public ModifyGuildEmojiRequest(final String name) {
        this.name = name;
    }

    public ModifyGuildEmojiRequest(final List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public ModifyGuildEmojiRequest(final String name, final List<String> roleIds) {
        this.name = name;
        this.roleIds = roleIds;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(final List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
