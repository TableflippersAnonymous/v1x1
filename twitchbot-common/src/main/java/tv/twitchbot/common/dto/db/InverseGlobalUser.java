package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 10/16/2016.
 */
@Table(name = "inverse_global_user")
public class InverseGlobalUser {
    @PartitionKey(0)
    private Platform platform;
    @PartitionKey(1)
    @Column(name = "user_id")
    private String userId;
    @Column(name = "global_user_id")
    private UUID globalUserId;

    public InverseGlobalUser() {
    }

    public InverseGlobalUser(final Platform platform, final String userId, final UUID globalUserId) {
        this.platform = platform;
        this.userId = userId;
        this.globalUserId = globalUserId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getUserId() {
        return userId;
    }

    public UUID getGlobalUserId() {
        return globalUserId;
    }
}
