package tv.v1x1.common.services.persistence;

import com.datastax.driver.mapping.MappingManager;
import tv.v1x1.common.dao.*;

/**
 * Created by cobi on 10/16/2016.
 */
public class DAOManager {
    private final DAOTenant daoTenant;
    private final DAOGlobalUser daoGlobalUser;
    private final DAOTenantUserPermissions daoTenantUserPermissions;
    private final DAOKeyValueEntry daoKeyValueEntry;
    private final DAOTenantConfiguration daoTenantConfiguration;
    private final DAOGlobalConfiguration daoGlobalConfiguration;
    private DAOLanguage daoLanguage;

    public DAOManager(final MappingManager mappingManager) {
        daoTenant = new DAOTenant(mappingManager);
        daoGlobalUser = new DAOGlobalUser(mappingManager);
        daoTenantUserPermissions = new DAOTenantUserPermissions(mappingManager);
        daoKeyValueEntry = new DAOKeyValueEntry(mappingManager);
        daoTenantConfiguration = new DAOTenantConfiguration(mappingManager);
        daoGlobalConfiguration = new DAOGlobalConfiguration(mappingManager);
        daoLanguage = new DAOLanguage(mappingManager);
    }

    public DAOTenant getDaoTenant() {
        return daoTenant;
    }

    public DAOGlobalUser getDaoGlobalUser() {
        return daoGlobalUser;
    }

    public DAOTenantUserPermissions getDaoTenantUserPermissions() {
        return daoTenantUserPermissions;
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

    public void setDaoLanguage(final DAOLanguage daoLanguage) {
        this.daoLanguage = daoLanguage;
    }
}
