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
import tv.v1x1.common.dto.db.ChannelGroup;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.dto.db.TwitchOauthToken;
import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.user.Connection;
import tv.v1x1.common.services.discord.dto.user.User;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.state.NoSuchUserException;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.auth.TokenResponse;
import tv.v1x1.common.services.twitch.dto.users.PrivateUser;
import tv.v1x1.modules.core.api.api.rest.ApiPrimitive;
import tv.v1x1.modules.core.api.api.rest.AuthTokenResponse;
import tv.v1x1.modules.core.api.api.rest.LongTermTokenRequest;
import tv.v1x1.modules.core.api.api.rest.OauthCode;
import tv.v1x1.modules.core.api.api.rest.StateResponse;
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
import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cobi on 10/26/2016.
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
    private final DiscordApi discordApi;
    private final TwitchDisplayNameService twitchDisplayNameService;
    private final KeyValueStore stateStore;
    private final SecureRandom secureRandom = new SecureRandom();

    @Inject
    public MetaResource(final DAOManager daoManager, final Authorizer authorizer, final TwitchApi twitchApi, final DiscordApi discordApi, final KeyValueStore stateStore, final TwitchDisplayNameService twitchDisplayNameService) {
        this.daoGlobalUser = daoManager.getDaoGlobalUser();
        this.daoTwitchOauthToken = daoManager.getDaoTwitchOauthToken();
        this.daoTenant = daoManager.getDaoTenant();
        this.daoTenantGroup = daoManager.getDaoTenantGroup();
        this.authorizer = authorizer;
        this.twitchApi = twitchApi;
        this.discordApi = discordApi;
        this.stateStore = stateStore;
        this.twitchDisplayNameService = twitchDisplayNameService;
    }

    @Path("/self")
    @GET
    public ApiPrimitive<String> getSelf(@HeaderParam("Authorization") final String authorization) {
        return new ApiPrimitive<>(authorizer.forAuthorization(authorization).getGlobalUser().getId().toString());
    }

    @Path("/login/twitch")
    @POST
    public AuthTokenResponse getTwitchAuthToken(final OauthCode oauthCode) {
        final TokenResponse tokenResponse = twitchApi.getOauth2().getToken(oauthCode.getOauthCode(), useState(oauthCode.getOauthState()));
        if(tokenResponse == null)
            throw new BadRequestException();
        final PrivateUser privateUser = twitchApi.withToken(tokenResponse.getAccessToken()).getUsers().getUser();
        final GlobalUser globalUser = daoGlobalUser.getOrCreate(Platform.TWITCH, String.valueOf(privateUser.getId()), privateUser.getDisplayName());
        daoTwitchOauthToken.put(new TwitchOauthToken(globalUser.getId(), privateUser.getName(), tokenResponse.getAccessToken(), tokenResponse.getScope()));
        final Channel channel = daoTenant.getChannel(Platform.TWITCH, String.valueOf(privateUser.getId()));
        final Tenant tenant = daoTenant.getOrCreate(Platform.TWITCH, String.valueOf(privateUser.getId()), String.valueOf(privateUser.getId()) + ":main", privateUser.getDisplayName());
        final boolean firstSetup = !daoTenantGroup.getAllGroupsByTenant(tenant.toCore(daoTenant)).iterator().hasNext() || channel == null;
        grantOwner(tenant, globalUser);
        if(firstSetup)
            createTwitchStartingGroups(tenant, String.valueOf(privateUser.getId()));
        return authorizer.getAuthorizationFromPrincipal(new Authorizer.Principal(globalUser.toCore(), null));
    }

    @Path("/login/discord")
    @POST
    public AuthTokenResponse getDiscordAuthToken(final OauthCode oauthCode) {
        final tv.v1x1.common.services.discord.dto.oauth2.TokenResponse tokenResponse = discordApi
                .getOauth2().getToken(oauthCode.getOauthCode(), useState(oauthCode.getOauthState()));
        if(tokenResponse == null)
            throw new BadRequestException();
        final User user = discordApi.withToken(tokenResponse.getAccessToken(), tokenResponse.getTokenType()).getUsers().getCurrentUser();
        final List<Connection> connections = discordApi.withToken(tokenResponse.getAccessToken(), tokenResponse.getTokenType()).getUsers().getUserConnections();
        final Optional<Connection> twitchConnection = connections.stream().filter(connection -> connection.getType().equals("twitch") && !connection.isRevoked()).findFirst();
        GlobalUser globalUser = daoGlobalUser.getByUser(Platform.DISCORD, user.getId());
        if(twitchConnection.isPresent()) {
            final GlobalUser twitchGlobalUser = daoGlobalUser.getByUser(Platform.TWITCH, twitchConnection.get().getId());
            if(globalUser == null && twitchGlobalUser == null) {
                // Neither exist, create with Discord, try to add Twitch.
                globalUser = daoGlobalUser.getOrCreate(Platform.DISCORD, user.getId(), user.getUsername());
                try {
                    globalUser = daoGlobalUser.addUser(globalUser, Platform.TWITCH, twitchConnection.get().getId(), twitchDisplayNameService.getDisplayNameFromUserId(twitchConnection.get().getId()));
                } catch (final NoSuchUserException e) {
                    LOG.warn("Got exception", e);
                }
            } else if(globalUser == null) {
                // Discord doesn't exist, Twitch does.  Add Discord.
                globalUser = daoGlobalUser.addUser(twitchGlobalUser, Platform.DISCORD, user.getId(), user.getUsername());
            } else if(twitchGlobalUser == null) {
                // Twitch doesn't exist, Discord does.  Add Twitch.
                try {
                    globalUser = daoGlobalUser.addUser(globalUser, Platform.TWITCH, twitchConnection.get().getId(), twitchDisplayNameService.getDisplayNameFromUserId(twitchConnection.get().getId()));
                } catch (final NoSuchUserException e) {
                    LOG.warn("Got exception", e);
                }
            }
        } else {
            globalUser = daoGlobalUser.getOrCreate(Platform.DISCORD, user.getId(), user.getUsername());
        }
        if(tokenResponse.getGuild() != null) {
            final ChannelGroup channelGroup = daoTenant.getChannelGroup(Platform.DISCORD, tokenResponse.getGuild().getId());
            final Tenant tenant = daoTenant.getOrCreate(Platform.DISCORD, tokenResponse.getGuild().getId(), tokenResponse.getGuild().getName());
            final boolean firstSetup = !daoTenantGroup.getAllGroupsByTenant(tenant.toCore(daoTenant)).iterator().hasNext() || channelGroup == null;
            grantOwner(tenant, globalUser);
            if (firstSetup)
                createDiscordStartingGroups(tenant, tokenResponse.getGuild().getId());
        }
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
        final Optional<TenantGroup> optionalTenantGroup = StreamSupport.stream(daoTenantGroup.getAllGroupsByTenant(tenant.toCore(daoTenant)).spliterator(), false)
                .filter(tg -> tg.getName().equals("Owner")).findFirst();
        final TenantGroup tenantGroup = optionalTenantGroup.orElseGet(() -> daoTenantGroup.createGroup(tenant.toCore(daoTenant), "Owner"));
        daoTenantGroup.addPermissionsToGroup(tenantGroup, ImmutableSet.of(
                new Permission("api.permissions.read"), new Permission("api.permissions.write"),
                new Permission("api.tenants.config.read"), new Permission("api.tenants.config.write"),
                new Permission("api.tenants.read"), new Permission("api.tenants.write"),
                new Permission("api.tenants.link"), new Permission("api.tenants.unlink"),
                new Permission("api.tenants.message")
        ));
        daoTenantGroup.addUserToGroup(tenantGroup, globalUser.toCore());
    }

    private void createTwitchStartingGroups(final Tenant tenant, final String channelGroupId) {
        createStartingGroup(tenant, Platform.TWITCH, channelGroupId, "Broadcaster", ImmutableSet.of("BROADCASTER"),
                ImmutableSet.of("link_purger.permit", "link_purger.whitelisted", "caster.user", "fact.modify", "quote.use", "timer.modify"));
        createStartingGroup(tenant, Platform.TWITCH, channelGroupId, "Mods", ImmutableSet.of("MODERATOR", "ADMIN", "STAFF", "GLOBAL_MOD"),
                ImmutableSet.of("link_purger.permit", "link_purger.whitelisted", "caster.user", "fact.modify", "quote.use", "timer.modify"));
        createStartingGroup(tenant, Platform.TWITCH, channelGroupId, "Subs", ImmutableSet.of("SUBSCRIBER"),
                ImmutableSet.of("link_purger.whitelisted"));
        createStartingGroup(tenant, Platform.TWITCH, channelGroupId, "Everyone", ImmutableSet.of("_DEFAULT_"),
                ImmutableSet.of("uptime.use", "quote.use"));
        createStartingGroup(tenant, Platform.TWITCH, channelGroupId, "Web_Read", ImmutableSet.of(),
                ImmutableSet.of("api.permissions.read", "api.tenants.config.read", "api.tenants.read"));
        createStartingGroup(tenant, Platform.TWITCH, channelGroupId, "Web_Edit", ImmutableSet.of(),
                ImmutableSet.of("api.permissions.read", "api.tenants.config.read", "api.tenants.read",
                        "api.tenants.config.write", "api.tenants.write", "api.tenants.link",
                        "api.tenants.unlink", "api.tenants.message"));
        createStartingGroup(tenant, Platform.TWITCH, channelGroupId, "Web_All", ImmutableSet.of(),
                ImmutableSet.of("api.permissions.read", "api.tenants.config.read", "api.tenants.read",
                        "api.tenants.config.write", "api.tenants.write", "api.tenants.link",
                        "api.tenants.unlink", "api.tenants.message", "api.permissions.write"));
    }

    private void createDiscordStartingGroups(final Tenant tenant, final String channelGroupId) {
        createStartingGroup(tenant, Platform.DISCORD, channelGroupId, "Everyone", ImmutableSet.of("_DEFAULT_"),
                ImmutableSet.of("uptime.use", "quote.use"));
        createStartingGroup(tenant, Platform.DISCORD, channelGroupId, "Web_Read", ImmutableSet.of(),
                ImmutableSet.of("api.permissions.read", "api.tenants.config.read", "api.tenants.read"));
        createStartingGroup(tenant, Platform.DISCORD, channelGroupId, "Web_Edit", ImmutableSet.of(),
                ImmutableSet.of("api.permissions.read", "api.tenants.config.read", "api.tenants.read",
                        "api.tenants.config.write", "api.tenants.write", "api.tenants.link",
                        "api.tenants.unlink", "api.tenants.message"));
        createStartingGroup(tenant, Platform.DISCORD, channelGroupId, "Web_All", ImmutableSet.of(),
                ImmutableSet.of("api.permissions.read", "api.tenants.config.read", "api.tenants.read",
                        "api.tenants.config.write", "api.tenants.write", "api.tenants.link",
                        "api.tenants.unlink", "api.tenants.message", "api.permissions.write"));
    }

    private void createStartingGroup(final Tenant tenant, final Platform platform, final String channelGroupId, final String name, final Set<String> platformGroups, final Set<String> nodes) {
        final TenantGroup tenantGroup = daoTenantGroup.createGroup(tenant.toCore(daoTenant), name);
        daoTenantGroup.addPermissionsToGroup(tenantGroup, nodes.stream().map(Permission::new).collect(Collectors.toSet()));
        for(final String platformGroup : platformGroups)
            daoTenantGroup.setChannelGroupPlatformMapping(platform, channelGroupId, platformGroup, tenantGroup);
    }
}
