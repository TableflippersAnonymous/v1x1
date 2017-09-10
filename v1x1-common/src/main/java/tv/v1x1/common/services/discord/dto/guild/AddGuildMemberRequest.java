package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddGuildMemberRequest {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty
    private String nick;
    @JsonProperty("roles")
    private List<String> roleIds;
    @JsonProperty
    private Boolean mute;
    @JsonProperty
    private Boolean deaf;

    public AddGuildMemberRequest() {
    }

    public AddGuildMemberRequest(final String accessToken) {
        this.accessToken = accessToken;
    }

    public AddGuildMemberRequest(final String accessToken, final String nick) {
        this.accessToken = accessToken;
        this.nick = nick;
    }

    public AddGuildMemberRequest(final String accessToken, final List<String> roleIds) {
        this.accessToken = accessToken;
        this.roleIds = roleIds;
    }

    public AddGuildMemberRequest(final String accessToken, final Boolean mute, final Boolean deaf) {
        this.accessToken = accessToken;
        this.mute = mute;
        this.deaf = deaf;
    }

    public AddGuildMemberRequest(final String accessToken, final String nick, final List<String> roleIds, final Boolean mute, final Boolean deaf) {
        this.accessToken = accessToken;
        this.nick = nick;
        this.roleIds = roleIds;
        this.mute = mute;
        this.deaf = deaf;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
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
}
