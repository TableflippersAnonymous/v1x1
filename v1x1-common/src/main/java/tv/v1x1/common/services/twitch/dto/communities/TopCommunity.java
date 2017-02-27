package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopCommunity extends CommunityId {
    @JsonProperty("avatar_image_url")
    private String avatarImageUrl;
    @JsonProperty
    private long channels;
    @JsonProperty
    private String name;
    @JsonProperty
    private long viewers;

    public TopCommunity() {
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public void setAvatarImageUrl(final String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public long getChannels() {
        return channels;
    }

    public void setChannels(final long channels) {
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getViewers() {
        return viewers;
    }

    public void setViewers(final long viewers) {
        this.viewers = viewers;
    }
}
