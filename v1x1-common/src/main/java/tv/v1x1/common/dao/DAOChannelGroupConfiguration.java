package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.Inject;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.db.ChannelGroupConfiguration;

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
        return mapper.get(module.getName(), channelGroup.getTenant().getId().getValue(), channelGroup.getPlatform(), channelGroup.getId());
    }

    public void put(final ChannelGroupConfiguration channelGroupConfiguration) {
        mapper.save(channelGroupConfiguration);
    }
}
