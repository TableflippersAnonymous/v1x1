package tv.v1x1.modules.core.api.resources.meta;

import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dao.DAOTwitchOauthToken;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.TwitchOauthToken;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.auth.TokenResponse;
import tv.v1x1.common.services.twitch.dto.users.PrivateUser;
import tv.v1x1.modules.core.api.api.AuthTokenResponse;
import tv.v1x1.modules.core.api.api.LongTermTokenRequest;
import tv.v1x1.modules.core.api.api.StateResponse;
import tv.v1x1.modules.core.api.api.TwitchOauthCode;
import tv.v1x1.modules.core.api.auth.AuthorizationContext;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /meta
    /self - GET: redirect to /global-users/{userid} for currently logged in user
 */
@Path("/api/v1/meta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MetaResource {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DAOGlobalUser daoGlobalUser;
    private final DAOTwitchOauthToken daoTwitchOauthToken;
    private final Authorizer authorizer;
    private final TwitchApi twitchApi;
    private final KeyValueStore stateStore;
    private final SecureRandom secureRandom = new SecureRandom();

    @Inject
    public MetaResource(final DAOManager daoManager, final Authorizer authorizer, final TwitchApi twitchApi, final KeyValueStore stateStore) {
        this.daoGlobalUser = daoManager.getDaoGlobalUser();
        this.daoTwitchOauthToken = daoManager.getDaoTwitchOauthToken();
        this.authorizer = authorizer;
        this.twitchApi = twitchApi;
        this.stateStore = stateStore;
    }

    @Path("/self")
    @GET
    public Response getSelf(@HeaderParam("Authorization") final String authorization) {
        try {
            return Response.temporaryRedirect(new URI(null, null, "/api/v1/global-users/" + authorizer.forAuthorization(authorization).getGlobalUser().getId(), null)).build();
        } catch (URISyntaxException e) {
            LOG.warn("Invalid URI:", e);
            return Response.serverError().build();
        }
    }

    @Path("/login/twitch")
    @POST
    public AuthTokenResponse getAuthToken(final TwitchOauthCode twitchOauthCode) {
        final TokenResponse tokenResponse = twitchApi.getOauth2().getToken(twitchOauthCode.getOauthCode(), useState(twitchOauthCode.getOauthState()));
        if(tokenResponse == null)
            throw new BadRequestException();
        final PrivateUser privateUser = twitchApi.withToken(tokenResponse.getAccessToken()).getUsers().getUser();
        final GlobalUser globalUser = daoGlobalUser.getOrCreate(Platform.TWITCH, privateUser.getName(), privateUser.getDisplayName());
        daoTwitchOauthToken.put(new TwitchOauthToken(globalUser.getId(), privateUser.getName(), tokenResponse.getAccessToken(), tokenResponse.getScope()));
        return authorizer.getAuthorizationFromPrincipal(new Authorizer.Principal(globalUser.toCore(), null));
    }

    @Path("/state")
    @GET
    public StateResponse getState() {
        final byte[] state = new byte[32];
        secureRandom.nextBytes(state);
        stateStore.put(state, Longs.toByteArray(new Date().getTime() + 3600000));
        return new StateResponse(BaseEncoding.base64Url().encode(state), 3600000);
    }

    @Path("/long-term-token")
    @POST
    public AuthTokenResponse getLongTermToken(@HeaderParam("Authorization") final String authorization,
                                              final LongTermTokenRequest longTermTokenRequest) {
        final AuthorizationContext authorizationContext = authorizer.forAuthorization(authorization).ensurePermission("api.meta.long-term-token");
        final Authorizer.Principal principal = new Authorizer.Principal(authorizationContext.getGlobalUser(), Sets.intersection(authorizationContext.getPermissions(), new HashSet<>(longTermTokenRequest.getPermissions())).stream().map(Permission::new).collect(Collectors.toSet()));
        return authorizer.getAuthorizationFromPrincipal(principal);
    }

    private String useState(final String stateStr) {
        final byte[] state = BaseEncoding.base64Url().decode(stateStr);
        final byte[] expiryBytes = stateStore.get(state);
        if(expiryBytes == null)
            throw new BadRequestException("State not found");
        stateStore.delete(state);
        final long expiry = Longs.fromByteArray(expiryBytes);
        if(new Date(expiry).before(new Date()))
            throw new BadRequestException("State expired");
        return stateStr;
    }
}
