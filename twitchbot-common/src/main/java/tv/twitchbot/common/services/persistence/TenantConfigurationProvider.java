package tv.twitchbot.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import tv.twitchbot.common.dao.DAOTenantConfiguration;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.Tenant;
import tv.twitchbot.common.modules.TenantConfiguration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/17/2016.
 */
public class TenantConfigurationProvider<T extends TenantConfiguration> {
    private LoadingCache<Tenant, T> cache;
    private DAOTenantConfiguration daoTenantConfiguration;
    private ObjectMapper mapper;
    private Module module;

    public TenantConfigurationProvider(Module module, DAOManager daoManager, Class<T> clazz) {
        daoTenantConfiguration = daoManager.getDaoTenantConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<Tenant, T>() {
                    @Override
                    public T load(Tenant tenant) throws Exception {
                        tv.twitchbot.common.dto.db.TenantConfiguration tenantConfiguration = daoTenantConfiguration.get(module, tenant);
                        if(tenantConfiguration == null)
                            return mapper.readValue("{}", clazz);
                        return mapper.readValue(tenantConfiguration.getJson(), clazz);
                    }
                });
    }

    public T getTenantConfiguration(Tenant tenant) {
        try {
            return cache.get(tenant);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Tenant tenant, T configuration) {
        try {
            tv.twitchbot.common.dto.db.TenantConfiguration dbConfiguration = new tv.twitchbot.common.dto.db.TenantConfiguration(module.getName(), tenant.getId().getValue(), mapper.writeValueAsString(configuration));
            daoTenantConfiguration.put(dbConfiguration);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
