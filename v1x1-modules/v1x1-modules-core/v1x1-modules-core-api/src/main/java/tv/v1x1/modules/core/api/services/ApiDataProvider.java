package tv.v1x1.modules.core.api.services;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.db.ChannelConfiguration;
import tv.v1x1.common.dto.db.ChannelGroupConfiguration;
import tv.v1x1.common.dto.db.ChannelGroupPlatformMapping;
import tv.v1x1.common.dto.db.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.db.JoinedTwitchChannel;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TenantConfiguration;
import tv.v1x1.common.dto.db.UserConfigurationDefinition;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Permission;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.ApiModule;
import tv.v1x1.modules.core.api.api.rest.ChannelGroupPlatformGroup;
import tv.v1x1.modules.core.api.api.rest.Configuration;
import tv.v1x1.modules.core.api.api.rest.ConfigurationDefinition;
import tv.v1x1.modules.core.api.api.rest.ConfigurationDefinitionSet;
import tv.v1x1.modules.core.api.api.rest.GlobalUser;
import tv.v1x1.modules.core.api.api.rest.Module;
import tv.v1x1.modules.core.api.api.rest.SyncChannel;
import tv.v1x1.modules.core.api.api.rest.SyncChannelGroup;
import tv.v1x1.modules.core.api.api.rest.SyncTenant;
import tv.v1x1.modules.core.api.api.rest.TenantGroup;
import tv.v1x1.modules.core.api.api.rest.WebConfig;
import tv.v1x1.modules.core.api.auth.AuthorizationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ApiDataProvider {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private static final JsonParser JSON_PARSER = new JsonParser();

    private final DAOManager daoManager;
    private final ApiModule module;

    public ApiDataProvider(final DAOManager daoManager, final ApiModule module) {
        this.daoManager = daoManager;
        this.module = module;
    }

    public WebConfig getConfiguration() {
        /* Eventually this should come from the datastore or config.  For now, this allows us to move the basics
         * out of the client.
         */
        return new WebConfig(
                ImmutableMap.of(
                        Platform.TWITCH, new String(module.requireCredential("Common|Twitch|ClientId")),
                        Platform.DISCORD, new String(module.requireCredential("Common|Discord|ClientId"))
                ),
                ImmutableMap.of(
                        Platform.TWITCH, new String(module.requireCredential("Common|Twitch|RedirectUri")),
                        Platform.DISCORD, new String(module.requireCredential("Common|Discord|RedirectUri"))
                ),
                "/api/v1",
                "wss://v1x1.tv",
                ImmutableMap.of(
                        "TMI", getOauthUrl("https://api.twitch.tv/kraken/oauth2/authorize",
                                module.requireCredential("Common|TMI|ClientId"),
                                module.requireCredential("Common|TMI|RedirectUri"),
                                ImmutableList.of("user_read", "channel:moderate", "chat:edit", "chat:read",
                                        "whispers:read", "whispers:edit")),
                        "SPOTIFY", getOauthUrl("https://accounts.spotify.com/authorize",
                                module.requireCredential("Common|Spotify|ClientId"),
                                module.requireCredential("Common|Spotify|RedirectUri"),
                                ImmutableList.of("user-read-recently-played", "user-top-read",
                                        "user-modify-playback-state", "user-read-playback-state",
                                        "user-read-currently-playing", "playlist-read-private",
                                        "playlist-read-collaborative"))
                )
        );
    }

    private String getOauthUrl(final String authorizeUrl, final byte[] clientId, final byte[] redirectUri,
                               final List<String> scopes) {
        return getOauthUrl(authorizeUrl, new String(clientId), new String(redirectUri), scopes);
    }

    private String getOauthUrl(final String authorizeUrl, final String clientId, final String redirectUri,
                               final List<String> scopes) {
        return authorizeUrl
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=" + Joiner.on("+").join(scopes)
                + "&force_verify=true"
                + "&state=";
    }

    public Map<String, Module> getModules() {
        final Map<String, ConfigurationDefinition> user = getUserConfigDefinitions().stream()
                .collect(Collectors.toMap(ConfigurationDefinition::getName, configurationDefinition -> configurationDefinition));
        final Map<String, ConfigurationDefinition> global = getGlobalConfigDefinitions().stream()
                .collect(Collectors.toMap(ConfigurationDefinition::getName, configurationDefinition -> configurationDefinition));
        return user.entrySet().stream()
                .map(entry -> new Module(entry.getKey(), new ConfigurationDefinitionSet(global.get(entry.getKey()), entry.getValue())))
                .collect(Collectors.toMap(Module::getName, module -> module));
    }

    public SyncTenant getSyncTenant(final AuthorizationContext authorizationContext, final UUID tenantId, final Set<String> modules) {
        final Tenant dbTenant = daoManager.getDaoTenant().getById(tenantId);
        if(dbTenant == null)
            return null;
        final tv.v1x1.common.dto.core.Tenant coreTenant = dbTenant.toCore(daoManager.getDaoTenant());
        final Map<String, tv.v1x1.common.dto.db.ConfigurationDefinition> userConfigurationDefinitions =
                StreamSupport.stream(daoManager.getDaoConfigurationDefinition().getAllUser().spliterator(), false)
                    .collect(Collectors.toMap(tv.v1x1.common.dto.db.ConfigurationDefinition::getName, configurationDefinition -> configurationDefinition));
        final Map<String, SyncChannelGroup> channelGroups = coreTenant.getChannelGroups().parallelStream()
                .map(channelGroup -> getSyncChannelGroup(authorizationContext, channelGroup, modules, userConfigurationDefinitions))
                .collect(Collectors.toMap(syncChannelGroup -> syncChannelGroup.getPlatform() + ":" + syncChannelGroup.getId(), syncChannelGroup -> syncChannelGroup));
        final Map<String, Configuration> moduleConfiguration = authorizationContext.hasPermission("api.tenants.config.read") ? modules.parallelStream()
                .collect(Collectors.toMap(moduleName -> moduleName, moduleName -> getTenantConfiguration(userConfigurationDefinitions.get(moduleName), moduleName, tenantId)))
                : ImmutableMap.of();
        final Map<String, TenantGroup> groups = authorizationContext.hasPermission("api.permissions.read") ? StreamSupport.stream(
                daoManager.getDaoTenantGroup().getAllGroupsByTenant(coreTenant).spliterator(), false)
                .map(tenantGroup -> new TenantGroup(tenantGroup.getTenantId().toString(), tenantGroup.getGroupId().toString(),
                        tenantGroup.getName(), tenantGroup.getPermissions().stream().map(tv.v1x1.common.dto.db.Permission::getNode).collect(Collectors.toList()),
                        StreamSupport.stream(daoManager.getDaoTenantGroup().getUsersByGroup(tenantGroup).spliterator(), true)
                            .map(this::getGlobalUser).collect(Collectors.toList())))
                .collect(Collectors.toMap(TenantGroup::getGroupId, tenantGroup -> tenantGroup)) : ImmutableMap.of();
        return new SyncTenant(
                coreTenant.getId().toString(),
                coreTenant.getDisplayName(),
                channelGroups,
                moduleConfiguration,
                groups
        );
    }

    private GlobalUser getGlobalUser(final UUID uuid) {
        final tv.v1x1.common.dto.core.GlobalUser globalUser = daoManager.getDaoGlobalUser().getById(uuid).toCore();
        return new GlobalUser(globalUser);
    }

    public Configuration getTenantConfiguration(final tv.v1x1.common.dto.db.ConfigurationDefinition userConfigurationDefinition,
                                                final String moduleName, final UUID tenantId) {
        TenantConfiguration tenantConfiguration = daoManager.getDaoTenantConfiguration().get(moduleName, tenantId);
        if(tenantConfiguration == null)
            tenantConfiguration = new TenantConfiguration(moduleName, tenantId, "{}");
        final JsonElement configElement = JSON_PARSER.parse(tenantConfiguration.getJson());
        if(!configElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject config = configElement.getAsJsonObject();
        return getConfigurationFromJson(sanitizeConfig(config, new JsonObject(), userConfigurationDefinition,
                ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)), true);
    }

    private SyncChannelGroup getSyncChannelGroup(final AuthorizationContext authorizationContext,
                                                 final ChannelGroup channelGroup, final Set<String> modules,
                                                 final Map<String, tv.v1x1.common.dto.db.ConfigurationDefinition> userConfigurationDefinitions) {
        final Map<String, SyncChannel> channels = channelGroup.getChannels().stream()
                .map(channel -> getSyncChannel(authorizationContext, channel, modules, userConfigurationDefinitions))
                .collect(Collectors.toMap(SyncChannel::getId, syncChannel -> syncChannel));
        final Map<String, Configuration> moduleConfiguration = authorizationContext.hasPermission("api.tenants.config.read") ? modules.parallelStream()
                .collect(Collectors.toMap(moduleName -> moduleName,
                        moduleName -> getChannelGroupConfiguration(userConfigurationDefinitions.get(moduleName),
                                moduleName, channelGroup.getTenant().getId().getValue(), channelGroup.getPlatform(),
                                channelGroup.getId())))
                : ImmutableMap.of();
        final Map<String, String> groupMappings = authorizationContext.hasPermission("api.permissions.read") ?
                StreamSupport.stream(daoManager.getDaoTenantGroup().getChannelGroupPlatformMappings(channelGroup.getPlatform(), channelGroup.getId()).spliterator(), false)
                    .collect(Collectors.toMap(ChannelGroupPlatformMapping::getPlatformGroup, channelGroupPlatformMapping -> channelGroupPlatformMapping.getGroupId().toString()))
                : ImmutableMap.of();
        final List<ChannelGroupPlatformGroup> platformGroups = StreamSupport.stream(daoManager.getDaoChannelGroupPlatformGroups().getAll(channelGroup.getPlatform(), channelGroup.getId()).spliterator(), false)
                .map(channelGroupPlatformGroup -> new ChannelGroupPlatformGroup(channelGroupPlatformGroup.getName(), channelGroupPlatformGroup.getDisplayName()))
                .collect(Collectors.toList());
        final boolean joined = getJoined(channelGroup);
        return new SyncChannelGroup(
                channelGroup.getTenant().getId().toString(),
                channelGroup.getPlatform().name(),
                channelGroup.getId(),
                channelGroup.getDisplayName(),
                channels,
                moduleConfiguration,
                groupMappings,
                platformGroups,
                joined
        );
    }

    private boolean getJoined(final ChannelGroup channelGroup) {
        switch(channelGroup.getPlatform()) {
            case TWITCH:
                final JoinedTwitchChannel joinedTwitchChannel = daoManager.getDaoJoinedTwitchChannel().get(channelGroup.getId());
                return joinedTwitchChannel != null;
            case DISCORD:
                return true;
            default:
                return false;
        }
    }

    private SyncChannel getSyncChannel(final AuthorizationContext authorizationContext,
                                       final Channel channel, final Set<String> modules,
                                       final Map<String, tv.v1x1.common.dto.db.ConfigurationDefinition> userConfigurationDefinitions) {
        final Map<String, Configuration> moduleConfiguration = authorizationContext.hasPermission("api.tenants.config.read") ? modules.parallelStream()
                .collect(Collectors.toMap(moduleName -> moduleName,
                        moduleName -> getChannelConfiguration(userConfigurationDefinitions.get(moduleName),
                                moduleName, channel.getChannelGroup().getPlatform(), channel.getChannelGroup().getId(), channel.getId())))
                : ImmutableMap.of();
        return new SyncChannel(
                channel.getChannelGroup().getPlatform().name() + ":" + channel.getChannelGroup().getId(),
                channel.getId(),
                channel.getDisplayName(),
                moduleConfiguration
        );
    }

    public List<ConfigurationDefinition> getUserConfigDefinitions() {
        return StreamSupport.stream(daoManager.getDaoConfigurationDefinition().getAllUser().spliterator(), false)
                .map(UserConfigurationDefinition::toCore)
                .map(ConfigurationDefinition::fromCore)
                .collect(Collectors.toList());
    }

    public List<ConfigurationDefinition> getGlobalConfigDefinitions() {
        return StreamSupport.stream(daoManager.getDaoConfigurationDefinition().getAllGlobal().spliterator(), false)
                .map(GlobalConfigurationDefinition::toCore)
                .map(ConfigurationDefinition::fromCore)
                .collect(Collectors.toList());
    }


    public Configuration getConfigurationFromJson(final JsonObject jsonObject, final boolean enabled) {
        return new Configuration(GSON.toJson(jsonObject), enabled);
    }

    public JsonObject sanitizeConfig(final JsonObject newConfig, final JsonObject oldConfig,
                                     final tv.v1x1.common.dto.db.ConfigurationDefinition configurationDefinition,
                                     final Set<Permission> allowedPermissions) {
        if(!allowedPermissions.contains(configurationDefinition.getTenantPermission()))
            return oldConfig;
        return sanitizeConfig(newConfig, oldConfig, configurationDefinition.getFields(), configurationDefinition.getComplexFields(), allowedPermissions);
    }

    private JsonObject sanitizeConfig(final JsonObject newConfig, final JsonElement oldConfig,
                                      final List<tv.v1x1.common.dto.db.ConfigurationDefinition.Field> fields,
                                      final Map<String, List<tv.v1x1.common.dto.db.ConfigurationDefinition.Field>> complexFields,
                                      final Set<Permission> allowedPermissions) {
        return sanitizeConfig(newConfig, (oldConfig != null && oldConfig.isJsonObject()) ? oldConfig.getAsJsonObject() : null, fields, complexFields, allowedPermissions);
    }

    private JsonObject sanitizeConfig(final JsonObject newConfig, final JsonObject oldConfig,
                                      final List<tv.v1x1.common.dto.db.ConfigurationDefinition.Field> fields,
                                      final Map<String, List<tv.v1x1.common.dto.db.ConfigurationDefinition.Field>> complexFields,
                                      final Set<Permission> allowedPermissions) {
        final JsonObject ret = new JsonObject();
        if(oldConfig != null)
            for(final Map.Entry<String, JsonElement> elem : oldConfig.entrySet())
                ret.add(elem.getKey(), elem.getValue());
        for(final tv.v1x1.common.dto.db.ConfigurationDefinition.Field field : fields)
            sanitizeField(newConfig, ret, field, complexFields, allowedPermissions);
        return ret;
    }

    private void sanitizeField(final JsonObject newConfig, final JsonObject ret,
                               final tv.v1x1.common.dto.db.ConfigurationDefinition.Field field,
                               final Map<String, List<tv.v1x1.common.dto.db.ConfigurationDefinition.Field>> complexFields,
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
            case CHANNEL:
                return jsonElement.isJsonObject();
            case CREDENTIAL:
            case STRING:
            case FILE:
            case BOT_NAME:
            case OAUTH:
                return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString();
            case INTEGER:
                return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber();
            case PERMISSION:
                return jsonElement.isJsonNull() || jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("node");
            default:
                throw new IllegalStateException("Unknown configType: " + configType.name());
        }
    }

    public Configuration getChannelGroupConfiguration(final tv.v1x1.common.dto.db.ConfigurationDefinition userConfigurationDefinition,
                                                      final String moduleName, final UUID tenantId,
                                                      final Platform platform, final String id) {
        ChannelGroupConfiguration channelGroupConfiguration = daoManager.getDaoChannelGroupConfiguration().get(moduleName, tenantId, platform, id);
        if(channelGroupConfiguration == null)
            channelGroupConfiguration = new ChannelGroupConfiguration(moduleName, tenantId, platform, id, false, "{}");
        final JsonElement configElement = JSON_PARSER.parse(channelGroupConfiguration.getJson());
        if(!configElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject config = configElement.getAsJsonObject();
        return getConfigurationFromJson(sanitizeConfig(config, new JsonObject(), userConfigurationDefinition,
                ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)), channelGroupConfiguration.enabled());
    }

    public Configuration getChannelConfiguration(final tv.v1x1.common.dto.db.ConfigurationDefinition userConfigurationDefinition,
                                                 final String moduleName, final Platform platform,
                                                 final String channelGroupId, final String channelId) {
        ChannelConfiguration channelConfiguration = daoManager.getDaoChannelConfiguration().get(moduleName, platform,
                channelGroupId, channelId);
        if(channelConfiguration == null)
            channelConfiguration = new ChannelConfiguration(moduleName, platform, channelGroupId, channelId, false, "{}");
        final JsonElement configElement = JSON_PARSER.parse(channelConfiguration.getJson());
        if(!configElement.isJsonObject())
            throw new IllegalStateException("Expected config to be Json Object");
        final JsonObject config = configElement.getAsJsonObject();
        return getConfigurationFromJson(sanitizeConfig(config, new JsonObject(), userConfigurationDefinition, ImmutableSet.of(Permission.READ_ONLY, Permission.READ_WRITE)), channelConfiguration.enabled());
    }
}
