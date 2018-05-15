package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import tv.v1x1.common.dao.DAOTenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/15/2016.
 */
@Table(name = "tenant_v2")
public class Tenant {
    @PartitionKey
    private UUID id;
    @Column(name = "display_name")
    private String displayName;

    public Tenant() {
    }

    public Tenant(final UUID id, final String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public tv.v1x1.common.dto.core.Tenant toCore(final DAOTenant daoTenant) {
        final List<tv.v1x1.common.dto.core.ChannelGroup> channelGroups = new ArrayList<>();
        final tv.v1x1.common.dto.core.Tenant tenant = new tv.v1x1.common.dto.core.Tenant(
                new tv.v1x1.common.dto.core.UUID(id),
                displayName,
                channelGroups
        );
        final Set<ChannelGroup> entries = daoTenant.getChannelGroups(this);
        channelGroups.addAll(entries.stream().map(entry -> entry.toCore(tenant, daoTenant)).collect(Collectors.toList()));
        return tenant;
    }
}
