package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Module {
    @JsonProperty
    private String name;
    @JsonProperty("configuration_definition_set")
    private ConfigurationDefinitionSet configurationDefinitionSet;

    public Module() {
    }

    public Module(final String name, final ConfigurationDefinitionSet configurationDefinitionSet) {
        this.name = name;
        this.configurationDefinitionSet = configurationDefinitionSet;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ConfigurationDefinitionSet getConfigurationDefinitionSet() {
        return configurationDefinitionSet;
    }

    public void setConfigurationDefinitionSet(final ConfigurationDefinitionSet configurationDefinitionSet) {
        this.configurationDefinitionSet = configurationDefinitionSet;
    }
}
