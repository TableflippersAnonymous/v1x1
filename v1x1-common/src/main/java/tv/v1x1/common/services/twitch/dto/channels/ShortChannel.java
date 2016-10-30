package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/30/2016.
 */
public class ShortChannel {
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty
    private String name;

    public ShortChannel() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
