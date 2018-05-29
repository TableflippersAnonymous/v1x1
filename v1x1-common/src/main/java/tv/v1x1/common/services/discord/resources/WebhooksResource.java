package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.channel.Message;
import tv.v1x1.common.services.discord.dto.webhook.CreateWebhookRequest;
import tv.v1x1.common.services.discord.dto.webhook.ExecuteWebhookRequest;
import tv.v1x1.common.services.discord.dto.webhook.ModifyWebhookRequest;
import tv.v1x1.common.services.discord.dto.webhook.Webhook;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by cobi on 9/17/2017.
 */
public class WebhooksResource {
    private final WebTarget channels;
    private final WebTarget guilds;
    private final WebTarget webhooks;

    public WebhooksResource(final WebTarget channels, final WebTarget guilds, final WebTarget webhooks) {
        this.channels = channels;
        this.guilds = guilds;
        this.webhooks = webhooks;
    }

    public Webhook createWebhook(final String channelId, final CreateWebhookRequest createWebhookRequest) {
        return channels.path(channelId).path("webhooks")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createWebhookRequest, MediaType.APPLICATION_JSON), Webhook.class);
    }

    public List<Webhook> getChannelWebhooks(final String channelId) {
        return channels.path(channelId).path("webhooks")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Webhook>>() {});
    }

    public List<Webhook> getGuildWebhooks(final String guildId) {
        return guilds.path(guildId).path("webhooks")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Webhook>>() {});
    }

    public Webhook getWebhook(final String webhookId) {
        return webhooks.path(webhookId)
                .request(DiscordApi.ACCEPT)
                .get(Webhook.class);
    }

    public Webhook getWebhookWithToken(final String webhookId, final String webhookToken) {
        return webhooks.path(webhookId).path(webhookToken)
                .request(DiscordApi.ACCEPT)
                .get(Webhook.class);
    }

    public Webhook modifyWebhook(final String webhookId, final ModifyWebhookRequest modifyWebhookRequest) {
        return webhooks.path(webhookId)
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyWebhookRequest, MediaType.APPLICATION_JSON), Webhook.class);
    }

    public Webhook modifyWebhookWithToken(final String webhookId, final String webhookToken,
                                          final ModifyWebhookRequest modifyWebhookRequest) {
        return webhooks.path(webhookId).path(webhookToken)
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyWebhookRequest, MediaType.APPLICATION_JSON), Webhook.class);
    }

    public void deleteWebhook(final String webhookId) {
        webhooks.path(webhookId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public void deleteWebhookWithToken(final String webhookId, final String webhookToken) {
        webhooks.path(webhookId).path(webhookToken)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public Message executeWebhook(final String webhookId, final String webhookToken, final Boolean wait,
                                  final ExecuteWebhookRequest executeWebhookRequest) {
        return webhooks.path(webhookId).path(webhookToken)
                .queryParam("wait", wait)
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(executeWebhookRequest, MediaType.APPLICATION_JSON), Message.class);
    }
}
