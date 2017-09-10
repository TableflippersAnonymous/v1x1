package tv.v1x1.common.services.discord.dto.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.user.User;

import java.util.List;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationInformation {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String description;
    @JsonProperty("rpc_origins")
    private List<String> rpcOrigins;
    @JsonProperty("bot_public")
    private boolean botPublic;
    @JsonProperty("bot_require_code_grant")
    private boolean botRequireCodeGrant;
    @JsonProperty
    private User owner;

    public ApplicationInformation() {
    }

    public ApplicationInformation(final String id, final String name, final String icon, final String description,
                                  final List<String> rpcOrigins, final boolean botPublic,
                                  final boolean botRequireCodeGrant, final User owner) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.rpcOrigins = rpcOrigins;
        this.botPublic = botPublic;
        this.botRequireCodeGrant = botRequireCodeGrant;
        this.owner = owner;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<String> getRpcOrigins() {
        return rpcOrigins;
    }

    public void setRpcOrigins(final List<String> rpcOrigins) {
        this.rpcOrigins = rpcOrigins;
    }

    public boolean isBotPublic() {
        return botPublic;
    }

    public void setBotPublic(final boolean botPublic) {
        this.botPublic = botPublic;
    }

    public boolean isBotRequireCodeGrant() {
        return botRequireCodeGrant;
    }

    public void setBotRequireCodeGrant(final boolean botRequireCodeGrant) {
        this.botRequireCodeGrant = botRequireCodeGrant;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }
}
