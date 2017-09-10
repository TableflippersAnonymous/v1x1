package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.channel.ChannelType;
import tv.v1x1.common.services.discord.dto.channel.Overwrite;

import java.util.List;

/**
 * Created by naomi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGuildChannelRequest {
    @JsonProperty
    private String name;
    @JsonProperty
    private ChannelType type;
    @JsonProperty
    private Integer bitrate;
    @JsonProperty("user_limit")
    private Integer userLimit;
    @JsonProperty("permission_overwrites")
    private List<Overwrite> permissionOverwrites;

    public CreateGuildChannelRequest() {
    }

    public CreateGuildChannelRequest(final String name, final ChannelType type) {
        this.name = name;
        this.type = type;
    }

    public CreateGuildChannelRequest(final String name, final Integer bitrate, final Integer userLimit) {
        this.name = name;
        this.type = ChannelType.GUILD_VOICE;
        this.bitrate = bitrate;
        this.userLimit = userLimit;
    }

    public CreateGuildChannelRequest(final String name, final ChannelType type,
                                     final List<Overwrite> permissionOverwrites) {
        this.name = name;
        this.type = type;
        this.permissionOverwrites = permissionOverwrites;
    }

    public CreateGuildChannelRequest(final String name, final Integer bitrate, final Integer userLimit,
                                     final List<Overwrite> permissionOverwrites) {
        this.name = name;
        this.type = ChannelType.GUILD_VOICE;
        this.bitrate = bitrate;
        this.userLimit = userLimit;
        this.permissionOverwrites = permissionOverwrites;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(final ChannelType type) {
        this.type = type;
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

    public List<Overwrite> getPermissionOverwrites() {
        return permissionOverwrites;
    }

    public void setPermissionOverwrites(final List<Overwrite> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
    }
}
