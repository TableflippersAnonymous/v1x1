package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Categories {
    @JsonProperty
    private Paging<Category> categories;

    public Categories() {
    }

    public Categories(final Paging<Category> categories) {
        this.categories = categories;
    }

    public Paging<Category> getCategories() {
        return categories;
    }

    public void setCategories(final Paging<Category> categories) {
        this.categories = categories;
    }
}
