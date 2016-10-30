package tv.v1x1.modules.core.api.resources.globaluser;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.modules.core.api.api.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/26/2016.
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

    @Inject
    public UsersResource(final DAOGlobalUser daoGlobalUser) {
        this.daoGlobalUser = daoGlobalUser;
    }

    @GET
    public List<String> listPlatforms(@PathParam("global_user_id") final String globalUserId) {
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        return ImmutableList.copyOf(globalUser.getEntries().stream().map(GlobalUser.Entry::getPlatform).map(Platform::name).map(String::toLowerCase).collect(Collectors.toSet()));
    }

    @Path("/{platform}")
    @GET
    public List<String> listUsers(@PathParam("global_user_id") final String globalUserId, @PathParam("platform") final String platformString) {
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        final Platform platform = Platform.valueOf(platformString.toUpperCase());
        return globalUser.getEntries().stream().filter(e -> e.getPlatform().equals(platform)).map(GlobalUser.Entry::getUserId).collect(Collectors.toList());
    }

    @Path("/{platform}/{user_id}")
    @GET
    public User getUser(@PathParam("global_user_id") final String globalUserId, @PathParam("platform") final String platform,
                        @PathParam("user_id") final String userId) {
        return null; //TODO
    }

    @Path("/{platform}/{user_id}")
    @PUT
    public User linkUser(@PathParam("global_user_id") final String globalUserId, @PathParam("platform") final String platform,
                         @PathParam("user_id") final String userId, final User user) {
        return null; //TODO
    }

    @Path("/{platform}/{user_id}")
    @DELETE
    public Response unlinkUser(@PathParam("global_user_id") final String globalUserId, @PathParam("platform") final String platform,
                               @PathParam("user_id") final String userId) {
        return null; //TODO
    }
}
