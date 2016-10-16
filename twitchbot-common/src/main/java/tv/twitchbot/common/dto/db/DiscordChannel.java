package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 10/15/2016.
 */
@Table(name = "discord_channel")
public class DiscordChannel extends Channel {
    public DiscordChannel(String id, String displayName, UUID tenantId) {
        super(id, displayName, tenantId);
    }

    @Override
    public tv.twitchbot.common.dto.core.DiscordChannel toCore(TenantAccessor accessor) {
        return new tv.twitchbot.common.dto.core.DiscordChannel(getId(), accessor.getById(getTenantId()).toCore(), getDisplayName());
    }
}
