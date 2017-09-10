package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.permissions.Role;
import tv.v1x1.common.services.discord.dto.user.User;

import java.util.List;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    @JsonProperty
    private String id;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty
    private User author;
    @JsonProperty
    private String content;
    @JsonProperty
    private String timestamp;
    @JsonProperty("edited_timestamp")
    private String editedTimestamp;
    @JsonProperty
    private boolean tts;
    @JsonProperty("mention_everyone")
    private boolean mentionEveryone;
    @JsonProperty
    private List<User> mentions;
    @JsonProperty("mention_roles")
    private List<Role> mentionRoles;
    @JsonProperty
    private List<Attachment> attachments;
    @JsonProperty
    private List<Embed> embeds;
    @JsonProperty
    private List<Reaction> reactions;
    @JsonProperty
    private String nonce;
    @JsonProperty
    private boolean pinned;
    @JsonProperty("webhook_id")
    private String webhookId;
    @JsonProperty
    private MessageType type;

    public Message() {
    }

    public Message(final String id, final String channelId, final User author, final String content,
                   final String timestamp, final String editedTimestamp, final boolean tts,
                   final boolean mentionEveryone, final List<User> mentions, final List<Role> mentionRoles,
                   final List<Attachment> attachments, final List<Embed> embeds, final List<Reaction> reactions,
                   final String nonce, final boolean pinned, final String webhookId, final MessageType type) {
        this.id = id;
        this.channelId = channelId;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
        this.editedTimestamp = editedTimestamp;
        this.tts = tts;
        this.mentionEveryone = mentionEveryone;
        this.mentions = mentions;
        this.mentionRoles = mentionRoles;
        this.attachments = attachments;
        this.embeds = embeds;
        this.reactions = reactions;
        this.nonce = nonce;
        this.pinned = pinned;
        this.webhookId = webhookId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(final User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEditedTimestamp() {
        return editedTimestamp;
    }

    public void setEditedTimestamp(final String editedTimestamp) {
        this.editedTimestamp = editedTimestamp;
    }

    public boolean isTts() {
        return tts;
    }

    public void setTts(final boolean tts) {
        this.tts = tts;
    }

    public boolean isMentionEveryone() {
        return mentionEveryone;
    }

    public void setMentionEveryone(final boolean mentionEveryone) {
        this.mentionEveryone = mentionEveryone;
    }

    public List<User> getMentions() {
        return mentions;
    }

    public void setMentions(final List<User> mentions) {
        this.mentions = mentions;
    }

    public List<Role> getMentionRoles() {
        return mentionRoles;
    }

    public void setMentionRoles(final List<Role> mentionRoles) {
        this.mentionRoles = mentionRoles;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Embed> getEmbeds() {
        return embeds;
    }

    public void setEmbeds(final List<Embed> embeds) {
        this.embeds = embeds;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(final List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(final String nonce) {
        this.nonce = nonce;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(final boolean pinned) {
        this.pinned = pinned;
    }

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(final String webhookId) {
        this.webhookId = webhookId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(final MessageType type) {
        this.type = type;
    }
}
