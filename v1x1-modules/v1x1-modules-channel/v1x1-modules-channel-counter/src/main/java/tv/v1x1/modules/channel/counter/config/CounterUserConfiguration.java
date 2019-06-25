package tv.v1x1.modules.channel.counter.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import tv.v1x1.common.modules.BasicUserConfiguration;
import tv.v1x1.common.scanners.config.ComplexType;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.ModuleConfig;
import tv.v1x1.common.scanners.config.Type;
import tv.v1x1.common.scanners.config.Version;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Josh on 2019-06-24.
 */
@ModuleConfig("counter")
@DisplayName("Counter")
@Description("This module allows you keep counters that you can set; for example, death counters")
@Version(4)
public class CounterUserConfiguration extends BasicUserConfiguration {
    @DisplayName("Geek Mode")
    @Description("Allow counters to be changed using C-like syntax; ie \"deaths++\"")
    @Type(ConfigType.BOOLEAN)
    private boolean geekMode;
    @DisplayName("Counters")
    @Description("Valid counters")
    @Type(ConfigType.COMPLEX_STRING_MAP)
    @ComplexType(Counter.class)
    @JsonProperty("counters")
    private Map<String, Counter> counters = new HashMap<>();

    public boolean isGeekMode() {
        return geekMode;
    }

    public void setGeekMode(final boolean geekMode) {
        this.geekMode = geekMode;
    }

    public void addCounter(final String id, final Counter counter) {
        counters.put(id, counter);
    }

    public boolean delCounter(final String id) {
        return counters.remove(id) != null;
    }

    public Counter getCounter(final String id) {
        return counters.get(id);
    }

    public Map<String, Counter> getCounters() {
        return ImmutableMap.copyOf(counters);
    }

}
