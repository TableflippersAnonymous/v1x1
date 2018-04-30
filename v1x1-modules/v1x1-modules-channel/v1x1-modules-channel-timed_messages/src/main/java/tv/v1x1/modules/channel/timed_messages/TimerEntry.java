package tv.v1x1.modules.channel.timed_messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.Type;

/**
 * @author Josh
 */
public class TimerEntry {
    @JsonProperty("message")
    @DisplayName("Message")
    @Type(ConfigType.STRING)
    private String message;

    public TimerEntry() {
        // For Jackson
    }

    public TimerEntry(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
