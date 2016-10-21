package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 10/15/2016.
 */
@Table(name = "discord_channel")
public class DiscordChannel extends Channel {
    public DiscordChannel(final String id, final String displayName, final UUID tenantId) {
        super(id, displayName, tenantId);
    }

    public DiscordChannel() {
    }

    @Override
    public tv.twitchbot.common.dto.core.DiscordChannel toCore(final TenantAccessor accessor) {
        return new tv.twitchbot.common.dto.core.DiscordChannel(getId(), accessor.getById(getTenantId()).toCore(), getDisplayName());
    }
}
