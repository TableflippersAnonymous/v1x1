package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import tv.v1x1.common.dao.DAOChannelConfiguration;
import tv.v1x1.common.dao.DAOTenantConfiguration;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.modules.ChannelConfiguration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 11/5/2016.
 */
public class ChannelConfigurationProvider<T extends ChannelConfiguration> {
    private final LoadingCache<Channel, T> cache;
    private final DAOChannelConfiguration daoChannelConfiguration;
    private final ObjectMapper mapper;
    private final Module module;

    public ChannelConfigurationProvider(final Module module, final DAOManager daoManager, final Class<T> clazz) {
        daoChannelConfiguration = daoManager.getDaoChannelConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<Channel, T>() {
                    @Override
                    public T load(final Channel channel) throws Exception {
                        try {
                            final tv.v1x1.common.dto.db.ChannelConfiguration channelConfiguration = daoChannelConfiguration.get(module, channel);
                            if (channelConfiguration == null)
                                return mapper.readValue("{}", clazz);
                            return mapper.readValue(channelConfiguration.getJson(), clazz);
                        } catch(final Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
    }

    public T getChannelConfiguration(final Channel channel) {
        try {
            return cache.get(channel);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(final Channel channel, final T configuration) {
        try {
            final tv.v1x1.common.dto.db.ChannelConfiguration dbConfiguration = new tv.v1x1.common.dto.db.ChannelConfiguration(module.getName(), channel.getTenant().getId().getValue(),
                    channel.getPlatform(), channel.getId(), mapper.writeValueAsString(configuration));
            daoChannelConfiguration.put(dbConfiguration);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
