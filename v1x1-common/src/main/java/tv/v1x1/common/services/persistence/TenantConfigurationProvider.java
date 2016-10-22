package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import tv.v1x1.common.dao.DAOTenantConfiguration;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.modules.TenantConfiguration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/17/2016.
 */
public class TenantConfigurationProvider<T extends TenantConfiguration> {
    private final LoadingCache<Tenant, T> cache;
    private final DAOTenantConfiguration daoTenantConfiguration;
    private final ObjectMapper mapper;
    private final Module module;

    public TenantConfigurationProvider(final Module module, final DAOManager daoManager, final Class<T> clazz) {
        daoTenantConfiguration = daoManager.getDaoTenantConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<Tenant, T>() {
                    @Override
                    public T load(final Tenant tenant) throws Exception {
                        try {
                            final tv.v1x1.common.dto.db.TenantConfiguration tenantConfiguration = daoTenantConfiguration.get(module, tenant);
                            if (tenantConfiguration == null)
                                return mapper.readValue("{}", clazz);
                            return mapper.readValue(tenantConfiguration.getJson(), clazz);
                        } catch(final Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
    }

    public T getTenantConfiguration(final Tenant tenant) {
        try {
            return cache.get(tenant);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(final Tenant tenant, final T configuration) {
        try {
            final tv.v1x1.common.dto.db.TenantConfiguration dbConfiguration = new tv.v1x1.common.dto.db.TenantConfiguration(module.getName(), tenant.getId().getValue(), mapper.writeValueAsString(configuration));
            daoTenantConfiguration.put(dbConfiguration);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
