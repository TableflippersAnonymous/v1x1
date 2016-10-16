package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by cobi on 10/15/2016.
 */
@Table(name = "twitch_channel")
public class TwitchChannel extends Channel {
    public TwitchChannel(String id, String displayName, UUID tenantId) {
        super(id, displayName, tenantId);
    }

    @Override
    public tv.twitchbot.common.dto.core.TwitchChannel toCore(TenantAccessor accessor) {
        return new tv.twitchbot.common.dto.core.TwitchChannel(getId(), accessor.getById(getTenantId()).toCore(), getDisplayName());
    }
}
