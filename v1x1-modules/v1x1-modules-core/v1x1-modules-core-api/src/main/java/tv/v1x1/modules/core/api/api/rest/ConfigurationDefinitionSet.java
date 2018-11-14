package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurationDefinitionSet {
    @JsonProperty
    private ConfigurationDefinition global;
    @JsonProperty
    private ConfigurationDefinition user;

    public ConfigurationDefinitionSet() {
    }

    public ConfigurationDefinitionSet(final ConfigurationDefinition global, final ConfigurationDefinition user) {
        this.global = global;
        this.user = user;
    }

    public ConfigurationDefinition getGlobal() {
        return global;
    }

    public void setGlobal(final ConfigurationDefinition global) {
        this.global = global;
    }

    public ConfigurationDefinition getUser() {
        return user;
    }

    public void setUser(final ConfigurationDefinition user) {
        this.user = user;
    }
}
