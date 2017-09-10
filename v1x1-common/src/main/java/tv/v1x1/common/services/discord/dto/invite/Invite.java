package tv.v1x1.common.services.discord.dto.invite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.channel.PartialChannel;
import tv.v1x1.common.services.discord.dto.guild.PartialGuild;

/**
 * Created by naomi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invite {
    @JsonProperty
    private String code;
    @JsonProperty
    private PartialGuild guild;
    @JsonProperty
    private PartialChannel channel;

    public Invite() {
    }

    public Invite(final String code, final PartialGuild guild, final PartialChannel channel) {
        this.code = code;
        this.guild = guild;
        this.channel = channel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public PartialGuild getGuild() {
        return guild;
    }

    public void setGuild(final PartialGuild guild) {
        this.guild = guild;
    }

    public PartialChannel getChannel() {
        return channel;
    }

    public void setChannel(final PartialChannel channel) {
        this.channel = channel;
    }
}
