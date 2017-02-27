package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityId {
    @JsonProperty("_id")
    private String id;

    public CommunityId() {
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
