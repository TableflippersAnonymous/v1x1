package tv.v1x1.common.services.persistence;

import com.google.common.cache.CacheLoader;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dao.DAOChannelConfiguration;
import tv.v1x1.common.dao.DAOChannelGroupConfiguration;
import tv.v1x1.common.dao.DAOGlobalConfiguration;
import tv.v1x1.common.dao.DAOTenantConfiguration;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.cache.SharedCache;

import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 2/17/2017.
 */
@Singleton
public class ConfigurationCacheManager {
    private final DAOChannelConfiguration daoChannelConfiguration;
    private final DAOChannelGroupConfiguration daoChannelGroupConfiguration;
    private final DAOTenantConfiguration daoTenantConfiguration;
    private final DAOGlobalConfiguration daoGlobalConfiguration;
    private final CacheManager cacheManager;

    @Inject
    public ConfigurationCacheManager(final DAOChannelConfiguration daoChannelConfiguration, final DAOChannelGroupConfiguration daoChannelGroupConfiguration,
                                     final DAOTenantConfiguration daoTenantConfiguration, final DAOGlobalConfiguration daoGlobalConfiguration,
                                     final CacheManager cacheManager) {
        this.daoChannelConfiguration = daoChannelConfiguration;
        this.daoChannelGroupConfiguration = daoChannelGroupConfiguration;
        this.daoTenantConfiguration = daoTenantConfiguration;
        this.daoGlobalConfiguration = daoGlobalConfiguration;
        this.cacheManager = cacheManager;
    }

    public SharedCache<byte[], byte[]> getChannelCache(final Module module) {
        return cacheManager.redisCache("ChannelConfiguration2|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] channelData) {
                final tv.v1x1.common.dto.db.ChannelConfiguration channelConfiguration = daoChannelConfiguration.get(module, Channel.KEY_CODEC.decode(channelData));
                if(channelConfiguration == null || !channelConfiguration.isEnabled())
                    return null;
                return channelConfiguration.getJson().getBytes();
            }
        });
    }

    public SharedCache<byte[], byte[]> getChannelGroupCache(final Module module) {
        return cacheManager.redisCache("ChannelGroupConfiguration2|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] channelGroupData) {
                final tv.v1x1.common.dto.db.ChannelGroupConfiguration channelGroupConfiguration = daoChannelGroupConfiguration.get(module, ChannelGroup.KEY_CODEC.decode(channelGroupData));
                if(channelGroupConfiguration == null || !channelGroupConfiguration.isEnabled())
                    return null;
                return channelGroupConfiguration.getJson().getBytes();
            }
        });
    }

    public SharedCache<byte[], byte[]> getTenantCache(final Module module) {
        return cacheManager.redisCache("TenantConfiguration2|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] tenantData) {
                final tv.v1x1.common.dto.db.TenantConfiguration tenantConfiguration = daoTenantConfiguration.get(module, Tenant.KEY_CODEC.decode(tenantData));
                if(tenantConfiguration == null)
                    return "{}".getBytes();
                return tenantConfiguration.getJson().getBytes();
            }
        });
    }

    public SharedCache<byte[], byte[]> getGlobalCache(final Module module) {
        return cacheManager.redisCache("GlobalConfiguration2|" + module.getName(), 10, TimeUnit.MINUTES, new CacheLoader<byte[], byte[]>() {
            @Override
            public byte[] load(final byte[] globalData) {
                final tv.v1x1.common.dto.db.GlobalConfiguration globalConfiguration = daoGlobalConfiguration.get(module);
                if(globalConfiguration == null)
                    return "{}".getBytes();
                return globalConfiguration.getJson().getBytes();
            }
        });
    }
}
