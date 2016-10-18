package tv.twitchbot.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.Tenant;
import tv.twitchbot.common.dto.db.TenantConfiguration;

/**
 * Created by naomi on 10/17/2016.
 */
public class DAOTenantConfiguration {
    private Mapper<TenantConfiguration> mapper;

    public DAOTenantConfiguration(MappingManager mappingManager) {
        mapper = mappingManager.mapper(TenantConfiguration.class);
    }

    public TenantConfiguration get(Module module, Tenant tenant) {
        return mapper.get(module.getName(), tenant.getId().getValue());
    }

    public void put(TenantConfiguration tenantConfiguration) {
        mapper.save(tenantConfiguration);
    }
}
