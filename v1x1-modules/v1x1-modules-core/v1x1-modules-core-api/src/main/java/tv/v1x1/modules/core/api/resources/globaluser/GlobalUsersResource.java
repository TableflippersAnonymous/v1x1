package tv.v1x1.modules.core.api.resources.globaluser;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

/**
 * Created by naomi on 10/26/2016.
 */
/*
  /global-users
    /{platform}
      /{username} - GET: global user id
    /{userid} - GET: list of endpoints
 */
@Path("/api/v1/global-users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GlobalUsersResource {
    private final DAOGlobalUser daoGlobalUser;
    private final Authorizer authorizer;

    @Inject
    public GlobalUsersResource(final DAOGlobalUser daoGlobalUser, final Authorizer authorizer) {
        this.daoGlobalUser = daoGlobalUser;
        this.authorizer = authorizer;
    }

    @Path("/{global_user_id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @GET
    public List<String> listGlobalUserEndpoints(@HeaderParam("Authorization") final String authorization,
                                                @PathParam("global_user_id") final String globalUserId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.list");
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        return ImmutableList.of("users");
    }

    @Path("/{platform: [a-z]+}/{id}")
    @GET
    public String getGlobalUserId(@HeaderParam("Authorization") final String authorization,
                                  @PathParam("platform") final String platformString,
                                  @PathParam("id") final String id) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.read");
        final Platform platform = Platform.valueOf(platformString.toUpperCase());
        final GlobalUser globalUser = daoGlobalUser.getByUser(platform, id);
        if(globalUser == null)
            throw new NotFoundException();
        return globalUser.getId().toString();
    }
}
