package tv.twitchbot.common.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.redisson.config.Config;

/**
 * Created by cobi on 10/6/16.
 */
public class ModuleSettings {
    private Config redissonConfig;
    private String zookeeperConnectionString;

    @JsonProperty("redisson")
    public Config getRedissonConfig() {
        return redissonConfig;
    }

    @JsonProperty("redisson")
    public void setRedissonConfig(Config redissonConfig) {
        this.redissonConfig = redissonConfig;
    }

    @JsonProperty("zookeeper_connection_string")
    public String getZookeeperConnectionString() {
        return zookeeperConnectionString;
    }

    @JsonProperty("zookeeper_connection_string")
    public void setZookeeperConnectionString(String zookeeperConnectionString) {
        this.zookeeperConnectionString = zookeeperConnectionString;
    }
}
