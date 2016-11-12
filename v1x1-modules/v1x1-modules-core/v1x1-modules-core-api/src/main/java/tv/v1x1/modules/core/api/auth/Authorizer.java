package tv.v1x1.modules.core.api.auth;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import io.jsonwebtoken.Jwts;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dao.DAOThirdPartyCredential;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.Platform;

import javax.ws.rs.BadRequestException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by naomi on 11/12/2016.
 */
public class Authorizer {
    private final DAOTenantGroup daoTenantGroup;
    private final DAOTenant daoTenant;
    private final DAOThirdPartyCredential daoThirdPartyCredential;

    private class Principal {
        private final GlobalUser globalUser;
        private final Set<Permission> restrictions;

        public Principal(final GlobalUser globalUser, final Set<Permission> restrictions) {
            this.globalUser = globalUser;
            this.restrictions = restrictions;
        }

        public GlobalUser getGlobalUser() {
            return globalUser;
        }

        public Set<Permission> getRestrictions() {
            return restrictions;
        }
    }

    @Inject
    public Authorizer(final DAOTenantGroup daoTenantGroup, final DAOTenant daoTenant, final DAOThirdPartyCredential daoThirdPartyCredential) {
        this.daoTenantGroup = daoTenantGroup;
        this.daoTenant = daoTenant;
        this.daoThirdPartyCredential = daoThirdPartyCredential;
    }

    public AuthorizationContext forAuthorization(final String authorizationHeader) {
        return getContext(authorizationHeader,
                principal -> daoTenantGroup.getAllPermissions(
                        DAOTenantGroup.GLOBAL_TENANT,
                        principal.getGlobalUser(),
                        Platform.API,
                        "__API__",
                        ImmutableSet.of("__DEFAULT__")));
    }

    public AuthorizationContext tenantAuthorization(final UUID tenantId, final String authorizationHeader) {
        return getContext(authorizationHeader,
                principal -> daoTenantGroup.getAllPermissions(
                        daoTenant.getById(tenantId).toCore(),
                        principal.getGlobalUser(),
                        Platform.API,
                        "__API__",
                        ImmutableSet.of("__DEFAULT__", "__DEFAULT_TENANT__")));
    }


    private Principal getPrincipal(final String authorizationHeader) {
        if(authorizationHeader == null)
            throw new BadRequestException("Bad authorization header");
        final String[] authorization = authorizationHeader.split(" ");
        if(authorization.length < 2)
            throw new BadRequestException("Bad authorization header");
        if(authorization.length == 2 && authorization[0].equals("JWT"))
            return getJWTPrincipal(authorization[1]);
        throw new BadRequestException("Bad authorization header");
    }

    private Principal getJWTPrincipal(final String jwtString) {
        Jwts.parser()
                .setSigningKey(daoThirdPartyCredential.get("Module|Core|API|JWT|Public").getCredential())
                .parseClaimsJws(jwtString);
        return null;
    }

    private AuthorizationContext getContext(final String authorizationHeader, final Function<Principal, Set<Permission>> f) {
        final Principal principal = getPrincipal(authorizationHeader);
        final Set<Permission> allPermissions = f.apply(principal);
        Set<Permission> allowedPermissions = allPermissions;
        if(principal.getRestrictions() != null) {
            allowedPermissions = Sets.intersection(allPermissions, principal.getRestrictions());
        }
        return new AuthorizationContext(principal.getGlobalUser(), allowedPermissions.stream().map(Permission::getNode).collect(Collectors.toSet()));
    }
}
