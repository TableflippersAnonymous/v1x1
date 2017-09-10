package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusUpdate {
    @JsonProperty
    private Integer since;
    @JsonProperty
    private Game game;
    @JsonProperty
    private Status status;
    @JsonProperty
    private boolean afk;

    public StatusUpdate() {
    }

    public StatusUpdate(final Integer since, final Game game, final Status status, final boolean afk) {
        this.since = since;
        this.game = game;
        this.status = status;
        this.afk = afk;
    }

    public Integer getSince() {
        return since;
    }

    public void setSince(final Integer since) {
        this.since = since;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(final Game game) {
        this.game = game;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public boolean isAfk() {
        return afk;
    }

    public void setAfk(final boolean afk) {
        this.afk = afk;
    }
}
