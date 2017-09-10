package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuildChannelPosition {
    @JsonProperty("id")
    private String channelId;
    @JsonProperty
    private Integer position;

    public GuildChannelPosition() {
    }

    public GuildChannelPosition(final String channelId, final Integer position) {
        this.channelId = channelId;
        this.position = position;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }
}
