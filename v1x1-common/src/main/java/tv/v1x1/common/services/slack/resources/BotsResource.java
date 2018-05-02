package tv.v1x1.common.services.slack.resources;

import tv.v1x1.common.services.slack.SlackApi;
import tv.v1x1.common.services.slack.dto.bots.BotInfoResponse;
import tv.v1x1.common.services.slack.exceptions.BotNotFoundException;
import tv.v1x1.common.services.slack.exceptions.SlackApiException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import java.util.Objects;

public class BotsResource {
    private final WebTarget api;
    private final String token;

    public BotsResource(final WebTarget api, final String token) {
        this.api = api;
        this.token = token;
    }

    public BotInfoResponse getBotInfo(final String botId) throws BotNotFoundException {
        final BotInfoResponse botInfoResponse = this.api.path("bots.info")
                .request(SlackApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("token", token)
                        .param("bot", botId)
                ))
                .readEntity(BotInfoResponse.class);
        if(botInfoResponse.isOk())
            return botInfoResponse;
        if(Objects.equals(botInfoResponse.getError(), "bot_not_found"))
            throw new BotNotFoundException();
        throw new SlackApiException(botInfoResponse.getError());
    }
}
