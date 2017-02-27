package tv.v1x1.common.services.twitch.dto.emotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmoticonImage {
    @JsonProperty("emoticon_set")
    private Integer emoticonSet;
    @JsonProperty
    private String code;
    @JsonProperty
    private long id;

    public EmoticonImage() {
    }

    public Integer getEmoticonSet() {
        return emoticonSet;
    }

    public void setEmoticonSet(final Integer emoticonSet) {
        this.emoticonSet = emoticonSet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
