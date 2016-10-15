package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 10/15/2016.
 */
@Table(name = "DiscordChannel")
public class DiscordChannel extends Channel {
    public DiscordChannel(String name, UUID tenantId) {
        super(name, tenantId);
    }

    @Override
    public tv.twitchbot.common.dto.core.DiscordChannel toCore(TenantAccessor accessor) {
        return new tv.twitchbot.common.dto.core.DiscordChannel(getName(), accessor.getById(getTenantId()).toCore());
    }
}
