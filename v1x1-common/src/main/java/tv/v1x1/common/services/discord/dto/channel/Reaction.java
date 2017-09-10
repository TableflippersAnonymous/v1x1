package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.emoji.Emoji;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reaction {
    @JsonProperty
    private Integer count;
    @JsonProperty
    private boolean me;
    @JsonProperty
    private Emoji emoji;

    public Reaction() {
    }

    public Reaction(final Integer count, final boolean me, final Emoji emoji) {
        this.count = count;
        this.me = me;
        this.emoji = emoji;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(final boolean me) {
        this.me = me;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public void setEmoji(final Emoji emoji) {
        this.emoji = emoji;
    }
}
