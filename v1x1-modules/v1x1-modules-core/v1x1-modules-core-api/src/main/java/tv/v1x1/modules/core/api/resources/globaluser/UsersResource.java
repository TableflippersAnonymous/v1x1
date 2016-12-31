package tv.v1x1.modules.core.api.resources.globaluser;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dao.DAOTwitchOauthToken;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.TwitchOauthToken;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.auth.TokenResponse;
import tv.v1x1.modules.core.api.api.ApiList;
import tv.v1x1.modules.core.api.api.TwitchOauthCode;
import tv.v1x1.modules.core.api.api.User;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.BadRequestException;
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
import java.util.List;
import java.util.Optional;
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
    private final DAOTwitchOauthToken daoTwitchOauthToken;
    private final Authorizer authorizer;
    private final TwitchApi twitchApi;

    @Inject
    public UsersResource(final DAOManager daoManager, final Authorizer authorizer, final TwitchApi twitchApi) {
        this.daoGlobalUser = daoManager.getDaoGlobalUser();
        this.daoTwitchOauthToken = daoManager.getDaoTwitchOauthToken();
        this.authorizer = authorizer;
        this.twitchApi = twitchApi;
    }

    @GET
    public ApiList<String> listPlatforms(@HeaderParam("Authorization") final String authorization,
                                         @PathParam("global_user_id") final String globalUserId) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.read").ensurePrincipal(globalUserId);
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        return new ApiList<>(ImmutableList.copyOf(globalUser.getEntries().stream().map(GlobalUser.Entry::getPlatform).map(Platform::name).map(String::toLowerCase).collect(Collectors.toSet())));
    }

    @Path("/{platform}")
    @GET
    public ApiList<String> listUsers(@HeaderParam("Authorization") final String authorization,
                                     @PathParam("global_user_id") final String globalUserId,
                                     @PathParam("platform") final String platformString) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.read").ensurePrincipal(globalUserId);
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        final Platform platform = Platform.valueOf(platformString.toUpperCase());
        return new ApiList<>(globalUser.getEntries().stream().filter(e -> e.getPlatform().equals(platform)).map(GlobalUser.Entry::getUserId).collect(Collectors.toList()));
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
                         @PathParam("user_id") final String userId, final TwitchOauthCode twitchOauthCode) {
        authorizer.forAuthorization(authorization).ensurePermission("api.global_users.write").ensurePrincipal(globalUserId);
        if(!platform.equalsIgnoreCase("twitch"))
            throw new NotFoundException();
        final GlobalUser globalUser = daoGlobalUser.getById(UUID.fromString(globalUserId));
        if(globalUser == null)
            throw new NotFoundException();
        final Optional<GlobalUser.Entry> entry = globalUser.getEntries().stream().filter(e -> e.getPlatform().name().toLowerCase().equals(platform) && e.getUserId().equals(userId)).findFirst();
        if(!entry.isPresent())
            throw new NotFoundException();
        final TokenResponse tokenResponse = twitchApi.getOauth2().getToken(twitchOauthCode.getOauthCode(), twitchOauthCode.getOauthState());
        if(tokenResponse == null)
            throw new BadRequestException();
        daoTwitchOauthToken.put(new TwitchOauthToken(globalUser.getId(), userId, tokenResponse.getAccessToken(), tokenResponse.getScope()));
        final String displayName = twitchApi.withToken(tokenResponse.getAccessToken()).getUsers().getUser().getDisplayName();
        unlinkUserIfLinked(Platform.TWITCH, userId);
        linkUser(globalUser, Platform.TWITCH, userId, displayName);
        return new User(globalUser.getId(), entry.get().getPlatform(), entry.get().getUserId(), entry.get().getDisplayName());
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

    private void linkUser(final GlobalUser globalUser, final Platform platform, final String userId, final String displayName) {
        daoGlobalUser.addUser(globalUser, platform, userId, displayName);
    }

    private void unlinkUserIfLinked(final Platform platform, final String userId) {
        final GlobalUser globalUser = daoGlobalUser.getByUser(platform, userId);
        if(globalUser == null)
            return;
        final Optional<GlobalUser.Entry> entry = globalUser.getEntries().stream().filter(e -> e.getPlatform() == platform && e.getUserId().equals(userId)).findFirst();
        if(!entry.isPresent())
            return;
        unlinkUser(globalUser, entry.get());
    }

    private void unlinkUser(final GlobalUser globalUser, final GlobalUser.Entry entry) {
        daoGlobalUser.removeUser(globalUser, entry.getPlatform(), entry.getUserId());
    }
}
