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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/17/2016.
 */
public class ConfigurationProvider<T extends GlobalConfiguration> {
    private final LoadingCache<Module, T> cache;
    private final DAOGlobalConfiguration daoGlobalConfiguration;
    private final ObjectMapper mapper;
    private final Module module;

    public ConfigurationProvider(final Module module, final DAOManager daoManager, final Class<T> clazz) {
        daoGlobalConfiguration = daoManager.getDaoGlobalConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<Module, T>() {
                    @Override
                    public T load(final Module module1) throws Exception {
                        final tv.v1x1.common.dto.db.GlobalConfiguration globalConfiguration = daoGlobalConfiguration.get(module);
                        if(globalConfiguration == null)
                            return mapper.readValue("{}", clazz);
                        return mapper.readValue(globalConfiguration.getJson(), clazz);
                    }
                });
    }

    public T getConfiguration() {
        try {
            return cache.get(module);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(final T configuration) {
        try {
            final tv.v1x1.common.dto.db.GlobalConfiguration dbConfiguration = new tv.v1x1.common.dto.db.GlobalConfiguration(module.getName(), mapper.writeValueAsString(configuration));
            daoGlobalConfiguration.put(dbConfiguration);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
