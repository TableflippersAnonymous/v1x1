package tv.v1x1.modules.core.api.resources.meta;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dao.DAOPermissionDefinition;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dao.DAOTwitchOauthToken;
import tv.v1x1.common.dto.db.Channel;
import tv.v1x1.common.dto.db.ChannelGroup;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.PermissionDefinition;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.dto.db.TwitchOauthToken;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.user.Connection;
import tv.v1x1.common.services.discord.dto.user.User;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.auth.TokenResponse;
import tv.v1x1.common.services.twitch.dto.users.PrivateUser;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.modules.core.api.ApiModule;
import tv.v1x1.modules.core.api.api.rest.ApiPrimitive;
import tv.v1x1.modules.core.api.api.rest.AuthTokenResponse;
import tv.v1x1.modules.core.api.api.rest.LongTermTokenRequest;
import tv.v1x1.modules.core.api.api.rest.Module;
import tv.v1x1.modules.core.api.api.rest.OauthCode;
import tv.v1x1.modules.core.api.api.rest.StateResponse;
import tv.v1x1.modules.core.api.api.rest.SyncTenant;
import tv.v1x1.modules.core.api.api.rest.WebSync;
import tv.v1x1.modules.core.api.auth.AuthorizationContext;
import tv.v1x1.modules.core.api.auth.Authorizer;
import tv.v1x1.modules.core.api.services.ApiDataProvider;

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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final DAOPermissionDefinition daoPermissionDefinition;
    private final Authorizer authorizer;
    private final TwitchApi twitchApi;
    private final DiscordApi discordApi;
    private final TwitchDisplayNameService twitchDisplayNameService;
    private final KeyValueStore stateStore;
    private final ApiDataProvider dataProvider;
    private final SecureRandom secureRandom = new SecureRandom();

    @Inject
    public MetaResource(final DAOManager daoManager, final Authorizer authorizer, final TwitchApi twitchApi,
                        final DiscordApi discordApi, final KeyValueStore stateStore,
                        final TwitchDisplayNameService twitchDisplayNameService, final ApiModule module) {
        this.daoGlobalUser = daoManager.getDaoGlobalUser();
        this.daoTwitchOauthToken = daoManager.getDaoTwitchOauthToken();
        this.daoTenant = daoManager.getDaoTenant();
        this.daoTenantGroup = daoManager.getDaoTenantGroup();
        this.daoPermissionDefinition = daoManager.getDaoPermissionDefinition();
        this.authorizer = authorizer;
        this.twitchApi = twitchApi;
        this.discordApi = discordApi;
        this.stateStore = stateStore;
        this.twitchDisplayNameService = twitchDisplayNameService;
        this.dataProvider = new ApiDataProvider(daoManager, module);
    }

    @Path("/self")
    @GET
    public ApiPrimitive<String> getSelf(@HeaderParam("Authorization") final String authorization) {
        return new ApiPrimitive<>(authorizer.forAuthorization(authorization).getGlobalUser().getId().toString());
    }

    @Path("/sync")
    @GET
    public WebSync getFull(@HeaderParam("Authorization") final String authorization) {
        final AuthorizationContext authorizationContext = authorizer.forAuthorization(authorization);
        authorizationContext.ensurePermission("api.global_users.read");
        final tv.v1x1.modules.core.api.api.rest.GlobalUser currentUser = new tv.v1x1.modules.core.api.api.rest.GlobalUser(authorizationContext.getGlobalUser());
        final Map<String, Module> modules = dataProvider.getModules();
        final Map<String, SyncTenant> tenants = StreamSupport.stream(daoTenantGroup.getTenantsByUser(authorizationContext.getGlobalUser()).spliterator(), true)
                .filter(tenantId -> authorizer.tenantAuthorization(tenantId, authorization).hasPermission("api.tenants.read"))
                .map(tenantId -> dataProvider.getSyncTenant(authorizer.tenantAuthorization(tenantId, authorization), tenantId, modules.keySet()))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SyncTenant::getId, syncTenant -> syncTenant));
        return new WebSync(
                dataProvider.getConfiguration(),
                modules,
                currentUser,
                tenants
        );
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
        final Channel channel = daoTenant.getChannel(Platform.TWITCH, String.valueOf(privateUser.getId()) + ":main");
        final Tenant tenant = daoTenant.getOrCreate(Platform.TWITCH, String.valueOf(privateUser.getId()), String.valueOf(privateUser.getId()) + ":main", privateUser.getDisplayName());
        final boolean firstSetup = !daoTenantGroup.getAllGroupsByTenant(tenant.toCore(daoTenant)).iterator().hasNext() || channel == null;
        createStartingGroups(firstSetup, tenant, Platform.TWITCH, String.valueOf(privateUser.getId()), globalUser);
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
                } catch (final NoSuchTargetException e) {
                    LOG.warn("Got exception", e);
                }
            } else if(globalUser == null) {
                // Discord doesn't exist, Twitch does.  Add Discord.
                globalUser = daoGlobalUser.addUser(twitchGlobalUser, Platform.DISCORD, user.getId(), user.getUsername());
            } else if(twitchGlobalUser == null) {
                // Twitch doesn't exist, Discord does.  Add Twitch.
                try {
                    globalUser = daoGlobalUser.addUser(globalUser, Platform.TWITCH, twitchConnection.get().getId(), twitchDisplayNameService.getDisplayNameFromUserId(twitchConnection.get().getId()));
                } catch (final NoSuchTargetException e) {
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
            createStartingGroups(firstSetup, tenant, Platform.DISCORD, tokenResponse.getGuild().getId(), globalUser);
        }
        return authorizer.getAuthorizationFromPrincipal(new Authorizer.Principal(globalUser.toCore(), null));
    }

    @Path("/state")
    @GET
    public StateResponse getState() {
        final byte[] state = new byte[32];
        secureRandom.nextBytes(state);
        stateStore.put(CompositeKey.makeKey("stateunauth".getBytes(), state), Longs.toByteArray(new Date().getTime() + 3600000));
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
        final byte[] expiryBytes = stateStore.get(CompositeKey.makeKey("stateunauth".getBytes(), state));
        if(expiryBytes == null)
            throw new BadRequestException("State not found");
        stateStore.delete(CompositeKey.makeKey("stateauth".getBytes(), state));
        final long expiry = Longs.fromByteArray(expiryBytes);
        if(new Date(expiry).before(new Date()))
            throw new BadRequestException("State expired");
        return stateStr;
    }

    private void createStartingGroups(final boolean firstSetup, final Tenant tenant, final Platform platform, final String channelGroupId, final GlobalUser globalUser) {
        if(firstSetup)
            for(final DefaultGroup defaultGroup : DefaultGroup.values())
                createStartingGroup(tenant, platform, channelGroupId, globalUser, defaultGroup);
        else
            createStartingGroup(tenant, platform, channelGroupId, globalUser, DefaultGroup.OWNER);
    }

    private void createStartingGroup(final Tenant tenant, final Platform platform, final String channelGroupId, final GlobalUser globalUser, final DefaultGroup defaultGroup) {
        if(!defaultGroup.getPlatforms().contains(platform))
            return;
        final TenantGroup tenantGroup = createStartingGroup(
                tenant,
                platform,
                channelGroupId,
                defaultGroup.getGroupName(),
                defaultGroup.getPlatformMap().getOrDefault(platform, ImmutableSet.of()),
                getPermissionNodes(defaultGroup)
        );
        if(defaultGroup.equals(DefaultGroup.OWNER))
            daoTenantGroup.addUserToGroup(tenantGroup, globalUser.toCore());
    }

    private Set<String> getPermissionNodes(final DefaultGroup defaultGroup) {
        return ImmutableSet.copyOf(StreamSupport.stream(daoPermissionDefinition.getAll().spliterator(), false)
                .map(PermissionDefinition::getEntries)
                .flatMap(Collection::stream)
                .filter(permissionEntry -> permissionEntry.getDefaultGroups().contains(defaultGroup))
                .map(PermissionDefinition.PermissionEntry::getNode)
                .collect(Collectors.toSet()));
    }

    private TenantGroup createStartingGroup(final Tenant tenant, final Platform platform, final String channelGroupId,
                                            final String name, final Iterable<String> platformGroups, final Collection<String> nodes) {
        final Optional<TenantGroup> optionalTenantGroup = StreamSupport.stream(daoTenantGroup.getAllGroupsByTenant(tenant.toCore(daoTenant)).spliterator(), false)
                .filter(tg -> tg.getName().equals(name)).findFirst();
        final TenantGroup tenantGroup = optionalTenantGroup.orElseGet(() -> daoTenantGroup.createGroup(tenant.toCore(daoTenant), name));
        daoTenantGroup.addPermissionsToGroup(tenantGroup, nodes.stream().map(Permission::new).collect(Collectors.toSet()));
        for(final String platformGroup : platformGroups)
            daoTenantGroup.setChannelGroupPlatformMapping(platform, channelGroupId, platformGroup, tenantGroup);
        return tenantGroup;
    }
}
