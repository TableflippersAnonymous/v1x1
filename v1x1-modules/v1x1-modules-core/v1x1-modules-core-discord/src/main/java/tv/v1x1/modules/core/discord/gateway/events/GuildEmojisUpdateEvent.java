package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.emoji.Emoji;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class GuildEmojisUpdateEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("guild_id")
        private String guildId;
        @JsonProperty
        private List<Emoji> emojis;

        public Data() {
        }

        public Data(final String guildId, final List<Emoji> emojis) {
            this.guildId = guildId;
            this.emojis = emojis;
        }

        public String getGuildId() {
            return guildId;
        }

        public void setGuildId(final String guildId) {
            this.guildId = guildId;
        }

        public List<Emoji> getEmojis() {
            return emojis;
        }

        public void setEmojis(final List<Emoji> emojis) {
            this.emojis = emojis;
        }
    }

    @JsonProperty("d")
    private Data data;

    public GuildEmojisUpdateEvent() {
    }

    public GuildEmojisUpdateEvent(final Long sequenceNumber, final Data data) {
        super(sequenceNumber, "GUILD_EMOJIS_UPDATE");
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(final Data data) {
        this.data = data;
    }
}
