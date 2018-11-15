package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendationSeed {
    @JsonProperty("after_filtering_size")
    private int afterFilteringSize;
    @JsonProperty("after_relinking_size")
    private int afterRelinkingSize;
    @JsonProperty
    private String href;
    @JsonProperty
    private String id;
    @JsonProperty("initial_pool_size")
    private int initialPoolSize;
    @JsonProperty
    private String type;

    public RecommendationSeed() {
    }

    public RecommendationSeed(final int afterFilteringSize, final int afterRelinkingSize, final String href,
                              final String id, final int initialPoolSize, final String type) {
        this.afterFilteringSize = afterFilteringSize;
        this.afterRelinkingSize = afterRelinkingSize;
        this.href = href;
        this.id = id;
        this.initialPoolSize = initialPoolSize;
        this.type = type;
    }

    public int getAfterFilteringSize() {
        return afterFilteringSize;
    }

    public void setAfterFilteringSize(final int afterFilteringSize) {
        this.afterFilteringSize = afterFilteringSize;
    }

    public int getAfterRelinkingSize() {
        return afterRelinkingSize;
    }

    public void setAfterRelinkingSize(final int afterRelinkingSize) {
        this.afterRelinkingSize = afterRelinkingSize;
    }

    public String getHref() {
        return href;
    }

    public void setHref(final String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public void setInitialPoolSize(final int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
