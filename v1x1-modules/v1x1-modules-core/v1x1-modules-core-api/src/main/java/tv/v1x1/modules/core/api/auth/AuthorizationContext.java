package tv.v1x1.modules.core.api.auth;

import tv.v1x1.common.dto.core.GlobalUser;

import javax.ws.rs.NotAuthorizedException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by cobi on 11/12/2016.
 */
public class AuthorizationContext {

    private final GlobalUser globalUser;
    private final Set<String> permissions;

    public AuthorizationContext(final GlobalUser globalUser, final Set<String> permissions) {
        this.globalUser = globalUser;
        this.permissions = permissions;
    }

    public AuthorizationContext ensurePermission(final String permission) {
        if(!permissions.contains(permission))
            throw new NotAuthorizedException("Not authorized");
        return this;
    }

    public AuthorizationContext ensurePrincipal(final String globalUserId) {
        if(!globalUser.getId().getValue().equals(UUID.fromString(globalUserId)))
            throw new NotAuthorizedException("Not authorized");
        return this;
    }
}
