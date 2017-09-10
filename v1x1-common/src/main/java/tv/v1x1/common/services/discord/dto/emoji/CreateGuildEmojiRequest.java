package tv.v1x1.common.services.discord.dto.emoji;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGuildEmojiRequest {
    @JsonProperty
    private String name;
    @JsonProperty
    private String image;
    @JsonProperty("roles")
    private List<String> roleIds;

    public CreateGuildEmojiRequest() {
    }

    public CreateGuildEmojiRequest(final String name, final String image, final List<String> roleIds) {
        this.name = name;
        this.image = image;
        this.roleIds = roleIds;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(final List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
