package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Josh
 */
public class Channel {
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("id")
    private String id;

    public Channel() {
        // For Jackson
    }

    public Channel(final String displayName, final String id) {
        this.displayName = displayName;
        this.id = id;
    }

    public Channel(final tv.v1x1.common.dto.core.Channel channel) {
        this(channel.getDisplayName(), channel.getId());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
