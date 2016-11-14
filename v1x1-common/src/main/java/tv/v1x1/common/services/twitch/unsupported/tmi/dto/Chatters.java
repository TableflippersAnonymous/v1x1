package tv.v1x1.common.services.twitch.unsupported.tmi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 11/13/2016.
 */
public class Chatters {
    @JsonProperty
    private List<String> moderators;
    @JsonProperty
    private List<String> staff;
    @JsonProperty
    private List<String> admins;
    @JsonProperty("global_mods")
    private List<String> globalMods;
    @JsonProperty
    private List<String> viewers;

    public Chatters() {
    }

    public List<String> getModerators() {
        return moderators;
    }

    public void setModerators(final List<String> moderators) {
        this.moderators = moderators;
    }

    public List<String> getStaff() {
        return staff;
    }

    public void setStaff(final List<String> staff) {
        this.staff = staff;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(final List<String> admins) {
        this.admins = admins;
    }

    public List<String> getGlobalMods() {
        return globalMods;
    }

    public void setGlobalMods(final List<String> globalMods) {
        this.globalMods = globalMods;
    }

    public List<String> getViewers() {
        return viewers;
    }

    public void setViewers(final List<String> viewers) {
        this.viewers = viewers;
    }
}
