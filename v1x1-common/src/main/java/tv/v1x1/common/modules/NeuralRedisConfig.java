package tv.v1x1.common.modules;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 1/2/2017.
 */
public class NeuralRedisConfig {
    private String redisUri;

    @JsonProperty("redis_uri")
    public String getRedisUri() {
        return redisUri;
    }

    @JsonProperty("redis_uri")
    public void setRedisUri(final String redisUri) {
        this.redisUri = redisUri;
    }
}
