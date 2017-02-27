package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCommunityRequest {
    @JsonProperty
    private String name;
    @JsonProperty
    private String summary;
    @JsonProperty
    private String description;
    @JsonProperty
    private String rules;

    public CreateCommunityRequest() {
    }

    public CreateCommunityRequest(final String name, final String summary, final String description, final String rules) {
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.rules = rules;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(final String rules) {
        this.rules = rules;
    }
}
