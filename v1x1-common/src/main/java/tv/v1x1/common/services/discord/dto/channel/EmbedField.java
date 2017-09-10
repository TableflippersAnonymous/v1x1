package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbedField {
    @JsonProperty
    private String name;
    @JsonProperty
    private String value;
    @JsonProperty
    private boolean inline;

    public EmbedField() {
    }

    public EmbedField(final String name, final String value, final boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(final boolean inline) {
        this.inline = inline;
    }
}
