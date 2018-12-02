package tv.v1x1.modules.core.api.resources.meta;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.db.ChannelConfiguration;
import tv.v1x1.common.dto.db.ChannelGroupConfiguration;
import tv.v1x1.common.dto.db.Configuration;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.TenantConfiguration;
import tv.v1x1.common.dto.messages.events.ChannelConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.ChannelGroupConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.ConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.services.cache.SharedCache;
import tv.v1x1.common.services.persistence.ConfigurationCacheManager;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.KeyValueStore;
import tv.v1x1.common.services.queue.MessageQueueManager;
import tv.v1x1.common.services.spotify.SpotifyApi;
import tv.v1x1.common.services.spotify.dto.AuthorizationResponse;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.auth.TokenResponse;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.modules.core.api.ApiModule;
import tv.v1x1.modules.core.api.api.rest.StateResponse;
import tv.v1x1.modules.core.api.auth.AuthorizationContext;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Path("/api/v1/meta/inbound")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InboundResource {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private static final JsonParser JSON_PARSER = new JsonParser();

    private final KeyValueStore stateStore;
    private final Authorizer authorizer;
    private final SpotifyApi spotifyApi;
    private final TwitchApi twitchApi;
    private final DAOManager daoManager;
    private final ApiModule apiModule;
    private final MessageQueueManager messageQueueManager;
    private final ConfigurationCacheManager configurationCacheManager;
    private final SecureRandom secureRandom = new SecureRandom();

    @Inject
    public InboundResource(final KeyValueStore stateStore, final Authorizer authorizer, final SpotifyApi spotifyApi,
                           final DAOManager daoManager, final ApiModule apiModule,
                           final MessageQueueManager messageQueueManager,
                           final ConfigurationCacheManager configurationCacheManager) {
        this.stateStore = stateStore;
        this.authorizer = authorizer;
        this.spotifyApi = spotifyApi; //FIXME: Guicify.
        this.twitchApi = new TwitchApi(new String(apiModule.requireCredential("Common|TMI|ClientId")),
                "", new String(apiModule.requireCredential("Common|TMI|ClientSecret")),
                new String(apiModule.requireCredential("Common|TMI|RedirectUri")));
        this.daoManager = daoManager;
        this.apiModule = apiModule;
        this.messageQueueManager = messageQueueManager;
        this.configurationCacheManager = configurationCacheManager;
    }

    @Path("/spotify")
    @GET
    public Response handleSpotifyOAuth(@QueryParam("code") final String code, @QueryParam("state") final String state,
                                       @QueryParam("error") final String error) {
        if(error != null || code == null || state == null)
            return Response.temporaryRedirect(URI.create("/")).build();
        final byte[][] decodedStateData = CompositeKey.getKeys(useState(state, "spotify"));
        final AuthorizationResponse authorizationResponse = spotifyApi.getOAuth2().authorize(code);
        writeUserConfigEntry(decodedStateData, "spotify", "refresh_token",
                authorizationResponse.getRefreshToken());
        return Response.temporaryRedirect(URI.create("/")).build();
    }

    @Path("/twitch")
    @GET
    public Response handleTwitchOAuth(@QueryParam("code") final String code, @QueryParam("state") final String state,
                                      @QueryParam("error") final String error) {
        if(error != null || code == null || state == null)
            return Response.temporaryRedirect(URI.create("/")).build();
        final byte[][] decodedStateData = CompositeKey.getKeys(useState(state, "twitch"));
        final TokenResponse tokenResponse = twitchApi.getOauth2().getToken(code, state);
        writeUserConfigEntry(decodedStateData, "tmi", "oauth_token", tokenResponse.getAccessToken());
        return Response.temporaryRedirect(URI.create("/")).build();
    }

    @Path("/state/{service}/{tenantid}")
    @GET
    public StateResponse getState(@HeaderParam("Authorization") final String authorization,
                                  @PathParam("service") final String service,
                                  @PathParam("tenantid") final String tenantId,
                                  @QueryParam("platform") final String platform,
                                  @QueryParam("cgid") final String channelGroupId,
                                  @QueryParam("cid") final String channelId) {
        final AuthorizationContext authorizationContext = authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization);
        authorizationContext.ensurePermission("api.tenants.config.write");
        final Tenant tenant = Optional.ofNullable(daoManager.getDaoTenant().getById(UUID.fromString(tenantId)))
                .map(x -> x.toCore(daoManager.getDaoTenant()))
                .orElseThrow(NotFoundException::new);
        if(platform != null) {
            if(channelGroupId == null)
                throw new BadRequestException("Expected cgid when platform specified.");
            final Optional<ChannelGroup> channelGroup = tenant.getChannelGroup(Platform.valueOf(platform), channelGroupId);
            if(!channelGroup.isPresent())
                throw new BadRequestException("Invalid platform/cgid.");
            if(channelId != null && !channelGroup.get().getChannel(channelId).isPresent())
                throw new BadRequestException("Invalid cid.");
        } else if(channelGroupId != null)
            throw new BadRequestException("Expected platform when cgid specified.");
        else if(channelId != null)
            throw new BadRequestException("Expected cgid/platform when cid specified.");
        final String state = makeState(service, CompositeKey.makeKey(tenantId,
                Optional.ofNullable(platform).orElse(""),
                Optional.ofNullable(channelGroupId).orElse(""),
                Optional.ofNullable(channelId).orElse("")));
        return new StateResponse(state, 3600000);
    }

    private void writeUserConfigEntry(final byte[][] decodedStateData, final String moduleName, final String key,
                                      final String value) {
        if(decodedStateData.length != 4)
            throw new BadRequestException("Invalid state");
        writeUserConfigEntry(new String(decodedStateData[0]),
                decodedStateData[1].length == 0 ? null : new String(decodedStateData[1]),
                decodedStateData[2].length == 0 ? null : new String(decodedStateData[2]),
                decodedStateData[3].length == 0 ? null : new String(decodedStateData[3]),
                moduleName, key, value);
    }

    private void writeUserConfigEntry(final String tenantId, final String platform, final String channelGroupId,
                                      final String channelId, final String moduleName, final String key,
                                      final String value) {
        final Module module = new Module(moduleName);
        final Tenant tenant = Optional.ofNullable(daoManager.getDaoTenant().getById(UUID.fromString(tenantId)))
                .map(x -> x.toCore(daoManager.getDaoTenant()))
                .orElseThrow(NotFoundException::new);
        if(platform == null)
            writeUserConfigEntry(tenant, module, key, value);
        else if(channelId == null) {
            final ChannelGroup channelGroup = tenant.getChannelGroup(Platform.valueOf(platform), channelGroupId)
                    .orElseThrow(BadRequestException::new);
            writeUserConfigEntry(channelGroup, module, key, value);
        } else {
            final Channel channel = tenant.getChannel(Platform.valueOf(platform), channelGroupId, channelId)
                    .orElseThrow(BadRequestException::new);
            writeUserConfigEntry(channel, module, key, value);
        }
    }

    private void writeUserConfigEntry(final Tenant tenant, final Module module, final String key, final String value) {
        final TenantConfiguration tenantConfiguration =
                Optional.ofNullable(daoManager.getDaoTenantConfiguration().get(module, tenant))
                        .orElse(new TenantConfiguration(module.getName(), tenant.getId().getValue(), "{}"));
        final TenantConfiguration newTenantConfiguration = new TenantConfiguration(
                tenantConfiguration.getModule(),
                tenantConfiguration.getTenantId(),
                setConfigEntry(tenantConfiguration.getJson(), key, value)
        );
        daoManager.getDaoTenantConfiguration().put(newTenantConfiguration);
        save(module, newTenantConfiguration, configurationCacheManager.getTenantCache(module), tenant,
                tv.v1x1.common.dto.core.Tenant.KEY_CODEC.encode(tenant),
                new TenantConfigChangeEvent(apiModule.toDto(), module, tenant));
    }

    private void writeUserConfigEntry(final ChannelGroup channelGroup, final Module module, final String key,
                                      final String value) {
        final ChannelGroupConfiguration channelGroupConfiguration =
                Optional.ofNullable(daoManager.getDaoChannelGroupConfiguration().get(module, channelGroup))
                        .orElse(new ChannelGroupConfiguration(module.getName(), channelGroup.getTenant().getId().getValue(),
                                channelGroup.getPlatform(), channelGroup.getId(), false, "{}"));
        final ChannelGroupConfiguration newChannelGroupConfiguration = new ChannelGroupConfiguration(
                channelGroupConfiguration.getModule(),
                channelGroupConfiguration.getTenantId(),
                channelGroupConfiguration.getPlatform(),
                channelGroupConfiguration.getChannelGroupId(),
                channelGroupConfiguration.isEnabled(),
                setConfigEntry(channelGroupConfiguration.getJson(), key, value)
        );
        daoManager.getDaoChannelGroupConfiguration().put(newChannelGroupConfiguration);
        save(module, newChannelGroupConfiguration, configurationCacheManager.getChannelGroupCache(module),
                channelGroup.getTenant(), tv.v1x1.common.dto.core.ChannelGroup.KEY_CODEC.encode(channelGroup),
                new ChannelGroupConfigChangeEvent(apiModule.toDto(), module, channelGroup));
    }

    private void writeUserConfigEntry(final Channel channel, final Module module, final String key,
                                      final String value) {
        final ChannelConfiguration channelConfiguration =
                Optional.ofNullable(daoManager.getDaoChannelConfiguration().get(module, channel))
                .orElse(new ChannelConfiguration(module.getName(), channel.getChannelGroup().getPlatform(),
                        channel.getChannelGroup().getId(), channel.getId(), false, "{}"));
        final ChannelConfiguration newChannelConfiguration = new ChannelConfiguration(
                channelConfiguration.getModule(),
                channelConfiguration.getPlatform(),
                channelConfiguration.getChannelGroupId(),
                channelConfiguration.getChannelId(),
                channelConfiguration.isEnabled(),
                setConfigEntry(channelConfiguration.getJson(), key, value)
        );
        daoManager.getDaoChannelConfiguration().put(newChannelConfiguration);
        save(module, newChannelConfiguration, configurationCacheManager.getChannelCache(module), channel.getTenant(),
                tv.v1x1.common.dto.core.Channel.KEY_CODEC.encode(channel),
                new ChannelConfigChangeEvent(apiModule.toDto(), module, channel));
    }

    private String setConfigEntry(final String json, final String key, final String value) {
        final JsonElement element = JSON_PARSER.parse(json);
        if(!element.isJsonObject())
            return json;
        final JsonObject object = element.getAsJsonObject();
        object.add(key, new JsonPrimitive(value));
        return GSON.toJson(object);
    }

    private void save(final Module module, final Configuration configuration, final SharedCache<byte[], byte[]> cache,
                      final Tenant tenant, final byte[] cacheKey, final ConfigChangeEvent event) {
        try (final SharedCache<byte[], byte[]> localCache = cache) {
            if(configuration.isEnabled())
                localCache.put(cacheKey, configuration.getJson().getBytes());
            else
                localCache.invalidate(cacheKey);
        } catch (final IOException e) {
            LOG.warn("Uncaught Exception:", e);
        }
        messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(event);
        apiModule.handleConfigChangeEvent(tenant, module);
    }

    private String makeState(final String service, final byte[] data) {
        final byte[] state = new byte[32];
        secureRandom.nextBytes(state);
        stateStore.put(CompositeKey.makeKey("stateauth".getBytes(), state), CompositeKey.makeKey(
                Longs.toByteArray(new Date().getTime() + 3600000),
                service.getBytes(),
                data));
        return BaseEncoding.base64Url().encode(state);
    }

    private byte[] useState(final String stateStr, final String service) {
        final byte[] state = BaseEncoding.base64Url().decode(stateStr);
        final byte[] stateDataBytes = stateStore.get(CompositeKey.makeKey("stateauth".getBytes(), state));
        if(stateDataBytes == null)
            throw new BadRequestException("State not found");
        final byte[][] stateData = CompositeKey.getKeys(stateDataBytes);
        if(stateData.length < 3)
            throw new BadRequestException("Invalid state");
        stateStore.delete(CompositeKey.makeKey("stateauth".getBytes(), state));
        final long expiry = Longs.fromByteArray(stateData[0]);
        if(new Date(expiry).before(new Date()))
            throw new BadRequestException("State expired");
        if(!Arrays.equals(stateData[1], service.getBytes()))
            throw new BadRequestException("Invalid state");
        return stateData[2];
    }
}
