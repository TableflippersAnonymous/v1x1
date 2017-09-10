package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupDmAddRecipientRequest {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty
    private String nick;

    public GroupDmAddRecipientRequest() {
    }

    public GroupDmAddRecipientRequest(final String accessToken, final String nick) {
        this.accessToken = accessToken;
        this.nick = nick;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(final String nick) {
        this.nick = nick;
    }
}
