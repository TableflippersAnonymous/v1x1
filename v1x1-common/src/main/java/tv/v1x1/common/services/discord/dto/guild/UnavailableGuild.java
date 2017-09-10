package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnavailableGuild {
    @JsonProperty
    private String id;
    @JsonProperty
    private boolean unavailable;

    public UnavailableGuild() {
    }

    public UnavailableGuild(final String id, final boolean unavailable) {
        this.id = id;
        this.unavailable = unavailable;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public boolean isUnavailable() {
        return unavailable;
    }

    public void setUnavailable(final boolean unavailable) {
        this.unavailable = unavailable;
    }
}
