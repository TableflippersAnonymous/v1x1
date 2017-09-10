package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import tv.v1x1.common.dao.DAOTenantConfiguration;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.modules.UserConfiguration;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.JsonCodec;
import tv.v1x1.common.services.queue.MessageQueueManager;

/**
 * Created by naomi on 10/17/2016.
 */
@Singleton
public class UserConfigurationProvider<T extends UserConfiguration> {
    private final CodecCache<Tenant, T> tenantCache;
    private final CodecCache<ChannelGroup, T> channelGroupCache;
    private final CodecCache<Channel, T> channelCache;
    private final DAOTenantConfiguration daoTenantConfiguration;
    private final ObjectMapper mapper;
    private final Module module;
    private final MessageQueueManager messageQueueManager;

    @Inject
    public UserConfigurationProvider(final Module module, final CacheManager cacheManager, final DAOManager daoManager,
                                       @Named("userConfigurationClass") final Class clazz, final MessageQueueManager messageQueueManager,
                                       final ConfigurationCacheManager configurationCacheManager) {
        daoTenantConfiguration = daoManager.getDaoTenantConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;
        this.messageQueueManager = messageQueueManager;

        tenantCache = cacheManager.codec(configurationCacheManager.getTenantCache(module), Tenant.KEY_CODEC, new JsonCodec<>((Class<T>) clazz));
        channelGroupCache = cacheManager.codec(configurationCacheManager.getChannelGroupCache(module), ChannelGroup.KEY_CODEC, new JsonCodec<>((Class<T>) clazz));
        channelCache = cacheManager.codec(configurationCacheManager.getChannelCache(module), Channel.KEY_CODEC, new JsonCodec<>((Class<T>) clazz));
    }

    public T getConfiguration(final Tenant tenant) {
        try {
            return tenantCache.get(tenant);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T getConfiguration(final ChannelGroup channelGroup) {
        try {
            return channelGroupCache.get(channelGroup);
        } catch (final Exception e) {
            return getConfiguration(channelGroup.getTenant());
        }
    }

    public T getConfiguration(final Channel channel) {
        try {
            return channelCache.get(channel);
        } catch (final Exception e) {
            return getConfiguration(channel.getChannelGroup());
        }
    }

    public void save(final Tenant tenant, final T configuration) {
        try {
            final tv.v1x1.common.dto.db.TenantConfiguration dbConfiguration = new tv.v1x1.common.dto.db.TenantConfiguration(module.getName(), tenant.getId().getValue(), mapper.writeValueAsString(configuration));
            daoTenantConfiguration.put(dbConfiguration);
            tenantCache.put(tenant, configuration);
            messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(new TenantConfigChangeEvent(module, module, tenant));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
