package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import tv.v1x1.common.dao.DAOChannelConfiguration;
import tv.v1x1.common.dao.DAOTenantConfiguration;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.dto.proto.core.UUIDOuterClass;
import tv.v1x1.common.modules.ChannelConfiguration;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.JsonCodec;
import tv.v1x1.common.services.cache.RedisCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 11/5/2016.
 */
@Singleton
public class ChannelConfigurationProvider<T extends ChannelConfiguration> {
    private final CodecCache<Channel, T> sharedCache;
    private final DAOChannelConfiguration daoChannelConfiguration;
    private final ObjectMapper mapper;
    private final Module module;

    @Inject
    public ChannelConfigurationProvider(final Module module, final CacheManager cacheManager, final DAOManager daoManager, @Named("channelConfigurationClass") final Class clazz) {
        daoChannelConfiguration = daoManager.getDaoChannelConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;

        sharedCache = cacheManager.codec(cacheManager.redisCache("ChannelConfigurationProvider|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] channelData) throws Exception {
                final tv.v1x1.common.dto.db.ChannelConfiguration channelConfiguration = daoChannelConfiguration.get(module, Channel.KEY_CODEC.decode(channelData));
                if(channelConfiguration == null)
                    return "{}".getBytes();
                return channelConfiguration.getJson().getBytes();
            }
        }), Channel.KEY_CODEC, new JsonCodec<T>((Class<T>) clazz));
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
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
