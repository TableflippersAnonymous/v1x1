package tv.v1x1.modules.channel.timed_messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.Permission;
import tv.v1x1.common.config.TenantPermission;
import tv.v1x1.common.config.Type;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.proto.core.UUIDOuterClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Josh
 */
public class Timer {
    @JsonProperty("entries")
    @DisplayName("Timer Entries")
    @Description("The list of messages this rotation will feature")
    @Type(ConfigType.COMPLEX)
    private List<TimerEntry> entries;
    @JsonProperty("interval")
    @DisplayName("Interval")
    @Description("Time (in milliseconds) between messages")
    private long interval;
    @JsonProperty("enabled")
    @DisplayName("Enabled?")
    @Description("Whether or not this rotation will run")
    private boolean isEnabled;
    @DisplayName("Always On")
    @Type(ConfigType.BOOLEAN)
    @Description("Should this rotation run while you're offline?")
    @JsonProperty("alwayson") // #okta
    private boolean alwaysOn;
    @JsonProperty("active_uuid")
    @TenantPermission(Permission.NONE)
    private java.util.UUID activeTimer;

    public Timer(final long interval) {
        this.interval = interval;
        this.entries = new ArrayList<>();
    }

    public List<TimerEntry> getEntries() {
        return entries;
    }

    public long getInterval() {
        return interval;
    }

    public TimerEntry nextEntry(int cursor) {
        if(cursor > entries.size())
            cursor = 0;
        return entries.get(cursor);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(final boolean enabled) {
        isEnabled = enabled;
    }

    public UUID getActiveTimer() {
        return new UUID(activeTimer);
    }

    public boolean isAlwaysOn() {
        return alwaysOn;
    }

    public void setAlwaysOn(final boolean alwaysOn) {
        this.alwaysOn = alwaysOn;
    }
}
