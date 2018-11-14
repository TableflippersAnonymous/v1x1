package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class WebSync {
    @JsonProperty
    private WebConfig configuration;
    @JsonProperty
    private Map<String, Module> modules;
    @JsonProperty("current_user")
    private GlobalUser currentUser;
    @JsonProperty
    private Map<String, SyncTenant> tenants;

    public WebSync() {
    }

    public WebSync(final WebConfig configuration, final Map<String, Module> modules, final GlobalUser currentUser,
                   final Map<String, SyncTenant> tenants) {
        this.configuration = configuration;
        this.modules = modules;
        this.currentUser = currentUser;
        this.tenants = tenants;
    }

    public WebConfig getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final WebConfig configuration) {
        this.configuration = configuration;
    }

    public Map<String, Module> getModules() {
        return modules;
    }

    public void setModules(final Map<String, Module> modules) {
        this.modules = modules;
    }

    public GlobalUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(final GlobalUser currentUser) {
        this.currentUser = currentUser;
    }

    public Map<String, SyncTenant> getTenants() {
        return tenants;
    }

    public void setTenants(final Map<String, SyncTenant> tenants) {
        this.tenants = tenants;
    }
}
