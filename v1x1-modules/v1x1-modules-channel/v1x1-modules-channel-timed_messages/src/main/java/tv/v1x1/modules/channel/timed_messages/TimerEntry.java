package tv.v1x1.modules.channel.timed_messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.Type;

/**
 * @author Josh
 */
public class TimerEntry {
    @JsonProperty("value")
    @DisplayName("Message")
    @Type(ConfigType.STRING)
    private String value;

    public TimerEntry(final String message) {
        value = message;
    }

    public String getMessage() {
        return value;
    }
}
