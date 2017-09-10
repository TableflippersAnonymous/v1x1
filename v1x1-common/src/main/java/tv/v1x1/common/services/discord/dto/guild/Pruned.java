package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pruned {
    @JsonProperty
    private Integer pruned;

    public Pruned() {
    }

    public Pruned(final Integer pruned) {
        this.pruned = pruned;
    }

    public Integer getPruned() {
        return pruned;
    }

    public void setPruned(final Integer pruned) {
        this.pruned = pruned;
    }
}
