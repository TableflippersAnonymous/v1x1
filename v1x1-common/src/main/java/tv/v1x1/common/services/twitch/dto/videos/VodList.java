package tv.v1x1.common.services.twitch.dto.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VodList {
    @JsonProperty
    private List<Video> vods;

    public VodList() {
    }

    public List<Video> getVods() {
        return vods;
    }

    public void setVods(final List<Video> vods) {
        this.vods = vods;
    }
}
