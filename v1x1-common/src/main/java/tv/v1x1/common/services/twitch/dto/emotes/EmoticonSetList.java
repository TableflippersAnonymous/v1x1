package tv.v1x1.common.services.twitch.dto.emotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by naomi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmoticonSetList {
    @JsonProperty("emoticon_sets")
    private Map<String, List<EmoticonImage>> emoticonSets;

    public EmoticonSetList() {
    }

    public Map<String, List<EmoticonImage>> getEmoticonSets() {
        return emoticonSets;
    }

    public void setEmoticonSets(final Map<String, List<EmoticonImage>> emoticonSets) {
        this.emoticonSets = emoticonSets;
    }
}
