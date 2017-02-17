package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import tv.v1x1.common.dao.DAOTenantConfiguration;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.JsonCodec;
import tv.v1x1.common.services.queue.MessageQueueManager;

import java.util.concurrent.ExecutionException;

/**
 * Created by cobi on 10/17/2016.
 */
@Singleton
public class TenantConfigurationProvider<T extends TenantConfiguration> {
    private final CodecCache<Tenant, T> sharedCache;
    private final DAOTenantConfiguration daoTenantConfiguration;
    private final ObjectMapper mapper;
    private final Module module;
    private final MessageQueueManager messageQueueManager;

    @Inject
    public TenantConfigurationProvider(final Module module, final CacheManager cacheManager, final DAOManager daoManager,
                                       @Named("tenantConfigurationClass") final Class clazz, final MessageQueueManager messageQueueManager,
                                       final ConfigurationCacheManager configurationCacheManager) {
        daoTenantConfiguration = daoManager.getDaoTenantConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;
        this.messageQueueManager = messageQueueManager;

        sharedCache = cacheManager.codec(configurationCacheManager.getTenantCache(module), Tenant.KEY_CODEC, new JsonCodec<T>((Class<T>) clazz));
    }

    public T getTenantConfiguration(final Tenant tenant) {
        try {
            return sharedCache.get(tenant);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(final Tenant tenant, final T configuration) {
        try {
            final tv.v1x1.common.dto.db.TenantConfiguration dbConfiguration = new tv.v1x1.common.dto.db.TenantConfiguration(module.getName(), tenant.getId().getValue(), mapper.writeValueAsString(configuration));
            daoTenantConfiguration.put(dbConfiguration);
            sharedCache.put(tenant, configuration);
            messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(new TenantConfigChangeEvent(module, module, tenant));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
