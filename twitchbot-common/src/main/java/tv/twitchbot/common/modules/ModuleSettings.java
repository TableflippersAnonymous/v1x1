package tv.twitchbot.common.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.redisson.config.Config;

/**
 * Created by cobi on 10/6/16.
 */
public class ModuleSettings {
    private Config redissonConfig;

    @JsonProperty("redisson")
    public Config getRedissonConfig() {
        return redissonConfig;
    }

    @JsonProperty("redisson")
    public void setRedissonConfig(Config redissonConfig) {
        this.redissonConfig = redissonConfig;
    }
}
