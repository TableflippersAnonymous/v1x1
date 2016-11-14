package tv.v1x1.modules.channel.timed_messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import tv.v1x1.common.config.ComplexType;
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
    @Type(ConfigType.COMPLEX_LIST)
    @ComplexType(TimerEntry.class)
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = UUIDSerializer.class)
    @JsonDeserialize(using = UUIDDeserializer.class)
    @TenantPermission(Permission.NONE)
    private java.util.UUID activeTimer;

    public Timer() {
        // For Jackson
    }

    public Timer(final long interval) {
        this.interval = interval;
        this.entries = new ArrayList<>();
        this.alwaysOn = true;
    }

    public List<TimerEntry> getEntries() {
        return entries;
    }

    public long getInterval() {
        return interval;
    }

    public TimerEntry getEntry(int cursor) {
        if(entries.size() < 1)
            return null;
        return entries.get(cursor);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(final boolean enabled) {
        isEnabled = enabled;
    }

    public java.util.UUID getActiveTimer() {
        return activeTimer;
    }

    public void setActiveTimer(final java.util.UUID activeTimer) {
        this.activeTimer = activeTimer;
    }

    public boolean isAlwaysOn() {
        return alwaysOn;
    }

    public void setAlwaysOn(final boolean alwaysOn) {
        this.alwaysOn = alwaysOn;
    }

    @JsonIgnore
    public UUID getDtoActiveTimer() {
        return new UUID(activeTimer);
    }
}
