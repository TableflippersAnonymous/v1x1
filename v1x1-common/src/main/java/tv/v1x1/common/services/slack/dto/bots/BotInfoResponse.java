package tv.v1x1.common.services.slack.dto.bots;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class BotInfoResponse extends Response {
    @JsonProperty
    private BotInfo bot;

    public BotInfoResponse() {
        super(true);
    }

    public BotInfoResponse(final BotInfo bot) {
        super(true);
        this.bot = bot;
    }

    public BotInfo getBot() {
        return bot;
    }

    public void setBot(final BotInfo bot) {
        this.bot = bot;
    }
}
