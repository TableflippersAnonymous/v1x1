package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCommunityRequest {
    @JsonProperty
    private String summary;
    @JsonProperty
    private String description;
    @JsonProperty
    private String rules;
    @JsonProperty
    private String email;

    public UpdateCommunityRequest() {
    }

    public UpdateCommunityRequest(final String summary, final String description, final String rules, final String email) {
        this.summary = summary;
        this.description = description;
        this.rules = rules;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
