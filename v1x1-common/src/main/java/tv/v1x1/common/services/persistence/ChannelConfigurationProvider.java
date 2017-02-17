package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import tv.v1x1.common.dao.DAOChannelConfiguration;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.messages.events.ChannelConfigChangeEvent;
import tv.v1x1.common.modules.ChannelConfiguration;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.JsonCodec;
import tv.v1x1.common.services.queue.MessageQueueManager;

import java.util.concurrent.ExecutionException;

/**
 * Created by naomi on 11/5/2016.
 */
@Singleton
public class ChannelConfigurationProvider<T extends ChannelConfiguration> {
    private final CodecCache<Channel, T> sharedCache;
    private final DAOChannelConfiguration daoChannelConfiguration;
    private final ObjectMapper mapper;
    private final Module module;
    private final MessageQueueManager messageQueueManager;

    @Inject
    public ChannelConfigurationProvider(final Module module, final CacheManager cacheManager, final DAOManager daoManager,
                                        @Named("channelConfigurationClass") final Class clazz, final MessageQueueManager messageQueueManager,
                                        final ConfigurationCacheManager configurationCacheManager) {
        daoChannelConfiguration = daoManager.getDaoChannelConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;
        this.messageQueueManager = messageQueueManager;
        sharedCache = cacheManager.codec(configurationCacheManager.getChannelCache(module), Channel.KEY_CODEC, new JsonCodec<T>((Class<T>) clazz));
    }

    public T getChannelConfiguration(final Channel channel) {
        try {
            return sharedCache.get(channel);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(final Channel channel, final T configuration) {
        try {
            final tv.v1x1.common.dto.db.ChannelConfiguration dbConfiguration = new tv.v1x1.common.dto.db.ChannelConfiguration(module.getName(), channel.getTenant().getId().getValue(),
                    channel.getPlatform(), channel.getId(), mapper.writeValueAsString(configuration));
            daoChannelConfiguration.put(dbConfiguration);
            sharedCache.put(channel, configuration);
            messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(new ChannelConfigChangeEvent(module, module, channel));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
