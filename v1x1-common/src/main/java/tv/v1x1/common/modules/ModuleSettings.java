package tv.v1x1.common.modules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.redisson.config.Config;

/**
 * Created by cobi on 10/6/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModuleSettings {
    private Config redissonConfig;
    private String zookeeperConnectionString;
    private CassandraConfig cassandraConfig;
    private ZipkinConfig zipkinConfig;
    private long waitStartMs = 0;

    @JsonProperty("redisson")
    public Config getRedissonConfig() {
        return redissonConfig;
    }

    @JsonProperty("redisson")
    public void setRedissonConfig(final Config redissonConfig) {
        this.redissonConfig = redissonConfig;
    }

    @JsonProperty("zookeeper_connection_string")
    public String getZookeeperConnectionString() {
        return zookeeperConnectionString;
    }

    @JsonProperty("zookeeper_connection_string")
    public void setZookeeperConnectionString(final String zookeeperConnectionString) {
        this.zookeeperConnectionString = zookeeperConnectionString;
    }

    @JsonProperty("cassandra")
    public CassandraConfig getCassandraConfig() {
        return cassandraConfig;
    }

    @JsonProperty("cassandra")
    public void setCassandraConfig(final CassandraConfig cassandraConfig) {
        this.cassandraConfig = cassandraConfig;
    }

    @JsonProperty("wait_start_ms")
    public long getWaitStartMs() {
        return waitStartMs;
    }

    @JsonProperty("wait_start_ms")
    public void setWaitStartMs(final long waitStartMs) {
        this.waitStartMs = waitStartMs;
    }

    @JsonProperty("zipkin_config")
    public ZipkinConfig getZipkinConfig() {
        return zipkinConfig;
    }

    @JsonProperty("zipkin_config")
    public void setZipkinConfig(final ZipkinConfig zipkinConfig) {
        this.zipkinConfig = zipkinConfig;
    }
}
