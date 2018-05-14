package tv.v1x1.modules.channel.timed_messages.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.modules.BasicUserConfiguration;
import tv.v1x1.common.scanners.config.ComplexType;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.ModuleConfig;
import tv.v1x1.common.scanners.config.Type;
import tv.v1x1.common.scanners.config.Version;
import tv.v1x1.modules.channel.timed_messages.Timer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Josh
 */
@ModuleConfig("timed_messages")
@DisplayName("Timers")
@Description("This module controls repeating messages to a channel -- timers, if you will")
@Version(0)
public class TimedMessagesUserConfiguration extends BasicUserConfiguration {
    @DisplayName("Timers")
    @Description("List of rotations you have configured")
    @Type(ConfigType.COMPLEX_STRING_MAP)
    @ComplexType(Timer.class)
    @JsonProperty("timers")
    private final Map<String, Timer> timers = new HashMap<>();

    public Map<String, Timer> getTimers() {
        return timers;
    }

    public Timer getTimer(final String name) {
        return timers.get(name);
    }

    public boolean addTimer(final String name, final Timer timer) {
        if(timers.get(name) == null) {
            timers.put(name, timer);
            return true;
        }
        return false;
    }

    public boolean delTimer(final String name) {
        return (timers.remove(name) != null);
    }
}
