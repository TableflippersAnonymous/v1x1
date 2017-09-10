package tv.v1x1.common.services.discord.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.channel.Embed;

import java.util.List;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecuteWebhookRequest {
    @JsonProperty
    private String content;
    @JsonProperty
    private String username;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty
    private Boolean tts;
    @JsonProperty
    private List<Embed> embeds;

    public ExecuteWebhookRequest() {
    }

    public ExecuteWebhookRequest(final String content) {
        this.content = content;
    }

    public ExecuteWebhookRequest(final String content, final String username) {
        this.content = content;
        this.username = username;
    }

    public ExecuteWebhookRequest(final String content, final String username, final String avatarUrl) {
        this.content = content;
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    public ExecuteWebhookRequest(final List<Embed> embeds) {
        this.embeds = embeds;
    }

    public ExecuteWebhookRequest(final String username, final String avatarUrl, final List<Embed> embeds) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.embeds = embeds;
    }

    public ExecuteWebhookRequest(final String content, final String username, final String avatarUrl, final Boolean tts,
                                 final List<Embed> embeds) {
        this.content = content;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.tts = tts;
        this.embeds = embeds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(final String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Boolean getTts() {
        return tts;
    }

    public void setTts(final Boolean tts) {
        this.tts = tts;
    }

    public List<Embed> getEmbeds() {
        return embeds;
    }

    public void setEmbeds(final List<Embed> embeds) {
        this.embeds = embeds;
    }
}
