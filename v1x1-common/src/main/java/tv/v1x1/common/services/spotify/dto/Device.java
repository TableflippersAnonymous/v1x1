package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {
    @JsonProperty
    private String id;
    @JsonProperty("is_active")
    private boolean isActive;
    @JsonProperty("is_private_session")
    private boolean isPrivateSession;
    @JsonProperty("is_restricted")
    private boolean isRestricted;
    @JsonProperty
    private String name;
    @JsonProperty
    private String type;
    @JsonProperty("volume_percent")
    private long volumePercent;

    public Device() {
    }

    public Device(final String id, final boolean isActive, final boolean isPrivateSession, final boolean isRestricted,
                  final String name, final String type, final long volumePercent) {
        this.id = id;
        this.isActive = isActive;
        this.isPrivateSession = isPrivateSession;
        this.isRestricted = isRestricted;
        this.name = name;
        this.type = type;
        this.volumePercent = volumePercent;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean active) {
        isActive = active;
    }

    public boolean isPrivateSession() {
        return isPrivateSession;
    }

    public void setPrivateSession(final boolean privateSession) {
        isPrivateSession = privateSession;
    }

    public boolean isRestricted() {
        return isRestricted;
    }

    public void setRestricted(final boolean restricted) {
        isRestricted = restricted;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public long getVolumePercent() {
        return volumePercent;
    }

    public void setVolumePercent(final long volumePercent) {
        this.volumePercent = volumePercent;
    }
}
