package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/29/2016.
 */
public class BadgeList {
    @JsonProperty("global_mod")
    private Badge globalMod;
    @JsonProperty
    private Badge admin;
    @JsonProperty
    private Badge broadcaster;
    @JsonProperty
    private Badge mod;
    @JsonProperty
    private Badge staff;
    @JsonProperty
    private Badge turbo;
    @JsonProperty
    private Badge subscriber;

    public BadgeList() {
    }

    public Badge getGlobalMod() {
        return globalMod;
    }

    public void setGlobalMod(final Badge globalMod) {
        this.globalMod = globalMod;
    }

    public Badge getAdmin() {
        return admin;
    }

    public void setAdmin(final Badge admin) {
        this.admin = admin;
    }

    public Badge getBroadcaster() {
        return broadcaster;
    }

    public void setBroadcaster(final Badge broadcaster) {
        this.broadcaster = broadcaster;
    }

    public Badge getMod() {
        return mod;
    }

    public void setMod(final Badge mod) {
        this.mod = mod;
    }

    public Badge getStaff() {
        return staff;
    }

    public void setStaff(final Badge staff) {
        this.staff = staff;
    }

    public Badge getTurbo() {
        return turbo;
    }

    public void setTurbo(final Badge turbo) {
        this.turbo = turbo;
    }

    public Badge getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(final Badge subscriber) {
        this.subscriber = subscriber;
    }
}
