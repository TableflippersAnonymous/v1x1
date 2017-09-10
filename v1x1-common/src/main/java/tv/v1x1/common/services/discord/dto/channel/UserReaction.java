package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.emoji.Emoji;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserReaction {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("message_id")
    private String messageId;
    @JsonProperty
    private Emoji emoji;

    public UserReaction() {
    }

    public UserReaction(final String userId, final String channelId, final String messageId, final Emoji emoji) {
        this.userId = userId;
        this.channelId = channelId;
        this.messageId = messageId;
        this.emoji = emoji;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public void setEmoji(final Emoji emoji) {
        this.emoji = emoji;
    }
}
