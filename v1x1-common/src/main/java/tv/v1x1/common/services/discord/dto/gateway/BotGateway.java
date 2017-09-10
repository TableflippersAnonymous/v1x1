package tv.v1x1.common.services.discord.dto.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/17/2017.
 */
public class BotGateway extends Gateway {
    @JsonProperty
    private int shards;

    public BotGateway() {
    }

    public BotGateway(final String url, final int shards) {
        super(url);
        this.shards = shards;
    }

    public int getShards() {
        return shards;
    }

    public void setShards(final int shards) {
        this.shards = shards;
    }
}
