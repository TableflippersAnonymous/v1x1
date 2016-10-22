package tv.twitchbot.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import tv.twitchbot.common.dto.db.TenantUserPermissions;

import java.util.List;
import java.util.UUID;

/**
 * Created by naomi on 10/16/2016.
 */
public class DAOTenantUserPermissions {
    private final Mapper<TenantUserPermissions> mapper;
    private final TenantUserPermissionsAccessor accessor;

    @Accessor
    public interface TenantUserPermissionsAccessor {
        @Query("SELECT * FROM tenant_user_permissions WHERE tenant_id = ? AND user_id = ?")
        TenantUserPermissions getByTenantAndUser(UUID tenantId, UUID userId);

        @Query("SELECT * FROM tenant_user_permissions WHERE tenant_id = ?")
        Result<TenantUserPermissions> getByTenant(UUID tenantId);
    }

    public DAOTenantUserPermissions(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(TenantUserPermissions.class);
        accessor = mappingManager.createAccessor(TenantUserPermissionsAccessor.class);
    }

    public TenantUserPermissions getByTenantAndUser(final UUID tenantId, final UUID userId) {
        return accessor.getByTenantAndUser(tenantId, userId);
    }

    public Iterable<TenantUserPermissions> getByTenant(final UUID tenantId) {
        return accessor.getByTenant(tenantId);
    }

    public TenantUserPermissions create(final UUID tenantId, final UUID userId, final List<TenantUserPermissions.Permission> permissions) {
        final TenantUserPermissions tenantUserPermissions = new TenantUserPermissions(tenantId, userId, permissions);
        save(tenantUserPermissions);
        return tenantUserPermissions;
    }

    public void save(final TenantUserPermissions tenantUserPermissions) {
        mapper.save(tenantUserPermissions);
    }

    public void delete(final TenantUserPermissions tenantUserPermissions) {
        mapper.delete(tenantUserPermissions);
    }
}
