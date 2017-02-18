package tv.v1x1.common.services.persistence;

import com.google.common.cache.CacheLoader;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dao.DAOChannelConfiguration;
import tv.v1x1.common.dao.DAOGlobalConfiguration;
import tv.v1x1.common.dao.DAOTenantConfiguration;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.cache.SharedCache;

import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 2/17/2017.
 */
@Singleton
public class ConfigurationCacheManager {
    private final DAOChannelConfiguration daoChannelConfiguration;
    private final DAOTenantConfiguration daoTenantConfiguration;
    private final DAOGlobalConfiguration daoGlobalConfiguration;
    private final CacheManager cacheManager;

    @Inject
    public ConfigurationCacheManager(final DAOChannelConfiguration daoChannelConfiguration, final DAOTenantConfiguration daoTenantConfiguration, final DAOGlobalConfiguration daoGlobalConfiguration, final CacheManager cacheManager) {
        this.daoChannelConfiguration = daoChannelConfiguration;
        this.daoTenantConfiguration = daoTenantConfiguration;
        this.daoGlobalConfiguration = daoGlobalConfiguration;
        this.cacheManager = cacheManager;
    }

    public SharedCache<byte[], byte[]> getChannelCache(final Module module) {
        return cacheManager.redisCache("ChannelConfiguration|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] channelData) throws Exception {
                final tv.v1x1.common.dto.db.ChannelConfiguration channelConfiguration = daoChannelConfiguration.get(module, Channel.KEY_CODEC.decode(channelData));
                if(channelConfiguration == null)
                    return "{}".getBytes();
                return channelConfiguration.getJson().getBytes();
            }
        });
    }

    public SharedCache<byte[], byte[]> getTenantCache(final Module module) {
        return cacheManager.redisCache("TenantConfiguration|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] tenantData) throws Exception {
                final tv.v1x1.common.dto.db.TenantConfiguration tenantConfiguration = daoTenantConfiguration.get(module, Tenant.KEY_CODEC.decode(tenantData));
                if(tenantConfiguration == null)
                    return "{}".getBytes();
                return tenantConfiguration.getJson().getBytes();
            }
        });
    }

    public SharedCache<byte[], byte[]> getGlobalCache(final Module module) {
        return cacheManager.redisCache("GlobalConfiguration|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] module) throws Exception {
                final tv.v1x1.common.dto.db.GlobalConfiguration globalConfiguration = daoGlobalConfiguration.get(Module.KEY_CODEC.decode(module));
                if(globalConfiguration == null)
                    return "{}".getBytes();
                return globalConfiguration.getJson().getBytes();
            }
        });
    }
}
