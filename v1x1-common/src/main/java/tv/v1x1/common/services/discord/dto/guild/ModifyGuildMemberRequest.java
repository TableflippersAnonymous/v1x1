package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyGuildMemberRequest {
    @JsonProperty
    private String nick;
    @JsonProperty("role_ids")
    private List<String> roleIds;
    @JsonProperty
    private Boolean mute;
    @JsonProperty
    private Boolean deaf;
    @JsonProperty("channel_id")
    private String channelId;

    public ModifyGuildMemberRequest() {
    }

    public ModifyGuildMemberRequest(final String nick) {
        this.nick = nick;
    }

    public ModifyGuildMemberRequest(final List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public ModifyGuildMemberRequest(final Boolean mute, final Boolean deaf) {
        this.mute = mute;
        this.deaf = deaf;
    }

    public ModifyGuildMemberRequest(final Boolean mute, final Boolean deaf, final String channelId) {
        this.mute = mute;
        this.deaf = deaf;
        this.channelId = channelId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(final String nick) {
        this.nick = nick;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(final List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Boolean getMute() {
        return mute;
    }

    public void setMute(final Boolean mute) {
        this.mute = mute;
    }

    public Boolean getDeaf() {
        return deaf;
    }

    public void setDeaf(final Boolean deaf) {
        this.deaf = deaf;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }
}
