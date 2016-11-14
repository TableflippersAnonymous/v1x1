package tv.v1x1.common.services.twitch.dto.games;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.games.Game;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopGame {
    @JsonProperty
    private Game game;
    @JsonProperty
    private long viewers;
    @JsonProperty
    private long channels;

    public TopGame() {
    }

    public Game getGame() {
        return game;
    }

    public void setGame(final Game game) {
        this.game = game;
    }

    public long getViewers() {
        return viewers;
    }

    public void setViewers(final long viewers) {
        this.viewers = viewers;
    }

    public long getChannels() {
        return channels;
    }

    public void setChannels(final long channels) {
        this.channels = channels;
    }
}
