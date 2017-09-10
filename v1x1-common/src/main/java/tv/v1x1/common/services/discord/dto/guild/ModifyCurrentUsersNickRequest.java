package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyCurrentUsersNickRequest {
    @JsonProperty
    private String nick;

    public ModifyCurrentUsersNickRequest() {
    }

    public ModifyCurrentUsersNickRequest(final String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(final String nick) {
        this.nick = nick;
    }
}
