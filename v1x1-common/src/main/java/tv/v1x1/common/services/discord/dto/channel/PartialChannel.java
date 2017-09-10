package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartialChannel {
    @JsonProperty
    private String id;
    @JsonProperty
    private ChannelType type;
    @JsonProperty
    private String name;

    public PartialChannel() {
    }

    public PartialChannel(final String id, final ChannelType type, final String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(final ChannelType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
