package tv.v1x1.modules.core.api.resources.tenant;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.Permission;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.db.ChannelConfiguration;
import tv.v1x1.common.dto.db.ChannelConfigurationDefinition;
import tv.v1x1.common.dto.db.ConfigurationDefinition;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TenantConfiguration;
import tv.v1x1.common.dto.db.TenantConfigurationDefinition;
import tv.v1x1.common.dto.messages.events.ChannelConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.services.cache.SharedCache;
import tv.v1x1.common.services.persistence.ConfigurationCacheManager;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.queue.MessageQueueManager;
import tv.v1x1.modules.core.api.ApiModule;
import tv.v1x1.modules.core.api.api.Configuration;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.BadRequestException;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * Created by naomi on 10/24/2016.
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

    @Inject
    public ConfigResource(final ApiModule apiModule, final DAOManager daoManager, final Authorizer authorizer, final MessageQueueManager messageQueueManager, final ConfigurationCacheManager configurationCacheManager) {
        this.apiModule = apiModule;
        this.daoManager = daoManager;
        this.authorizer = authorizer;
        this.messageQueueManager = messageQueueManager;
        this.configurationCacheManager = configurationCacheManager;
    }

    @Path("/{module}")
    @GET
    public Configuration getConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.read");
        final TenantConfigurationDefinition tenantConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getTenant(moduleName);
        if(tenantConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant tenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(tenant == null)
            throw new NotFoundException();
        TenantConfiguration tenantConfiguration = daoManager.getDaoTenantConfiguration().get(new Module(moduleName), tenant.toCore());
        if(tenantConfiguration == null)
            tenantConfiguration = new TenantConfiguration(moduleName, tenant.getId(), "{}");
        final JsonElement configElement = JSON_PARSER.parse(tenantConfiguration.getJson());
        if(!configElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject config = configElement.getAsJsonObject();
        return getConfigurationFromJson(sanitizeConfig(config, new JsonObject(), tenantConfigurationDefinition, ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)));
    }

    @Path("/{module}")
    @PUT
    public Configuration putConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName,
                                   final Configuration configuration) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.write");
        final TenantConfigurationDefinition tenantConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getTenant(moduleName);
        if(tenantConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant tenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(tenant == null)
            throw new NotFoundException();
        TenantConfiguration tenantConfiguration = daoManager.getDaoTenantConfiguration().get(new Module(moduleName), tenant.toCore());
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
        final JsonObject finalConfig = sanitizeConfig(newConfig, config, tenantConfigurationDefinition, ImmutableSet.of(Permission.WRITE_ONLY, Permission.READ_WRITE));
        final TenantConfiguration newTenantConfiguration = new TenantConfiguration(moduleName, tenant.getId(), GSON.toJson(finalConfig));
        final Module module = new Module(moduleName);
        daoManager.getDaoTenantConfiguration().put(newTenantConfiguration);
        try (final SharedCache<byte[], byte[]> cache = configurationCacheManager.getTenantCache(module)) {
            cache.put(tv.v1x1.common.dto.core.Tenant.KEY_CODEC.encode(tenant.toCore()), newTenantConfiguration.getJson().getBytes());
        } catch (final IOException e) {
            LOG.warn("Uncaught Exception:", e);
        }
        messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(new TenantConfigChangeEvent(apiModule.toDto(), module, tenant.toCore()));
        return getConfigurationFromJson(sanitizeConfig(finalConfig, new JsonObject(), tenantConfigurationDefinition, ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)));
    }

    @Path("/{module}/{platform}/{channel}")
    @GET
    public Configuration getConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName,
                                   @PathParam("platform") final String platformString, @PathParam("channel") final String channelId) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.read");
        final ChannelConfigurationDefinition channelConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getChannel(moduleName);
        if(channelConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant tenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(tenant == null)
            throw new NotFoundException();
        final Optional<Tenant.Entry> tenantEntry = tenant.getEntries().stream()
                .filter(e -> e.getPlatform().equals(Platform.valueOf(platformString.toUpperCase())) && e.getChannelId().equals(channelId))
                .findFirst();
        if(!tenantEntry.isPresent())
            throw new NotFoundException();
        final Channel channel = tenantEntry.get().toCore(tenant.toCore());
        ChannelConfiguration channelConfiguration = daoManager.getDaoChannelConfiguration().get(new Module(moduleName), channel);
        if(channelConfiguration == null)
            channelConfiguration = new ChannelConfiguration(moduleName, tenant.getId(), channel.getPlatform(), channel.getId(), "{}");
        final JsonElement configElement = JSON_PARSER.parse(channelConfiguration.getJson());
        if(!configElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject config = configElement.getAsJsonObject();
        return getConfigurationFromJson(sanitizeConfig(config, new JsonObject(), channelConfigurationDefinition, ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)));
    }

    @Path("/{module}/{platform}/{channel}")
    @PUT
    public Configuration putConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("tenant") final String tenantId, @PathParam("module") final String moduleName,
                                   @PathParam("platform") final String platformString, @PathParam("channel") final String channelId,
                                   final Configuration configuration) {
        authorizer.tenantAuthorization(UUID.fromString(tenantId), authorization).ensurePermission("api.tenants.config.write");
        final ChannelConfigurationDefinition channelConfigurationDefinition = daoManager.getDaoConfigurationDefinition().getChannel(moduleName);
        if(channelConfigurationDefinition == null)
            throw new NotFoundException();
        final Tenant tenant = daoManager.getDaoTenant().getById(UUID.fromString(tenantId));
        if(tenant == null)
            throw new NotFoundException();
        final Optional<Tenant.Entry> tenantEntry = tenant.getEntries().stream()
                .filter(e -> e.getPlatform().equals(Platform.valueOf(platformString.toUpperCase())) && e.getChannelId().equals(channelId))
                .findFirst();
        if(!tenantEntry.isPresent())
            throw new NotFoundException();
        final Channel channel = tenantEntry.get().toCore(tenant.toCore());
        ChannelConfiguration channelConfiguration = daoManager.getDaoChannelConfiguration().get(new Module(moduleName), channel);
        if(channelConfiguration == null)
            channelConfiguration = new ChannelConfiguration(moduleName, tenant.getId(), channel.getPlatform(), channel.getId(), "{}");
        final JsonElement configElement = JSON_PARSER.parse(channelConfiguration.getJson());
        if(!configElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject config = configElement.getAsJsonObject();
        final JsonElement newConfigElement = JSON_PARSER.parse(configuration.getConfigJson());
        if(!newConfigElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject newConfig = newConfigElement.getAsJsonObject();
        final JsonObject finalConfig = sanitizeConfig(newConfig, config, channelConfigurationDefinition, ImmutableSet.of(Permission.WRITE_ONLY, Permission.READ_WRITE));
        final ChannelConfiguration newChannelConfiguration = new ChannelConfiguration(moduleName, tenant.getId(), channel.getPlatform(), channel.getId(), GSON.toJson(finalConfig));
        final Module module = new Module(moduleName);
        daoManager.getDaoChannelConfiguration().put(newChannelConfiguration);
        try (final SharedCache<byte[], byte[]> cache = configurationCacheManager.getChannelCache(module)) {
            cache.put(tv.v1x1.common.dto.core.Channel.KEY_CODEC.encode(channel), newChannelConfiguration.getJson().getBytes());
        } catch (final IOException e) {
            LOG.warn("Uncaught Exception:", e);
        }
        messageQueueManager.forName(tv.v1x1.common.modules.Module.getMainQueueForModule(module)).add(new ChannelConfigChangeEvent(apiModule.toDto(), module, channel));
        return getConfigurationFromJson(sanitizeConfig(finalConfig, new JsonObject(), channelConfigurationDefinition, ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)));
    }

    private Configuration getConfigurationFromJson(final JsonObject jsonObject) {
        return new Configuration(GSON.toJson(jsonObject));
    }

    private JsonObject sanitizeConfig(final JsonObject newConfig, final JsonObject oldConfig,
                                      final ConfigurationDefinition configurationDefinition,
                                      final Set<Permission> allowedPermissions) {
        if(!allowedPermissions.contains(configurationDefinition.getTenantPermission()))
            return oldConfig;
        return sanitizeConfig(newConfig, oldConfig, configurationDefinition.getFields(), configurationDefinition.getComplexFields(), allowedPermissions);
    }

    private JsonObject sanitizeConfig(final JsonObject newConfig, final JsonElement oldConfig,
                                      final List<ConfigurationDefinition.Field> fields,
                                      final Map<String, List<ConfigurationDefinition.Field>> complexFields,
                                      final Set<Permission> allowedPermissions) {
        return sanitizeConfig(newConfig, (oldConfig != null && oldConfig.isJsonObject()) ? oldConfig.getAsJsonObject() : null, fields, complexFields, allowedPermissions);
    }

    private JsonObject sanitizeConfig(final JsonObject newConfig, final JsonObject oldConfig,
                                      final List<ConfigurationDefinition.Field> fields,
                                      final Map<String, List<ConfigurationDefinition.Field>> complexFields,
                                      final Set<Permission> allowedPermissions) {
        final JsonObject ret = new JsonObject();
        if(oldConfig != null)
            for(final Map.Entry<String, JsonElement> elem : oldConfig.entrySet())
                ret.add(elem.getKey(), elem.getValue());
        for(final ConfigurationDefinition.Field field : fields)
            sanitizeField(newConfig, ret, field, complexFields, allowedPermissions);
        return ret;
    }

    private void sanitizeField(final JsonObject newConfig, final JsonObject ret,
                               final ConfigurationDefinition.Field field,
                               final Map<String, List<ConfigurationDefinition.Field>> complexFields,
                               final Set<Permission> allowedPermissions) {
        if(!newConfig.has(field.getJsonField()))
            return;
        if(!allowedPermissions.contains(field.getTenantPermission()))
            return;
        if(!validType(newConfig.get(field.getJsonField()), field.getConfigType()))
            throw new IllegalStateException("Invalid data found");
        if(field.getConfigType().isComplex()) {
            switch(field.getConfigType()) {
                case COMPLEX:
                    ret.add(field.getJsonField(), sanitizeConfig(newConfig,
                            ret.has(field.getJsonField()) ? ret.get(field.getJsonField()) : null,
                            complexFields.get(field.getComplexType()), complexFields, allowedPermissions));
                    return;
                case COMPLEX_LIST:
                    final JsonArray retAry = new JsonArray();
                    if(!newConfig.get(field.getJsonField()).isJsonArray())
                        throw new IllegalArgumentException("Expected Json Array for " + field.getJsonField());
                    for(final JsonElement elem : newConfig.get(field.getJsonField()).getAsJsonArray()) {
                        if(!elem.isJsonObject())
                            throw new IllegalArgumentException("Expected Json Object for element of " + field.getJsonField());
                        retAry.add(sanitizeConfig(elem.getAsJsonObject(), null,
                                complexFields.get(field.getComplexType()), complexFields, allowedPermissions));
                    }
                    ret.add(field.getJsonField(), retAry);
                    return;
                case COMPLEX_STRING_MAP:
                    final JsonObject retObj = new JsonObject();
                    if(!newConfig.get(field.getJsonField()).isJsonObject())
                        throw new IllegalArgumentException("Expected Json Object for " + field.getJsonField());
                    for(final Map.Entry<String, JsonElement> elem : newConfig.get(field.getJsonField()).getAsJsonObject().entrySet()) {
                        if(!elem.getValue().isJsonObject())
                            throw new IllegalArgumentException("Expected Json Object for element of " + field.getJsonField());
                        retObj.add(elem.getKey(), sanitizeConfig(elem.getValue().getAsJsonObject(), null,
                                complexFields.get(field.getComplexType()), complexFields, allowedPermissions));
                    }
                    ret.add(field.getJsonField(), retObj);
                    return;
                default:
                    throw new IllegalStateException("Unknown complex configType: " + field.getConfigType().name());
            }
        } else {
            ret.add(field.getJsonField(), newConfig.get(field.getJsonField()));
        }
    }

    private boolean validType(final JsonElement jsonElement, final ConfigType configType) {
        switch(configType) {
            case MASTER_ENABLE:
            case BOOLEAN:
                return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean();
            case STRING_LIST:
                return jsonElement.isJsonArray() && StreamSupport.stream(jsonElement.getAsJsonArray().spliterator(), false).allMatch(x -> validType(x, ConfigType.STRING));
            case COMPLEX_LIST:
                return jsonElement.isJsonArray();
            case STRING_MAP:
                return jsonElement.isJsonObject() && jsonElement.getAsJsonObject().entrySet().stream().allMatch(e -> validType(e.getValue(), ConfigType.STRING));
            case COMPLEX_STRING_MAP:
            case COMPLEX:
                return jsonElement.isJsonObject();
            case CREDENTIAL:
            case STRING:
            case BOT_NAME:
            case TWITCH_OAUTH:
                return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString();
            case INTEGER:
                return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber();
            default:
                throw new IllegalStateException("Unknown configType: " + configType.name());
        }
    }
}
