package tv.v1x1.common.services.slack.dto.bots;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.misc.IconSet;

public class BotInfo {
    @JsonProperty
    private String id;
    @JsonProperty
    private Boolean deleted;
    @JsonProperty
    private String name;
    @JsonProperty
    private Long updated;
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty
    private IconSet icons;

    public BotInfo() {
    }

    public BotInfo(final String id, final Boolean deleted, final String name, final Long updated, final String appId, final IconSet icons) {
        this.id = id;
        this.deleted = deleted;
        this.name = name;
        this.updated = updated;
        this.appId = appId;
        this.icons = icons;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(final Long updated) {
        this.updated = updated;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(final String appId) {
        this.appId = appId;
    }

    public IconSet getIcons() {
        return icons;
    }

    public void setIcons(final IconSet icons) {
        this.icons = icons;
    }
}
