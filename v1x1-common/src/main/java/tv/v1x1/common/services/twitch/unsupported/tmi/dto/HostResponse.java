package tv.v1x1.common.services.twitch.unsupported.tmi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 11/13/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HostResponse {
    @JsonProperty
    private List<Host> hosts;

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(final List<Host> hosts) {
        this.hosts = hosts;
    }
}
