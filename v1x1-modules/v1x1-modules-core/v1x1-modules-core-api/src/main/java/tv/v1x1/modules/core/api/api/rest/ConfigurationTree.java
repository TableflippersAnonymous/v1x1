package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ConfigurationTree {
    @JsonProperty("tenant_configuration")
    private Configuration tenantConfiguration;
    @JsonProperty
    private Tenant tenant;
    @JsonProperty("channel_group_configurations")
    private List<ChannelGroupConfigurationTree> channelGroupConfigurations;

    public ConfigurationTree() {
    }

    public ConfigurationTree(final Configuration tenantConfiguration, final Tenant tenant,
                             final List<ChannelGroupConfigurationTree> channelGroupConfigurations) {
        this.tenantConfiguration = tenantConfiguration;
        this.tenant = tenant;
        this.channelGroupConfigurations = channelGroupConfigurations;
    }

    public Configuration getTenantConfiguration() {
        return tenantConfiguration;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public List<ChannelGroupConfigurationTree> getChannelGroupConfigurations() {
        return channelGroupConfigurations;
    }
}
