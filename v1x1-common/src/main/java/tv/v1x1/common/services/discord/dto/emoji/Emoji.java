package tv.v1x1.common.services.discord.dto.emoji;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.permissions.Role;

import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Emoji {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private List<Role> roles;
    @JsonProperty("require_colons")
    private boolean requireColons;
    @JsonProperty
    private boolean managed;

    public Emoji() {
    }

    public Emoji(final String id, final String name, final List<Role> roles, final boolean requireColons,
                 final boolean managed) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.requireColons = requireColons;
        this.managed = managed;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }

    public boolean isRequireColons() {
        return requireColons;
    }

    public void setRequireColons(final boolean requireColons) {
        this.requireColons = requireColons;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(final boolean managed) {
        this.managed = managed;
    }
}
