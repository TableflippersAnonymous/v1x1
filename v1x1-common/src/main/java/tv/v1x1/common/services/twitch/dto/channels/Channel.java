package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Naomi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel extends ShortChannel {
    @JsonProperty("broadcaster_language")
    private String broadcasterLanguage;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty
    private long followers;
    @JsonProperty
    private String game;
    @JsonProperty
    private String language;
    @JsonProperty
    private String logo;
    @JsonProperty
    private boolean mature;
    @JsonProperty
    private boolean partner;
    @JsonProperty("profile_banner")
    private String profileBanner;
    @JsonProperty("profile_banner_background_color")
    private String profileBannerBackgroundColor;
    @JsonProperty
    private String status;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty
    private String url;
    @JsonProperty("video_banner")
    private String videoBanner;
    @JsonProperty
    private long views;

    public Channel() {
    }

    public boolean isMature() {
        return mature;
    }

    public void setMature(final boolean mature) {
        this.mature = mature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getBroadcasterLanguage() {
        return broadcasterLanguage;
    }

    public void setBroadcasterLanguage(final String broadcasterLanguage) {
        this.broadcasterLanguage = broadcasterLanguage;
    }

    public String getGame() {
        return game;
    }

    public void setGame(final String game) {
        this.game = game;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(final String logo) {
        this.logo = logo;
    }

    public String getVideoBanner() {
        return videoBanner;
    }

    public void setVideoBanner(final String videoBanner) {
        this.videoBanner = videoBanner;
    }

    public String getProfileBanner() {
        return profileBanner;
    }

    public void setProfileBanner(final String profileBanner) {
        this.profileBanner = profileBanner;
    }

    public String getProfileBannerBackgroundColor() {
        return profileBannerBackgroundColor;
    }

    public void setProfileBannerBackgroundColor(final String profileBannerBackgroundColor) {
        this.profileBannerBackgroundColor = profileBannerBackgroundColor;
    }

    public boolean isPartner() {
        return partner;
    }

    public void setPartner(final boolean partner) {
        this.partner = partner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public long getViews() {
        return views;
    }

    public void setViews(final long views) {
        this.views = views;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(final long followers) {
        this.followers = followers;
    }
}
