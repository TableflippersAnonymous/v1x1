package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
public class Channel extends PartialChannel {
    @JsonProperty("guild_id")
    private String guildId;
    @JsonProperty
    private Integer position;
    @JsonProperty("permission_overwrites")
    private List<Overwrite> permissionOverwrites = new ArrayList<>();
    @JsonProperty
    private String topic;
    @JsonProperty("last_message_id")
    private String lastMessageId;
    @JsonProperty
    private Integer bitrate;
    @JsonProperty("user_limit")
    private Integer userLimit;
    @JsonProperty
    private List<User> recipients = new ArrayList<>();
    @JsonProperty
    private String icon;
    @JsonProperty("owner_id")
    private String ownerId;
    @JsonProperty("application_id")
    private String applicationId;

    public Channel() {
    }

    public Channel(final String id, final ChannelType type, final String guildId, final Integer position,
                   final List<Overwrite> permissionOverwrites, final String name, final String topic,
                   final String lastMessageId, final Integer bitrate, final Integer userLimit,
                   final List<User> recipients, final String icon, final String ownerId, final String applicationId) {
        super(id, type, name);
        this.guildId = guildId;
        this.position = position;
        this.permissionOverwrites = permissionOverwrites;
        this.topic = topic;
        this.lastMessageId = lastMessageId;
        this.bitrate = bitrate;
        this.userLimit = userLimit;
        this.recipients = recipients;
        this.icon = icon;
        this.ownerId = ownerId;
        this.applicationId = applicationId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }

    public List<Overwrite> getPermissionOverwrites() {
        return permissionOverwrites;
    }

    public void setPermissionOverwrites(final List<Overwrite> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(final String topic) {
        this.topic = topic;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(final String lastMessageId) {
        this.lastMessageId = lastMessageId;
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

    public List<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(final List<User> recipients) {
        this.recipients = recipients;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(final String applicationId) {
        this.applicationId = applicationId;
    }
}
