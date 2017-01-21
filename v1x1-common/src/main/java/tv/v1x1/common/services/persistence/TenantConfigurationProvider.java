package tv.v1x1.common.services.persistence;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dao.DAOTenantConfiguration;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.proto.core.ChannelOuterClass;
import tv.v1x1.common.dto.proto.core.UUIDOuterClass;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.cache.RedisCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/17/2016.
 */
public class TenantConfigurationProvider<T extends TenantConfiguration> {
    private final LoadingCache<byte[], T> cache;
    private final RedisCache sharedCache;
    private final DAOTenantConfiguration daoTenantConfiguration;
    private final ObjectMapper mapper;
    private final Module module;

    public TenantConfigurationProvider(final Module module, final CacheManager cacheManager, final DAOManager daoManager, final Class<T> clazz) {
        daoTenantConfiguration = daoManager.getDaoTenantConfiguration();
        mapper = new ObjectMapper(new JsonFactory());
        this.module = module;

        sharedCache = cacheManager.redisCache("TenantConfigurationProvider|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] tenantData) throws Exception {
                final tv.v1x1.common.dto.db.TenantConfiguration tenantConfiguration = daoTenantConfiguration.get(module, Tenant.fromProto(ChannelOuterClass.Tenant.parseFrom(tenantData)));
                if(tenantConfiguration == null)
                    return "{}".getBytes();
                return tenantConfiguration.getJson().getBytes();
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

    public T getTenantConfiguration(final Tenant tenant) {
        try {
            return cache.get(sharedCache.get(tenant.toProto().toByteArray()));
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(final Tenant tenant, final T configuration) {
        try {
            final tv.v1x1.common.dto.db.TenantConfiguration dbConfiguration = new tv.v1x1.common.dto.db.TenantConfiguration(module.getName(), tenant.getId().getValue(), mapper.writeValueAsString(configuration));
            daoTenantConfiguration.put(dbConfiguration);
            sharedCache.put(tenant.toProto().toByteArray(), dbConfiguration.getJson().getBytes());
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
