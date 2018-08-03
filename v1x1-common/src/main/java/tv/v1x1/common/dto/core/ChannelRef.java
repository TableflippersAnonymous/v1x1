package tv.v1x1.common.dto.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dto.db.Platform;

/**
 * Created by Josh on 2018-07-22
 */
public class ChannelRef {
    @JsonProperty
    protected Platform platform;
    @JsonProperty
    protected String id;

    public ChannelRef() {
        // Intentionally left blank for Jackson
    }

    public ChannelRef(final Platform platform, final String id) {
        this.platform = platform;
        this.id = id;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getId() {
        return id;
    }
}
