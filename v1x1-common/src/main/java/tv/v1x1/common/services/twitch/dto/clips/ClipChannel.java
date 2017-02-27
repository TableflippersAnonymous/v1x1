package tv.v1x1.common.services.twitch.dto.clips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/28/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClipChannel {
    @JsonProperty("channel_url")
    private String channelUrl;
    @JsonProperty
    private String id;
    @JsonProperty
    private String logo;
    @JsonProperty
    private String name;
    @JsonProperty("display_name")
    private String displayName;

    public ClipChannel() {
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(final String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(final String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
}
