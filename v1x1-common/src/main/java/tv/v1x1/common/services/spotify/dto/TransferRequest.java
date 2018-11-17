package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferRequest {
    @JsonProperty("device_ids")
    private List<String> deviceIds;
    @JsonProperty
    private boolean play;

    public TransferRequest() {
    }

    public TransferRequest(final String deviceId) {
        this.deviceIds = ImmutableList.of(deviceId);
    }

    public TransferRequest(final String deviceId, final boolean play) {
        this(deviceId);
        this.play = play;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(final List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(final boolean play) {
        this.play = play;
    }
}
