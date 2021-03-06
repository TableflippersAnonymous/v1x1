package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.gateway.BotGateway;
import tv.v1x1.common.services.discord.dto.gateway.Gateway;

import javax.ws.rs.client.WebTarget;

/**
 * Created by cobi on 9/17/2017.
 */
public class GatewayResource {
    private final WebTarget gateway;

    public GatewayResource(final WebTarget gateway) {
        this.gateway = gateway;
    }

    public Gateway getGateway() {
        return gateway
                .request(DiscordApi.ACCEPT)
                .get(Gateway.class);
    }

    public BotGateway getGatewayBot() {
        return gateway.path("bot")
                .request(DiscordApi.ACCEPT)
                .get(BotGateway.class);
    }
}
