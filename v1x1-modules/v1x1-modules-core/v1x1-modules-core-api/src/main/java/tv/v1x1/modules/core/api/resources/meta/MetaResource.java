package tv.v1x1.modules.core.api.resources.meta;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dao.DAOTwitchOauthToken;
import tv.v1x1.common.dto.db.Channel;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.dto.db.TwitchOauthToken;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.auth.TokenResponse;
import tv.v1x1.common.services.twitch.dto.users.PrivateUser;
import tv.v1x1.modules.core.api.api.ApiPrimitive;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by naomi on 10/26/2016.
 */
/*
  /meta
    /self - GET: global user ID for currently logged in user
 */
@Path("/api/v1/meta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MetaResource {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DAOGlobalUser daoGlobalUser;
    private final DAOTwitchOauthToken daoTwitchOauthToken;
    private final DAOTenant daoTenant;
    private final DAOTenantGroup daoTenantGroup;
    private final Authorizer authorizer;
    private final TwitchApi twitchApi;
    private final KeyValueStore stateStore;
    private final SecureRandom secureRandom = new SecureRandom();

    @Inject
    public MetaResource(final DAOManager daoManager, final Authorizer authorizer, final TwitchApi twitchApi, final KeyValueStore stateStore) {
        this.daoGlobalUser = daoManager.getDaoGlobalUser();
        this.daoTwitchOauthToken = daoManager.getDaoTwitchOauthToken();
        this.daoTenant = daoManager.getDaoTenant();
        this.daoTenantGroup = daoManager.getDaoTenantGroup();
        this.authorizer = authorizer;
        this.twitchApi = twitchApi;
        this.stateStore = stateStore;
    }

    @Path("/self")
    @GET
    public ApiPrimitive<String> getSelf(@HeaderParam("Authorization") final String authorization) {
        return new ApiPrimitive<>(authorizer.forAuthorization(authorization).getGlobalUser().getId().toString());
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
        final Channel channel = daoTenant.getChannel(Platform.TWITCH, "#" + privateUser.getName());
        final Tenant tenant = daoTenant.getOrCreate(Platform.TWITCH, "#" + privateUser.getName(), "#" + privateUser.getName());
        final boolean firstSetup = !daoTenantGroup.getAllGroupsByTenant(tenant.toCore()).iterator().hasNext() || channel == null;
        grantOwner(tenant, globalUser);
        if(firstSetup)
            createStartingGroups(tenant, "#" + privateUser.getName());
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

    private void grantOwner(final Tenant tenant, final GlobalUser globalUser) {
        final Optional<TenantGroup> optionalTenantGroup = StreamSupport.stream(daoTenantGroup.getAllGroupsByTenant(tenant.toCore()).spliterator(), false)
                .filter(tg -> tg.getName().equals("Owner")).findFirst();
        final TenantGroup tenantGroup = optionalTenantGroup.orElseGet(() -> daoTenantGroup.createGroup(tenant.toCore(), "Owner"));
        daoTenantGroup.addPermissionsToGroup(tenantGroup, ImmutableSet.of(
                new Permission("api.permissions.read"), new Permission("api.permissions.write"),
                new Permission("api.tenants.config.read"), new Permission("api.tenants.config.write"),
                new Permission("api.tenants.read"), new Permission("api.tenants.write"),
                new Permission("api.tenants.link"), new Permission("api.tenants.unlink"),
                new Permission("api.tenants.message")
        ));
        daoTenantGroup.addUserToGroup(tenantGroup, globalUser.toCore());
    }

    private void createStartingGroups(final Tenant tenant, final String channelId) {
        createStartingGroup(tenant, channelId, "Broadcaster", ImmutableSet.of("BROADCASTER"),
                ImmutableSet.of("link_purger.permit", "link_purger.whitelisted", "caster.user", "fact.modify", "quote.use", "timer.modify"));
        createStartingGroup(tenant, channelId, "Mods", ImmutableSet.of("MODERATOR", "ADMIN", "STAFF", "GLOBAL_MOD"),
                ImmutableSet.of("link_purger.permit", "link_purger.whitelisted", "caster.user", "fact.modify", "quote.use", "timer.modify"));
        createStartingGroup(tenant, channelId, "Subs", ImmutableSet.of("SUBSCRIBER"),
                ImmutableSet.of("link_purger.whitelisted"));
        createStartingGroup(tenant, channelId, "Everyone", ImmutableSet.of("_DEFAULT_"),
                ImmutableSet.of("uptime.use", "quote.use"));
        createStartingGroup(tenant, channelId, "Web_Read", ImmutableSet.of(),
                ImmutableSet.of("api.permissions.read", "api.tenants.config.read", "api.tenants.read"));
        createStartingGroup(tenant, channelId, "Web_Edit", ImmutableSet.of(),
                ImmutableSet.of("api.permissions.read", "api.tenants.config.read", "api.tenants.read",
                        "api.tenants.config.write", "api.tenants.write", "api.tenants.link",
                        "api.tenants.unlink", "api.tenants.message"));
        createStartingGroup(tenant, channelId, "Web_All", ImmutableSet.of(),
                ImmutableSet.of("api.permissions.read", "api.tenants.config.read", "api.tenants.read",
                        "api.tenants.config.write", "api.tenants.write", "api.tenants.link",
                        "api.tenants.unlink", "api.tenants.message", "api.permissions.write"));
    }

    private void createStartingGroup(final Tenant tenant, final String channelId, final String name, final Set<String> platformGroups, final Set<String> nodes) {
        final TenantGroup tenantGroup = daoTenantGroup.createGroup(tenant.toCore(), name);
        daoTenantGroup.addPermissionsToGroup(tenantGroup, nodes.stream().map(Permission::new).collect(Collectors.toSet()));
        for(final String platformGroup : platformGroups)
            daoTenantGroup.setChannelPlatformMapping(Platform.TWITCH, channelId, platformGroup, tenantGroup);
    }
}
