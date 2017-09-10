package tv.v1x1.common.services.discord.dto.permissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private Integer color;
    @JsonProperty
    private boolean hoist;
    @JsonProperty
    private Integer position;
    @JsonProperty
    private PermissionSet permissions;
    @JsonProperty
    private boolean managed;
    @JsonProperty
    private boolean mentionable;

    public Role() {
    }

    public Role(final String id, final String name, final Integer color, final boolean hoist, final Integer position,
                final PermissionSet permissions, final boolean managed, final boolean mentionable) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.hoist = hoist;
        this.position = position;
        this.permissions = permissions;
        this.managed = managed;
        this.mentionable = mentionable;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }

    public PermissionSet getPermissions() {
        return permissions;
    }

    public void setPermissions(final PermissionSet permissions) {
        this.permissions = permissions;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(final boolean managed) {
        this.managed = managed;
    }

    public boolean isMentionable() {
        return mentionable;
    }

    public void setMentionable(final boolean mentionable) {
        this.mentionable = mentionable;
    }
}
