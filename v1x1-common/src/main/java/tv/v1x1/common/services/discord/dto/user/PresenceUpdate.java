package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PresenceUpdate {
    @JsonProperty
    private User user;
    @JsonProperty("role_ids")
    private List<String> roleIds;
    @JsonProperty
    private Game game;
    @JsonProperty("guild_id")
    private String guildId;
    @JsonProperty
    private Status status;

    public PresenceUpdate() {
    }

    public PresenceUpdate(final User user, final List<String> roleIds, final Game game, final String guildId,
                          final Status status) {
        this.user = user;
        this.roleIds = roleIds;
        this.game = game;
        this.guildId = guildId;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(final List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(final Game game) {
        this.game = game;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }
}
