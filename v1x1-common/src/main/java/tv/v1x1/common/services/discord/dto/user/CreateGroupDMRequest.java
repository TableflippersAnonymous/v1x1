package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by naomi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGroupDMRequest {
    @JsonProperty("access_tokens")
    private List<String> accessTokens;
    @JsonProperty("nicks")
    private Map<String, String> userIdsToNicknames;

    public CreateGroupDMRequest() {
    }

    public CreateGroupDMRequest(final List<String> accessTokens, final Map<String, String> userIdsToNicknames) {
        this.accessTokens = accessTokens;
        this.userIdsToNicknames = userIdsToNicknames;
    }

    public List<String> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(final List<String> accessTokens) {
        this.accessTokens = accessTokens;
    }

    public Map<String, String> getUserIdsToNicknames() {
        return userIdsToNicknames;
    }

    public void setUserIdsToNicknames(final Map<String, String> userIdsToNicknames) {
        this.userIdsToNicknames = userIdsToNicknames;
    }
}
