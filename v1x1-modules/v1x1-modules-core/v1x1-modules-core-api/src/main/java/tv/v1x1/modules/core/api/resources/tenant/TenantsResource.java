package tv.v1x1.modules.core.api.resources.tenant;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.api.rest.ApiList;
import tv.v1x1.modules.core.api.api.rest.ApiPrimitive;
import tv.v1x1.modules.core.api.auth.AuthorizationContext;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /tenants - GET: list of tenants you are allowed to control.
    /{platform}
      /{channel} - GET: tenant-id
    /{tenant} - GET: list of endpoints
 */
@Path("/api/v1/tenants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TenantsResource {
    private final DAOTenant daoTenant;
    private final DAOTenantGroup daoTenantGroup;
    private final Authorizer authorizer;

    @Inject
    public TenantsResource(final DAOManager daoManager, final Authorizer authorizer) {
        this.daoTenant = daoManager.getDaoTenant();
        this.daoTenantGroup = daoManager.getDaoTenantGroup();
        this.authorizer = authorizer;
    }

    @GET
    public ApiList<String> listTenantsForUser(@HeaderParam("Authorization") final String authorization) {
        final AuthorizationContext authorizationContext = authorizer.forAuthorization(authorization);
        authorizationContext.ensurePermission("api.tenants.list");
        return new ApiList<>(Lists.newArrayList(daoTenantGroup.getTenantsByUser(authorizationContext.getGlobalUser()))
                .stream().map(UUID::toString).collect(Collectors.toList()));
    }

    @Path("/{tenant_id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @GET
    public tv.v1x1.modules.core.api.api.rest.Tenant getTenant(@HeaderParam("Authorization") final String authorization,
                                                              @PathParam("tenant_id") final String tenantId) {
        final tv.v1x1.common.dto.core.Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(tenant.getId().getValue(), authorization).ensurePermission("api.tenants.read");
        return new tv.v1x1.modules.core.api.api.rest.Tenant(tenant);
    }

    @Path("/{platform: [a-z]+}/{id}")
    @GET
    public ApiPrimitive<String> getTenantId(@HeaderParam("Authorization") final String authorization,
                                            @PathParam("platform") final String platform, @PathParam("id") final String id) {
        authorizer.forAuthorization(authorization).ensurePermission("api.tenants.list");
        final Tenant tenant = daoTenant.getByChannel(Platform.valueOf(platform.toUpperCase()), id);
        if(tenant == null)
            throw new NotFoundException();
        return new ApiPrimitive<>(tenant.getId().toString());
    }

    private tv.v1x1.common.dto.core.Tenant getTenant(final String tenantId) {
        final Tenant dbTenant = daoTenant.getById(UUID.fromString(tenantId));
        if(dbTenant == null)
            throw new NotFoundException();
        return dbTenant.toCore(daoTenant);
    }
}
