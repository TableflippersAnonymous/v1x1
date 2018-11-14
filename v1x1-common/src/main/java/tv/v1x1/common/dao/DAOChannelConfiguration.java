package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.db.ChannelConfiguration;
import tv.v1x1.common.dto.db.Platform;

/**
 * Created by naomi on 11/5/2016.
 */
@Singleton
public class DAOChannelConfiguration {
    private final Mapper<ChannelConfiguration> mapper;

    @Inject
    public DAOChannelConfiguration(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(ChannelConfiguration.class);
    }

    public ChannelConfiguration get(final Module module, final Channel channel) {
        return get(module.getName(), channel.getChannelGroup().getPlatform(), channel.getChannelGroup().getId(), channel.getId());
    }

    public ChannelConfiguration get(final String moduleName, final Platform platform, final String channelGroupId, final String channelId) {
        return mapper.get(moduleName, platform, channelGroupId, channelGroupId);
    }

    public void put(final ChannelConfiguration channelConfiguration) {
        mapper.save(channelConfiguration);
    }
}
