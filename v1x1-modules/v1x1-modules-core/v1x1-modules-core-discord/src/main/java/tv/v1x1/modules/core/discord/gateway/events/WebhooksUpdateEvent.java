package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.guild.GuildId;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class WebhooksUpdateEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebhooksUpdate extends GuildId {
        @JsonProperty("channel_id")
        private String channelId;

        public WebhooksUpdate() {
        }

        public WebhooksUpdate(final String guildId, final String channelId) {
            super(guildId);
            this.channelId = channelId;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(final String channelId) {
            this.channelId = channelId;
        }
    }

    @JsonProperty("d")
    private WebhooksUpdate webhooksUpdate;

    public WebhooksUpdateEvent() {
    }

    public WebhooksUpdateEvent(final Long sequenceNumber, final WebhooksUpdate webhooksUpdate) {
        super(sequenceNumber, "WEBHOOKS_UPDATE");
        this.webhooksUpdate = webhooksUpdate;
    }

    public WebhooksUpdate getWebhooksUpdate() {
        return webhooksUpdate;
    }

    public void setWebhooksUpdate(final WebhooksUpdate webhooksUpdate) {
        this.webhooksUpdate = webhooksUpdate;
    }
}
