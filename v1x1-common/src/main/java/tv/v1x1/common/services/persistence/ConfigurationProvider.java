package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import tv.v1x1.common.dao.DAOGlobalConfiguration;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.cache.CodecCache;
import tv.v1x1.common.services.cache.JsonCodec;
import tv.v1x1.common.services.cache.RedisCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/17/2016.
 */
public class ConfigurationProvider<T extends GlobalConfiguration> {
    private final CodecCache<Module, T> sharedCache;
    private final DAOGlobalConfiguration daoGlobalConfiguration;
    private final ObjectMapper mapper;
    private final Module module;

    public ConfigurationProvider(final Module module, final CacheManager cacheManager, final DAOManager daoManager, final Class<T> clazz) {
        daoGlobalConfiguration = daoManager.getDaoGlobalConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;

        sharedCache = cacheManager.codec(cacheManager.redisCache("ConfigurationProvider|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] module) throws Exception {
                final tv.v1x1.common.dto.db.GlobalConfiguration globalConfiguration = daoGlobalConfiguration.get(Module.KEY_CODEC.decode(module));
                if(globalConfiguration == null)
                    return "{}".getBytes();
                return globalConfiguration.getJson().getBytes();
            }
        }), Module.KEY_CODEC, new JsonCodec<>(clazz));
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
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
