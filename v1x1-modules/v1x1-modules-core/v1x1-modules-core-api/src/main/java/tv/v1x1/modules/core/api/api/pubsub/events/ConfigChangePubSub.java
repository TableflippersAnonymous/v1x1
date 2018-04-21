package tv.v1x1.modules.core.api.api.pubsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigChangePubSub {
    @JsonProperty
    private String module;

    public ConfigChangePubSub() {
    }

    public ConfigChangePubSub(final String module) {
        this.module = module;
    }

    public String getModule() {
        return module;
    }

    public void setModule(final String module) {
        this.module = module;
    }
}
