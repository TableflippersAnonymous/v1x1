package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.Inject;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.db.ChannelGroupConfiguration;
import tv.v1x1.common.dto.db.Platform;

import java.util.UUID;

/**
 * Created by naomi on 4/6/2018.
 */
public class DAOChannelGroupConfiguration {
    private final Mapper<ChannelGroupConfiguration> mapper;

    @Inject
    public DAOChannelGroupConfiguration(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(ChannelGroupConfiguration.class);
    }

    public ChannelGroupConfiguration get(final Module module, final ChannelGroup channelGroup) {
        return get(module.getName(), channelGroup.getTenant().getId().getValue(), channelGroup.getPlatform(), channelGroup.getId());
    }

    public ChannelGroupConfiguration get(final String moduleName, final UUID tenantId, final Platform platform, final String id) {
        return mapper.get(moduleName, tenantId, platform, id);
    }

    public void put(final ChannelGroupConfiguration channelGroupConfiguration) {
        mapper.save(channelGroupConfiguration);
    }
}
