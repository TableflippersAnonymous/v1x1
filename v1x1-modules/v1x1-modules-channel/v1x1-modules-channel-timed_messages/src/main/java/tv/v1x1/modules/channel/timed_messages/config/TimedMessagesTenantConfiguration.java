package tv.v1x1.modules.channel.timed_messages.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.config.ComplexType;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.ModuleConfig;
import tv.v1x1.common.config.Type;
import tv.v1x1.common.modules.BasicTenantConfiguration;
import tv.v1x1.modules.channel.timed_messages.Timer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Josh
 */
@ModuleConfig("timed_messages")
@DisplayName("Timers")
@Description("This module controls repeating messages to a channel -- timers, if you will")
public class TimedMessagesTenantConfiguration extends BasicTenantConfiguration {
    @DisplayName("Timers")
    @Description("List of rotations you have configured")
    @Type(ConfigType.COMPLEX_STRING_MAP)
    @ComplexType(Timer.class)
    @JsonProperty("timers")
    private Map<String, Timer> timers = new HashMap<>();

    public Map<String, Timer> getTimers() {
        return timers;
    }

    public Timer getTimer(final String name) {
        return timers.get(name);
    }

    public boolean addTimer(final String name, final Timer timer) {
        if(timers.get(name) == null) {
            timers.putIfAbsent(name, timer);
            return true;
        }
        return false;
    }

    public boolean delTimer(final String name) {
        return (timers.remove(name) != null);
    }
}
