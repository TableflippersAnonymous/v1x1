package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
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
import tv.v1x1.common.services.cache.RedisCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 11/5/2016.
 */
public class ChannelConfigurationProvider<T extends ChannelConfiguration> {
    private final LoadingCache<byte[], T> cache;
    private final RedisCache sharedCache;
    private final DAOChannelConfiguration daoChannelConfiguration;
    private final ObjectMapper mapper;
    private final Module module;

    public ChannelConfigurationProvider(final Module module, final CacheManager cacheManager, final DAOManager daoManager, final Class<T> clazz) {
        daoChannelConfiguration = daoManager.getDaoChannelConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;

        sharedCache = cacheManager.redisCache("ChannelConfigurationProvider|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] channelData) throws Exception {
                final tv.v1x1.common.dto.db.ChannelConfiguration channelConfiguration = daoChannelConfiguration.get(module, Channel.fromProto(ChannelOuterClass.Channel.parseFrom(channelData)));
                if(channelConfiguration == null)
                    return "{}".getBytes();
                return channelConfiguration.getJson().getBytes();
            }
        });

        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<byte[], T>() {
                    @Override
                    public T load(final byte[] key) throws Exception {
                        try {
                            return mapper.readValue(new String(key), clazz);
                        } catch(final Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
    }

    public T getChannelConfiguration(final Channel channel) {
        try {
            return cache.get(sharedCache.get(channel.toProto().toByteArray()));
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(final Channel channel, final T configuration) {
        try {
            final tv.v1x1.common.dto.db.ChannelConfiguration dbConfiguration = new tv.v1x1.common.dto.db.ChannelConfiguration(module.getName(), channel.getTenant().getId().getValue(),
                    channel.getPlatform(), channel.getId(), mapper.writeValueAsString(configuration));
            daoChannelConfiguration.put(dbConfiguration);
            sharedCache.put(channel.toProto().toByteArray(), dbConfiguration.getJson().getBytes());
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
