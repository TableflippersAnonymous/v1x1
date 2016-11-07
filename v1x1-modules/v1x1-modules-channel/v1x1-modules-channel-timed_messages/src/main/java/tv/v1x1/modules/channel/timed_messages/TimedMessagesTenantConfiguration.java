package tv.v1x1.modules.channel.timed_messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.config.ComplexType;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.ModuleConfig;
import tv.v1x1.common.config.Type;
import tv.v1x1.common.modules.BasicTenantConfiguration;

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
    private Map<String, Timer> timers;

    Map<String, Timer> getTimers() {
        return timers;
    }

    Timer getTimer(final String name) {
        return timers.get(name);
    }

    boolean addTimer(final String name, final Timer timer) {
        if(timers.get(name) == null) {
            timers.putIfAbsent(name, timer);
            return true;
        }
        return false;
    }

    boolean delTimer(final String name) {
        return (timers.remove(name) != null);
    }
}
