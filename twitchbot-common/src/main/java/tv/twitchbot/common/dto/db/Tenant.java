package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 10/15/2016.
 */
@Table(name = "Tenant")
public class Tenant {
    @PartitionKey
    private UUID id;

    public Tenant(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public tv.twitchbot.common.dto.core.Tenant toCore() {
        return new tv.twitchbot.common.dto.core.Tenant(new tv.twitchbot.common.dto.core.UUID(id));
    }
}
