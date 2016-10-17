package tv.twitchbot.common.services.persistence;

import com.datastax.driver.mapping.MappingManager;
import tv.twitchbot.common.dao.DAOGlobalUser;
import tv.twitchbot.common.dao.DAOKeyValueEntry;
import tv.twitchbot.common.dao.DAOTenant;
import tv.twitchbot.common.dao.DAOTenantUserPermissions;

/**
 * Created by naomi on 10/16/2016.
 */
public class DAOManager {
    private DAOTenant daoTenant;
    private DAOGlobalUser daoGlobalUser;
    private DAOTenantUserPermissions daoTenantUserPermissions;
    private DAOKeyValueEntry daoKeyValueEntry;

    public DAOManager(MappingManager mappingManager) {
        daoTenant = new DAOTenant(mappingManager);
        daoGlobalUser = new DAOGlobalUser(mappingManager);
        daoTenantUserPermissions = new DAOTenantUserPermissions(mappingManager);
        daoKeyValueEntry = new DAOKeyValueEntry(mappingManager);
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
}
