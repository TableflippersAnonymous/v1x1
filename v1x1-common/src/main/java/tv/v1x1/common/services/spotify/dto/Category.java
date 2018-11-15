package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {
    @JsonProperty
    private String href;
    @JsonProperty
    private List<Image> icons;
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;

    public Category() {
    }

    public Category(final String href, final List<Image> icons, final String id, final String name) {
        this.href = href;
        this.icons = icons;
        this.id = id;
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(final String href) {
        this.href = href;
    }

    public List<Image> getIcons() {
        return icons;
    }

    public void setIcons(final List<Image> icons) {
        this.icons = icons;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
