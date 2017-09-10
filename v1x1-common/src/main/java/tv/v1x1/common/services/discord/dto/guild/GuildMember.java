package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.user.User;

import java.util.List;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuildMember {
    @JsonProperty
    private User user;
    @JsonProperty
    private String nick;
    @JsonProperty("roles")
    private List<String> roleIds;
    @JsonProperty("joined_at")
    private String joinedAt;
    @JsonProperty
    private boolean deaf;
    @JsonProperty
    private boolean mute;

    public GuildMember() {
    }

    public GuildMember(final User user, final String nick, final List<String> roleIds, final String joinedAt,
                       final boolean deaf, final boolean mute) {
        this.user = user;
        this.nick = nick;
        this.roleIds = roleIds;
        this.joinedAt = joinedAt;
        this.deaf = deaf;
        this.mute = mute;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
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

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(final String joinedAt) {
        this.joinedAt = joinedAt;
    }

    public boolean isDeaf() {
        return deaf;
    }

    public void setDeaf(final boolean deaf) {
        this.deaf = deaf;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(final boolean mute) {
        this.mute = mute;
    }
}
