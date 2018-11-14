package tv.v1x1.modules.core.api.resources.tenant;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.db.ChannelConfiguration;
import tv.v1x1.common.dto.db.ChannelGroupConfiguration;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TenantConfiguration;
import tv.v1x1.common.dto.db.UserConfigurationDefinition;
import tv.v1x1.common.dto.messages.events.ChannelConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.ChannelGroupConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.scanners.config.Permission;
import tv.v1x1.common.services.cache.SharedCache;
import tv.v1x1.common.services.persistence.ConfigurationCacheManager;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.queue.MessageQueueManager;
import tv.v1x1.modules.core.api.ApiModule;
import tv.v1x1.modules.core.api.api.rest.ChannelConfigurationTree;
import tv.v1x1.modules.core.api.api.rest.ChannelGroupConfigurationTree;
import tv.v1x1.modules.core.api.api.rest.Configuration;
import tv.v1x1.modules.core.api.api.rest.ConfigurationTree;
import tv.v1x1.modules.core.api.auth.Authorizer;
import tv.v1x1.modules.core.api.services.ApiDataProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/24/2016.
 */
/*
  /tenants
    /{tenant}
      /config
        /{module} - GET: TenantConfiguration for module; PUT: Update TenantConfiguration for module.
 */
@Path("/api/v1/tenants/{tenant}/config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigResource {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private static final JsonParser JSON_PARSER = new JsonParser();

    private final ApiModule apiModule;
    private final DAOManager daoManager;
    private final Authorizer authorizer;
    private final MessageQueueManager messageQueueManager;
    private final ConfigurationCacheManager configurationCacheManager;
    private final ApiDataProvider dataProvider;

    @Inject
    public ConfigResource(final ApiModule apiModule, final DAOManager daoManager, final Authorizer authorizer,
                          final MessageQueueManager messageQueueManager, final ConfigurationCacheManager configurationCacheManager) {
        this.apiModule = apiModule;
        this.daoManager = daoManager;
        this.authorizer = authorizer;
        this.messageQueueManager = messageQueueManager;
        this.configurationCacheManager = configurationCacheManager;
        this.dataProvider = new ApiDataProvider(daoManager, apiModule);
    }

    @Path("/{module}")
    @GET
    public Configuration getConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.read");
        final UserConfigurationDefinition userConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getUser(moduleName);
        if(userConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant tenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(tenant == null)
            throw new NotFoundException();
        return dataProvider.getTenantConfiguration(userConfigurationDefinition, moduleName, tenant.getId());
    }

    @Path("/{module}")
    @PUT
    public Configuration putConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName,
                                   final Configuration configuration) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.write");
        final UserConfigurationDefinition userConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getUser(moduleName);
        if(userConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant tenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(tenant == null)
            throw new NotFoundException();
        TenantConfiguration tenantConfiguration = daoManager.getDaoTenantConfiguration().get(new Module(moduleName), tenant.toCore(daoManager.getDaoTenant()));
        if(tenantConfiguration == null)
            tenantConfiguration = new TenantConfiguration(moduleName, tenant.getId(), "{}");
        final JsonElement configElement = JSON_PARSER.parse(tenantConfiguration.getJson());
        if(!configElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject config = configElement.getAsJsonObject();
        final JsonElement newConfigElement = JSON_PARSER.parse(configuration.getConfigJson());
        if(!newConfigElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject newConfig = newConfigElement.getAsJsonObject();
        final JsonObject finalConfig = dataProvider.sanitizeConfig(newConfig, config, userConfigurationDefinition, ImmutableSet.of(Permission.WRITE_ONLY, Permission.READ_WRITE));
        final TenantConfiguration newTenantConfiguration = new TenantConfiguration(moduleName, tenant.getId(), GSON.toJson(finalConfig));
        final Module module = new Module(moduleName);
        daoManager.getDaoTenantConfiguration().put(newTenantConfiguration);
        try (final SharedCache<byte[], byte[]> cache = configurationCacheManager.getTenantCache(module)) {
            cache.put(tv.v1x1.common.dto.core.Tenant.KEY_CODEC.encode(tenant.toCore(daoManager.getDaoTenant())), newTenantConfiguration.getJson().getBytes());
        } catch (final IOException e) {
            LOG.warn("Uncaught Exception:", e);
        }
        final tv.v1x1.common.dto.core.Tenant coreTenant = tenant.toCore(daoManager.getDaoTenant());
        messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(new TenantConfigChangeEvent(apiModule.toDto(), module, coreTenant));
        apiModule.handleConfigChangeEvent(coreTenant, module);
        return dataProvider.getConfigurationFromJson(dataProvider.sanitizeConfig(finalConfig, new JsonObject(), userConfigurationDefinition, ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)), true);
    }

    @Path("/{module}/{platform}/{channelgroup}")
    @GET
    public Configuration getConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName,
                                   @PathParam("platform") final String platformString, @PathParam("channelgroup") final String channelGroupId) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.read");
        final UserConfigurationDefinition userConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getUser(moduleName);
        if(userConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant dbTenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(dbTenant == null)
            throw new NotFoundException();
        final tv.v1x1.common.dto.core.Tenant tenant = dbTenant.toCore(daoManager.getDaoTenant());
        final Optional<tv.v1x1.common.dto.core.ChannelGroup> optionalChannelGroup = tenant.getChannelGroup(Platform.valueOf(platformString.toUpperCase()), channelGroupId);
        if(!optionalChannelGroup.isPresent())
            throw new NotFoundException();
        final tv.v1x1.common.dto.core.ChannelGroup channelGroup = optionalChannelGroup.get();
        return dataProvider.getChannelGroupConfiguration(userConfigurationDefinition, moduleName, tenant.getId().getValue(), channelGroup.getPlatform(), channelGroup.getId());
    }

    @Path("/{module}/{platform}/{channelgroup}")
    @PUT
    public Configuration putConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName,
                                   @PathParam("platform") final String platformString, @PathParam("channelgroup") final String channelGroupId,
                                   final Configuration configuration) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.write");
        final UserConfigurationDefinition userConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getUser(moduleName);
        if(userConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant dbTenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(dbTenant == null)
            throw new NotFoundException();
        final tv.v1x1.common.dto.core.Tenant tenant = dbTenant.toCore(daoManager.getDaoTenant());
        final Optional<tv.v1x1.common.dto.core.ChannelGroup> optionalChannelGroup = tenant.getChannelGroup(Platform.valueOf(platformString.toUpperCase()), channelGroupId);
        if(!optionalChannelGroup.isPresent())
            throw new NotFoundException();
        final tv.v1x1.common.dto.core.ChannelGroup channelGroup = optionalChannelGroup.get();
        ChannelGroupConfiguration channelGroupConfiguration = daoManager.getDaoChannelGroupConfiguration().get(new Module(moduleName), channelGroup);
        if(channelGroupConfiguration == null)
            channelGroupConfiguration = new ChannelGroupConfiguration(moduleName, channelGroup.getTenant().getId().getValue(), channelGroup.getPlatform(), channelGroup.getId(), false, "{}");
        final JsonElement configElement = JSON_PARSER.parse(channelGroupConfiguration.getJson());
        if(!configElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject config = configElement.getAsJsonObject();
        final JsonElement newConfigElement = JSON_PARSER.parse(configuration.getConfigJson());
        if(!newConfigElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject newConfig = newConfigElement.getAsJsonObject();
        final JsonObject finalConfig = dataProvider.sanitizeConfig(newConfig, config, userConfigurationDefinition, ImmutableSet.of(Permission.WRITE_ONLY, Permission.READ_WRITE));
        final ChannelGroupConfiguration newChannelGroupConfiguration = new ChannelGroupConfiguration(moduleName, channelGroup.getTenant().getId().getValue(), channelGroup.getPlatform(), channelGroup.getId(), configuration.isEnabled(), GSON.toJson(finalConfig));
        final Module module = new Module(moduleName);
        daoManager.getDaoChannelGroupConfiguration().put(channelGroupConfiguration);
        try (final SharedCache<byte[], byte[]> cache = configurationCacheManager.getChannelGroupCache(module)) {
            if(configuration.isEnabled())
                cache.put(tv.v1x1.common.dto.core.ChannelGroup.KEY_CODEC.encode(channelGroup), newChannelGroupConfiguration.getJson().getBytes());
            else
                cache.invalidate(tv.v1x1.common.dto.core.ChannelGroup.KEY_CODEC.encode(channelGroup));
        } catch (final IOException e) {
            LOG.warn("Uncaught Exception:", e);
        }
        messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(new ChannelGroupConfigChangeEvent(apiModule.toDto(), module, channelGroup));
        apiModule.handleConfigChangeEvent(channelGroup.getTenant(), module);
        return dataProvider.getConfigurationFromJson(dataProvider.sanitizeConfig(finalConfig, new JsonObject(), userConfigurationDefinition, ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)), newChannelGroupConfiguration.isEnabled());
    }

    @Path("/{module}/{platform}/{channelgroup}/{channel}")
    @GET
    public Configuration getConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName,
                                   @PathParam("platform") final String platformString, @PathParam("channelgroup") final String channelGroupId,
                                   @PathParam("channel") final String channelId) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.read");
        final UserConfigurationDefinition userConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getUser(moduleName);
        if(userConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant dbTenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(dbTenant == null)
            throw new NotFoundException();
        final tv.v1x1.common.dto.core.Tenant tenant = dbTenant.toCore(daoManager.getDaoTenant());
        final Optional<Channel> optionalChannel = tenant.getChannel(Platform.valueOf(platformString.toUpperCase()), channelGroupId, channelId);
        if(!optionalChannel.isPresent())
            throw new NotFoundException();
        final Channel channel = optionalChannel.get();
        return dataProvider.getChannelConfiguration(userConfigurationDefinition, moduleName, channel.getChannelGroup().getPlatform(), channel.getChannelGroup().getId(), channel.getId());
    }

    @Path("/{module}/{platform}/{channelgroup}/{channel}")
    @PUT
    public Configuration putConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName,
                                   @PathParam("platform") final String platformString, @PathParam("channelgroup") final String channelGroupId,
                                   @PathParam("channel") final String channelId, final Configuration configuration) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.write");
        final UserConfigurationDefinition userConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getUser(moduleName);
        if(userConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant dbTenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(dbTenant == null)
            throw new NotFoundException();
        final tv.v1x1.common.dto.core.Tenant tenant = dbTenant.toCore(daoManager.getDaoTenant());
        final Optional<Channel> optionalChannel = tenant.getChannel(Platform.valueOf(platformString.toUpperCase()), channelGroupId, channelId);
        if(!optionalChannel.isPresent())
            throw new NotFoundException();
        final Channel channel = optionalChannel.get();
        ChannelConfiguration channelConfiguration = daoManager.getDaoChannelConfiguration().get(new Module(moduleName), channel);
        if(channelConfiguration == null)
            channelConfiguration = new ChannelConfiguration(moduleName, channel.getChannelGroup().getPlatform(), channel.getChannelGroup().getId(), channel.getId(), false, "{}");
        final JsonElement configElement = JSON_PARSER.parse(channelConfiguration.getJson());
        if(!configElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject config = configElement.getAsJsonObject();
        final JsonElement newConfigElement = JSON_PARSER.parse(configuration.getConfigJson());
        if(!newConfigElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject newConfig = newConfigElement.getAsJsonObject();
        final JsonObject finalConfig = dataProvider.sanitizeConfig(newConfig, config, userConfigurationDefinition, ImmutableSet.of(Permission.WRITE_ONLY, Permission.READ_WRITE));
        final ChannelConfiguration newChannelConfiguration = new ChannelConfiguration(moduleName, channel.getChannelGroup().getPlatform(), channel.getChannelGroup().getId(), channel.getId(), configuration.isEnabled(), GSON.toJson(finalConfig));
        final Module module = new Module(moduleName);
        daoManager.getDaoChannelConfiguration().put(newChannelConfiguration);
        try (final SharedCache<byte[], byte[]> cache = configurationCacheManager.getChannelCache(module)) {
            if(configuration.isEnabled())
                cache.put(tv.v1x1.common.dto.core.Channel.KEY_CODEC.encode(channel), newChannelConfiguration.getJson().getBytes());
            else
                cache.invalidate(tv.v1x1.common.dto.core.Channel.KEY_CODEC.encode(channel));
        } catch (final IOException e) {
            LOG.warn("Uncaught Exception:", e);
        }
        messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(new ChannelConfigChangeEvent(apiModule.toDto(), module, channel));
        apiModule.handleConfigChangeEvent(channel.getChannelGroup().getTenant(), module);
        return dataProvider.getConfigurationFromJson(dataProvider.sanitizeConfig(finalConfig, new JsonObject(), userConfigurationDefinition, ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)), newChannelConfiguration.isEnabled());
    }

    @Path("/{module}/all")
    @GET
    public ConfigurationTree getAllConfig(@HeaderParam("Authorization") final String authorization,
                                          @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName) {
        final tv.v1x1.common.dto.core.Tenant tenant = getTenant(tenantId);
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization)
                .ensurePermission("api.tenants.config.read")
                .ensurePermission("api.tenants.read");
        return new ConfigurationTree(
                getConfig(authorization, tenantId, moduleName),
                new tv.v1x1.modules.core.api.api.rest.Tenant(tenant),
                tenant.getChannelGroups().stream().map(
                        channelGroup -> new ChannelGroupConfigurationTree(
                                getConfig(authorization, tenantId, moduleName, channelGroup.getPlatform().toString(), channelGroup.getId()),
                                new tv.v1x1.modules.core.api.api.rest.ChannelGroup(channelGroup),
                                channelGroup.getChannels().stream().map(
                                        channel -> new ChannelConfigurationTree(
                                                new tv.v1x1.modules.core.api.api.rest.Channel(channel),
                                                getConfig(authorization, tenantId, moduleName, channelGroup.getPlatform().toString(), channelGroup.getId(), channel.getId())
                                        )
                                ).collect(Collectors.toList())
                        )
                ).collect(Collectors.toList())
        );
    }

    private tv.v1x1.common.dto.core.Tenant getTenant(final String tenantId) {
        final Tenant dbTenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(dbTenant == null)
            throw new NotFoundException();
        return dbTenant.toCore(daoManager.getDaoTenant());
    }
}
