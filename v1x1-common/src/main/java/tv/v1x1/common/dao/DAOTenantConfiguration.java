package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.db.TenantConfiguration;

/**
 * Created by cobi on 10/17/2016.
 */
@Singleton
public class DAOTenantConfiguration {
    private final Mapper<TenantConfiguration> mapper;

    @Inject
    public DAOTenantConfiguration(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(TenantConfiguration.class);
    }

    public TenantConfiguration get(final Module module, final Tenant tenant) {
        return mapper.get(module.getName(), tenant.getId().getValue());
    }

    public void put(final TenantConfiguration tenantConfiguration) {
        mapper.save(tenantConfiguration);
    }
}
