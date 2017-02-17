package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import tv.v1x1.common.dao.DAOGlobalConfiguration;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.messages.events.GlobalConfigChangeEvent;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.JsonCodec;
import tv.v1x1.common.services.queue.MessageQueueManager;

import java.util.concurrent.ExecutionException;

/**
 * Created by cobi on 10/17/2016.
 */
@Singleton
public class ConfigurationProvider<T extends GlobalConfiguration> {
    private final CodecCache<Module, T> sharedCache;
    private final DAOGlobalConfiguration daoGlobalConfiguration;
    private final ObjectMapper mapper;
    private final Module module;
    private final MessageQueueManager messageQueueManager;

    @Inject
    public ConfigurationProvider(final Module module, final CacheManager cacheManager, final DAOManager daoManager,
                                 @Named("globalConfigurationClass") final Class clazz, final MessageQueueManager messageQueueManager,
                                 final ConfigurationCacheManager configurationCacheManager) {
        daoGlobalConfiguration = daoManager.getDaoGlobalConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;
        this.messageQueueManager = messageQueueManager;

        sharedCache = cacheManager.codec(configurationCacheManager.getGlobalCache(module), Module.KEY_CODEC, new JsonCodec<T>((Class<T>) clazz));
    }

    public T getConfiguration() {
        try {
            return sharedCache.get(module);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(final T configuration) {
        try {
            final tv.v1x1.common.dto.db.GlobalConfiguration dbConfiguration = new tv.v1x1.common.dto.db.GlobalConfiguration(module.getName(), mapper.writeValueAsString(configuration));
            daoGlobalConfiguration.put(dbConfiguration);
            sharedCache.put(module, configuration);
            messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(new GlobalConfigChangeEvent(module, module));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
