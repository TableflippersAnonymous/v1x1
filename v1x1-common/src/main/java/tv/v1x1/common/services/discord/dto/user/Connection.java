package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.guild.Integration;

import java.util.List;

/**
 * Created by naomi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Connection {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String type;
    @JsonProperty
    private boolean revoked;
    @JsonProperty
    private List<Integration> integrations;

    public Connection() {
    }

    public Connection(final String id, final String name, final String type, final boolean revoked,
                      final List<Integration> integrations) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.revoked = revoked;
        this.integrations = integrations;
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

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(final boolean revoked) {
        this.revoked = revoked;
    }

    public List<Integration> getIntegrations() {
        return integrations;
    }

    public void setIntegrations(final List<Integration> integrations) {
        this.integrations = integrations;
    }
}
