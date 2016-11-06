package tv.v1x1.common.services.persistence;

import com.datastax.driver.mapping.MappingManager;
import tv.v1x1.common.dao.*;

/**
 *
 * @author Naomi
 */
public class DAOManager {
    private final DAOTenant daoTenant;
    private final DAOGlobalUser daoGlobalUser;
    private final DAOTenantGroup daoTenantGroup;
    private final DAOKeyValueEntry daoKeyValueEntry;
    private final DAOTenantConfiguration daoTenantConfiguration;
    private final DAOGlobalConfiguration daoGlobalConfiguration;
    private final DAOLanguage daoLanguage;
    private final DAOConfigurationDefinition daoConfigurationDefinition;
    private final DAOChannelConfiguration daoChannelConfiguration;

    public DAOManager(final MappingManager mappingManager) {
        daoTenant = new DAOTenant(mappingManager);
        daoGlobalUser = new DAOGlobalUser(mappingManager);
        daoTenantGroup = new DAOTenantGroup(mappingManager);
        daoKeyValueEntry = new DAOKeyValueEntry(mappingManager);
        daoTenantConfiguration = new DAOTenantConfiguration(mappingManager);
        daoGlobalConfiguration = new DAOGlobalConfiguration(mappingManager);
        daoLanguage = new DAOLanguage(mappingManager);
        daoConfigurationDefinition = new DAOConfigurationDefinition(mappingManager);
        daoChannelConfiguration = new DAOChannelConfiguration(mappingManager);
    }

    public DAOTenant getDaoTenant() {
        return daoTenant;
    }

    public DAOGlobalUser getDaoGlobalUser() {
        return daoGlobalUser;
    }

    public DAOTenantGroup getDaoTenantGroup() {
        return daoTenantGroup;
    }

    public DAOKeyValueEntry getDaoKeyValueEntry() {
        return daoKeyValueEntry;
    }

    public DAOTenantConfiguration getDaoTenantConfiguration() {
        return daoTenantConfiguration;
    }

    public DAOGlobalConfiguration getDaoGlobalConfiguration() {
        return daoGlobalConfiguration;
    }

    public DAOLanguage getDaoLanguage() {
        return daoLanguage;
    }

    public DAOConfigurationDefinition getDaoConfigurationDefinition() {
        return daoConfigurationDefinition;
    }

    public DAOChannelConfiguration getDaoChannelConfiguration() {
        return daoChannelConfiguration;
    }
}
