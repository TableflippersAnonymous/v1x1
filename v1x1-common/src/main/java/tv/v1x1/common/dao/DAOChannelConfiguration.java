package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.db.ChannelConfiguration;

/**
 * Created by cobi on 11/5/2016.
 */
public class DAOChannelConfiguration {
    private final Mapper<ChannelConfiguration> mapper;

    public DAOChannelConfiguration(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(ChannelConfiguration.class);
    }

    public ChannelConfiguration get(final Module module, final Channel channel) {
        return mapper.get(module.getName(), channel.getTenant().getId().getValue(), channel.getPlatform(), channel.getId());
    }

    public void put(final ChannelConfiguration channelConfiguration) {
        mapper.save(channelConfiguration);
    }
}
