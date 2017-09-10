package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMessageRequest {
    @JsonProperty
    private String content;
    @JsonProperty
    private String nonce;
    @JsonProperty
    private boolean tts;
    @JsonProperty
    private Embed embed;

    public CreateMessageRequest() {
    }

    public CreateMessageRequest(final String content) {
        this.content = content;
    }

    public CreateMessageRequest(final String content, final String nonce) {
        this.content = content;
        this.nonce = nonce;
    }

    public CreateMessageRequest(final String content, final String nonce, final boolean tts, final Embed embed) {
        this.content = content;
        this.nonce = nonce;
        this.tts = tts;
        this.embed = embed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(final String nonce) {
        this.nonce = nonce;
    }

    public boolean isTts() {
        return tts;
    }

    public void setTts(final boolean tts) {
        this.tts = tts;
    }

    public Embed getEmbed() {
        return embed;
    }

    public void setEmbed(final Embed embed) {
        this.embed = embed;
    }
}
