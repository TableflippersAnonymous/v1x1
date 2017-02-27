package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Community extends CommunityId {
    @JsonProperty("avatar_image_url")
    private String avatarImageUrl;
    @JsonProperty("cover_image_url")
    private String coverImageUrl;
    @JsonProperty
    private String description;
    @JsonProperty("description_html")
    private String descriptionHtml;
    @JsonProperty
    private String language;
    @JsonProperty
    private String name;
    @JsonProperty("owner_id")
    private String ownerId;
    @JsonProperty
    private String rules;
    @JsonProperty("rules_html")
    private String rulesHtml;
    @JsonProperty
    private String summary;

    public Community() {
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public void setAvatarImageUrl(final String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(final String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    public void setDescriptionHtml(final String descriptionHtml) {
        this.descriptionHtml = descriptionHtml;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(final String rules) {
        this.rules = rules;
    }

    public String getRulesHtml() {
        return rulesHtml;
    }

    public void setRulesHtml(final String rulesHtml) {
        this.rulesHtml = rulesHtml;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }
}
