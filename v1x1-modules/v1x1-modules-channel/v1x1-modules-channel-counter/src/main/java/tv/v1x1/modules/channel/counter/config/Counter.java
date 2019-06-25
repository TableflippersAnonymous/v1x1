package tv.v1x1.modules.channel.counter.config;

import org.codehaus.jackson.annotate.JsonProperty;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.Type;

/**
 * Created by Josh on 2019-06-24
 */
public class Counter {
    @JsonProperty("count")
    @DisplayName("Current count")
    @Description("The current count saved")
    @Type(ConfigType.INTEGER)
    private int count;
    @JsonProperty("incMessage")
    @DisplayName("Add Message")
    @Description("Optional. The message to send on increment. Use %target% and %count% as variables.")
    @Type(ConfigType.STRING)
    private String incMessage;
    @JsonProperty("decMessage")
    @DisplayName("Subtract Message")
    @Description("Optional. The message to send on decrement. Use %target% and %count% as variables.")
    @Type(ConfigType.STRING)
    private String decMessage;

    public Counter() {
        // Intentionally empty
    }

    public Counter(final int count, final String incMessage, final String decMessage) {
        this.count = count;
        this.incMessage = incMessage;
        this.decMessage = decMessage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public void inc() {
        ++this.count;
    }

    public void dec() {
        --this.count;
    }

    public String getIncMessage() {
        return incMessage;
    }

    public void setIncMessage(final String incMessage) {
        this.incMessage = incMessage;
    }

    public String getDecMessage() {
        return decMessage;
    }

    public void setDecMessage(final String decMessage) {
        this.decMessage = decMessage;
    }
}
