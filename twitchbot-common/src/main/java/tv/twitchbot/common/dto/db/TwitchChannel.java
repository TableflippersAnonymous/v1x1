package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 10/15/2016.
 */
@Table(name = "twitch_channel")
public class TwitchChannel extends Channel {
    public TwitchChannel() {
    }

    public TwitchChannel(final String id, final String displayName, final UUID tenantId) {
        super(id, displayName, tenantId);
    }

    @Override
    public tv.twitchbot.common.dto.core.TwitchChannel toCore(final TenantAccessor accessor) {
        return new tv.twitchbot.common.dto.core.TwitchChannel(getId(), accessor.getById(getTenantId()).toCore(), getDisplayName());
    }
}
