package tv.v1x1.common.services.twitch.dto.ingests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingest {
    @JsonProperty
    private String name;
    @JsonProperty("default")
    private boolean defaultIngest;
    @JsonProperty("_id")
    private long id;
    @JsonProperty("url_template")
    private String urlTemplate;
    @JsonProperty
    private double availability;

    public Ingest() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isDefaultIngest() {
        return defaultIngest;
    }

    public void setDefaultIngest(final boolean defaultIngest) {
        this.defaultIngest = defaultIngest;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(final String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    public double getAvailability() {
        return availability;
    }

    public void setAvailability(final double availability) {
        this.availability = availability;
    }
}
