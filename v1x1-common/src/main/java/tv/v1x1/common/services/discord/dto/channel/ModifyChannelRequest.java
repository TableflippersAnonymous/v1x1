package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyChannelRequest {
    @JsonProperty
    private String name;
    @JsonProperty
    private Integer position;
    @JsonProperty
    private String topic;
    @JsonProperty
    private Integer bitrate;
    @JsonProperty("user_limit")
    private Integer userLimit;

    public ModifyChannelRequest() {
    }

    public ModifyChannelRequest(final String name, final Integer position, final String topic) {
        this.name = name;
        this.position = position;
        this.topic = topic;
    }

    public ModifyChannelRequest(final String name, final Integer position, final Integer bitrate, final Integer userLimit) {
        this.name = name;
        this.position = position;
        this.bitrate = bitrate;
        this.userLimit = userLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(final String topic) {
        this.topic = topic;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(final Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(final Integer userLimit) {
        this.userLimit = userLimit;
    }
}
