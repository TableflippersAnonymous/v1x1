package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Devices {
    @JsonProperty
    private List<Device> devices;

    public Devices() {
    }

    public Devices(final List<Device> devices) {
        this.devices = devices;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(final List<Device> devices) {
        this.devices = devices;
    }
}
