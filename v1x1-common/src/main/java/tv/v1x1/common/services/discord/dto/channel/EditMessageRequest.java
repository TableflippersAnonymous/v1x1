package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditMessageRequest {
    @JsonProperty
    private String content;
    @JsonProperty
    private Embed embed;

    public EditMessageRequest() {
    }

    public EditMessageRequest(final String content) {
        this.content = content;
    }

    public EditMessageRequest(final Embed embed) {
        this.embed = embed;
    }

    public EditMessageRequest(final String content, final Embed embed) {
        this.content = content;
        this.embed = embed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Embed getEmbed() {
        return embed;
    }

    public void setEmbed(final Embed embed) {
        this.embed = embed;
    }
}
