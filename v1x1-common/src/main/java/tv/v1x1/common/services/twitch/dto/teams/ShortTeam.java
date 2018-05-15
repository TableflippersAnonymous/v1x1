package tv.v1x1.common.services.twitch.dto.teams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortTeam {
    @JsonProperty("_id")
    private long id;
    @JsonProperty
    private String background;
    @JsonProperty
    private String banner;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty
    private String info;
    @JsonProperty
    private String logo;
    @JsonProperty
    private String name;
    @JsonProperty("updated_at")
    private String updatedAt;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(final String info) {
        this.info = info;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
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

    public String getBanner() {
        return banner;
    }

    public void setBanner(final String banner) {
        this.banner = banner;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(final String background) {
        this.background = background;
    }
}
