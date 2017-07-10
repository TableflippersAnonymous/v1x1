package tv.v1x1.modules.core.api.auth;

import tv.v1x1.common.dto.core.GlobalUser;

import javax.ws.rs.NotAuthorizedException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by cobi on 11/12/2016.
 */
public class AuthorizationContext {

    private final Authorizer.Principal principal;
    private final Set<String> permissions;

    public AuthorizationContext(final Authorizer.Principal principal, final Set<String> permissions) {
        this.principal = principal;
        this.permissions = permissions;
    }

    public AuthorizationContext ensurePermission(final String permission) {
        if(!hasPermission(permission))
            throw new NotAuthorizedException("Not authorized");
        return this;
    }

    public boolean hasPermission(final String permission) {
        return permissions.contains(permission);
    }

    public AuthorizationContext ensurePrincipal(final String globalUserId) {
        if(!getGlobalUser().getId().getValue().equals(UUID.fromString(globalUserId)))
            throw new NotAuthorizedException("Not authorized");
        return this;
    }

    public Authorizer.Principal getPrincipal() {
        return principal;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public GlobalUser getGlobalUser() {
        return principal.getGlobalUser();
    }
}
