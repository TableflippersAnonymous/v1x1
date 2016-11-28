package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Created by jcarter on 11/27/16.
 */
@Table(name = "joined_twitch_channel")
public class JoinedTwitchChannel {
    @PartitionKey
    private String channel;

    public JoinedTwitchChannel() {
    }

    public JoinedTwitchChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
