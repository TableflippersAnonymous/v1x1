package tv.v1x1.common.services.discord.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.user.User;

/**
 * Created by naomi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Webhook {
    @JsonProperty
    private String id;
    @JsonProperty("guild_id")
    private String guildId;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty
    private User user;
    @JsonProperty
    private String name;
    @JsonProperty
    private String avatar;
    @JsonProperty
    private String token;

    public Webhook() {
    }

    public Webhook(final String id, final String guildId, final String channelId, final User user, final String name,
                   final String avatar, final String token) {
        this.id = id;
        this.guildId = guildId;
        this.channelId = channelId;
        this.user = user;
        this.name = name;
        this.avatar = avatar;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}
