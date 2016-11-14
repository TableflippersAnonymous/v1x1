package tv.v1x1.modules.core.api.resources.globaluser;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.modules.core.api.api.PrivateUser;
import tv.v1x1.modules.core.api.api.User;
import tv.v1x1.modules.core.api.auth.AuthorizationContext;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /global-users
    /{userid}
      /users - GET: list of platforms
        /{platform} - GET: list of users on platform
          /{user} - GET: user object; PUT: link a user; DELETE: unlink a user
 */
@Path("/api/v1/global-users/{global_user_id}/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {
    private final DAOGlobalUser daoGlobalUser;
    private final Authorizer authorizer;

    @Inject
    public UsersResource(final DAOGlobalUser daoGlobalUser, final Authorizer authorizer) {
        this.daoGlobalUser = daoGlobalUser;
        this.authorizer = authorizer;
    }

    @GET
    public List<String> listPlatforms(@HeaderParam("Authorization") final String authorization,
                                      @PathParam("global_user_id") final String globalUserId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.read").ensurePrincipal(globalUserId);
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        return ImmutableList.copyOf(globalUser.getEntries().stream().map(GlobalUser.Entry::getPlatform).map(Platform::name).map(String::toLowerCase).collect(Collectors.toSet()));
    }

    @Path("/{platform}")
    @GET
    public List<String> listUsers(@HeaderParam("Authorization") final String authorization,
                                  @PathParam("global_user_id") final String globalUserId,
                                  @PathParam("platform") final String platformString) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.read").ensurePrincipal(globalUserId);
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        final Platform platform = Platform.valueOf(platformString.toUpperCase());
        return globalUser.getEntries().stream().filter(e -> e.getPlatform().equals(platform)).map(GlobalUser.Entry::getUserId).collect(Collectors.toList());
    }

    @Path("/{platform}/{user_id}")
    @GET
    public User getUser(@HeaderParam("Authorization") final String authorization,
                        @PathParam("global_user_id") final String globalUserId,
                        @PathParam("platform") final String platform,
                        @PathParam("user_id") final String userId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.read").ensurePrincipal(globalUserId);
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        final Optional<GlobalUser.Entry> entry = globalUser.getEntries().stream().filter(e -> e.getPlatform().name().toLowerCase().equals(platform) && e.getUserId().equals(userId)).findFirst();
        if(!entry.isPresent())
            throw new NotFoundException();
        return new User(globalUser.getId(), entry.get().getPlatform(), entry.get().getUserId(), entry.get().getDisplayName());
    }

    @Path("/{platform}/{user_id}")
    @PUT
    public User linkUser(@HeaderParam("Authorization") final String authorization,
                         @PathParam("global_user_id") final String globalUserId,
                         @PathParam("platform") final String platform,
                         @PathParam("user_id") final String userId, final PrivateUser user) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.write").ensurePrincipal(globalUserId);
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        final Optional<GlobalUser.Entry> entry = globalUser.getEntries().stream().filter(e -> e.getPlatform().name().toLowerCase().equals(platform) && e.getUserId().equals(userId)).findFirst();
        if(!entry.isPresent())
            throw new NotFoundException();
        return null; //TODO
    }

    @Path("/{platform}/{user_id}")
    @DELETE
    public Response unlinkUser(@HeaderParam("Authorization") final String authorization,
                               @PathParam("global_user_id") final String globalUserId,
                               @PathParam("platform") final String platform,
                               @PathParam("user_id") final String userId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.write").ensurePrincipal(globalUserId);
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        final Optional<GlobalUser.Entry> entry = globalUser.getEntries().stream().filter(e -> e.getPlatform().name().toLowerCase().equals(platform) && e.getUserId().equals(userId)).findFirst();
        if(!entry.isPresent())
            throw new NotFoundException();
        unlinkUser(globalUser, entry.get());
        return Response.noContent().build();
    }

    private void unlinkUser(final GlobalUser globalUser, final GlobalUser.Entry entry) {
        daoGlobalUser.removeChannel(globalUser, entry.getPlatform(), entry.getUserId());
    }
}
