package tv.twitchbot.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import tv.twitchbot.common.dao.DAOGlobalConfiguration;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.modules.GlobalConfiguration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/17/2016.
 */
public class ConfigurationProvider<T extends GlobalConfiguration> {
    private LoadingCache<Module, T> cache;
    private DAOGlobalConfiguration daoGlobalConfiguration;
    private ObjectMapper mapper;
    private Module module;

    public ConfigurationProvider(Module module, DAOManager daoManager, Class<T> clazz) {
        daoGlobalConfiguration = daoManager.getDaoGlobalConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<Module, T>() {
                    @Override
                    public T load(Module module1) throws Exception {
                        tv.twitchbot.common.dto.db.GlobalConfiguration globalConfiguration = daoGlobalConfiguration.get(module);
                        if(globalConfiguration == null)
                            return mapper.readValue("{}", clazz);
                        return mapper.readValue(globalConfiguration.getJson(), clazz);
                    }
                });
    }

    public T getConfiguration() {
        try {
            return cache.get(module);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(T configuration) {
        try {
            tv.twitchbot.common.dto.db.GlobalConfiguration dbConfiguration = new tv.twitchbot.common.dto.db.GlobalConfiguration(module.getName(), mapper.writeValueAsString(configuration));
            daoGlobalConfiguration.put(dbConfiguration);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
