package tv.v1x1.modules.core.discord;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import org.redisson.api.RMapCache;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.messages.events.DiscordVoiceStateEvent;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.modules.ServiceModule;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.services.coordination.LoadBalancingDistributor;
import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.gateway.BotGateway;
import tv.v1x1.common.services.discord.dto.voice.VoiceState;
import tv.v1x1.common.services.queue.MessageQueue;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.common.util.ratelimiter.GlobalRateLimiter;
import tv.v1x1.common.util.ratelimiter.RateLimiter;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by cobi on 9/10/2017.
 */
public class DiscordModule extends ServiceModule<DiscordGlobalConfiguration, DiscordUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final UUID SCHEDULER_UPDATE_SHARDS = new UUID(java.util.UUID.nameUUIDFromBytes("Module|Discord|UpdateShards".getBytes()));
    private static final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());

    private final Map<String, DiscordBotShard> bots = new ConcurrentHashMap<>();
    private final LoadingCache<String, Tenant> tenantByGuildCache;
    private final LoadingCache<String, Tenant> tenantCache;
    private final LoadingCache<String, GlobalUser> globalUserCache;
    private final LoadingCache<PermissionCacheKey, List<Permission>> permissionCache;

    private ScheduledExecutorService scheduledExecutorService;
    private MessageQueue eventRouter;
    private LoadBalancingDistributor shardDistributor;
    private RateLimiter identifyShortLimiter;
    private RateLimiter identifyDailyLimiter;
    private DiscordApi discordApi;
    private RMapCache<byte[], byte[]> lastSeen;
    private RMapCache<byte[], byte[]> sessionIds;
    private RMapCache<byte[], byte[]> roles;
    private RMapCache<byte[], byte[]> voiceStates;

    private static class PermissionCacheKey {
        private final Tenant tenant;
        private final GlobalUser globalUser;
        private final String channelGroupId;
        private final Set<String> platformGroups;

        public PermissionCacheKey(final Tenant tenant, final GlobalUser globalUser, final String channelGroupId, final Set<String> platformGroups) {
            this.tenant = tenant;
            this.globalUser = globalUser;
            this.channelGroupId = channelGroupId;
            this.platformGroups = platformGroups;
        }

        public Tenant getTenant() {
            return tenant;
        }

        public GlobalUser getGlobalUser() {
            return globalUser;
        }

        public String getChannelGroupId() {
            return channelGroupId;
        }

        public Set<String> getPlatformGroups() {
            return platformGroups;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final PermissionCacheKey that = (PermissionCacheKey) o;

            if (tenant != null ? !tenant.equals(that.tenant) : that.tenant != null) return false;
            if (globalUser != null ? !globalUser.equals(that.globalUser) : that.globalUser != null) return false;
            if (channelGroupId != null ? !channelGroupId.equals(that.channelGroupId) : that.channelGroupId != null) return false;
            return platformGroups != null ? platformGroups.equals(that.platformGroups) : that.platformGroups == null;

        }

        @Override
        public int hashCode() {
            int result = tenant != null ? tenant.hashCode() : 0;
            result = 31 * result + (globalUser != null ? globalUser.hashCode() : 0);
            result = 31 * result + (channelGroupId != null ? channelGroupId.hashCode() : 0);
            result = 31 * result + (platformGroups != null ? platformGroups.hashCode() : 0);
            return result;
        }
    }


    public DiscordModule() {
        this.tenantByGuildCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Tenant>() {
                    @Override
                    public Tenant load(final String s) {
                        try {
                            LOG.debug("Loading tenant for guild {}", s);
                            return getDaoManager().getDaoTenant().getOrCreate(Platform.DISCORD, s, null).toCore(getDaoManager().getDaoTenant());
                        } catch(final Exception e) {
                            LOG.error("Exception getting tenant", e);
                            throw e;
                        }
                    }
                });
        this.tenantCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Tenant>() {
                    @Override
                    public Tenant load(final String s) {
                        try {
                            final String[] parts = s.split(":", 2);
                            if(parts.length != 2)
                                throw new IllegalArgumentException("Expected 2 parts but got " + parts.length);
                            LOG.debug("Loading tenant for {}", s);
                            return getDaoManager().getDaoTenant().getOrCreate(Platform.DISCORD, parts[0], parts[1], null).toCore(getDaoManager().getDaoTenant());
                        } catch(final Exception e) {
                            LOG.error("Exception getting tenant", e);
                            throw e;
                        }
                    }
                });
        this.globalUserCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, GlobalUser>() {
                    @Override
                    public GlobalUser load(final String s) {
                        try {
                            final String[] parts = s.split(":", 2);
                            if(parts.length != 2)
                                throw new IllegalArgumentException("Expected 2 parts but got " + parts.length);
                            LOG.debug("Loading global user for {}", s);
                            return getDaoManager().getDaoGlobalUser().getOrCreate(Platform.DISCORD, parts[0], parts[1]).toCore();
                        } catch(final Exception e) {
                            LOG.error("Exception getting global user", e);
                            throw e;
                        }
                    }
                });
        this.permissionCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<PermissionCacheKey, List<Permission>>() {
                    @Override
                    public List<Permission> load(final PermissionCacheKey permissionCacheKey) {
                        try {
                            LOG.debug("Loading tenant permissions for tenant={} globalUser={} platformGroups={}",
                                    permissionCacheKey.getTenant().getId(), permissionCacheKey.getGlobalUser().getId(),
                                    Joiner.on(", ").join(permissionCacheKey.getPlatformGroups()));
                            final Set<tv.v1x1.common.dto.db.Permission> permissions = getDaoManager().getDaoTenantGroup().getAllPermissions(
                                    permissionCacheKey.getTenant(), permissionCacheKey.getGlobalUser(), Platform.DISCORD,
                                    permissionCacheKey.getChannelGroupId(), permissionCacheKey.getPlatformGroups());
                            if(permissions == null)
                                return ImmutableList.of();
                            final List<Permission> permissionList = permissions.stream().map(tv.v1x1.common.dto.db.Permission::toCore).collect(Collectors.toList());
                            LOG.debug("Loaded tenant permissions for tenant={} globalUser={} platformGroups={}: {}",
                                    permissionCacheKey.getTenant().getId(), permissionCacheKey.getGlobalUser().getId(),
                                    Joiner.on(", ").join(permissionCacheKey.getPlatformGroups()),
                                    Joiner.on(", ").join(permissionList.stream().map(Permission::getNode).collect(Collectors.toList())));
                            return permissionList;
                        } catch(final Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
    }

    @Override
    public String getName() {
        return "discord";
    }

    @Override
    protected void initialize() {
        discordApi = getInjector().getInstance(DiscordApi.class);
        scheduledExecutorService = Executors.newScheduledThreadPool(100);
        identifyShortLimiter = new GlobalRateLimiter(getCuratorFramework(), scheduledExecutorService, "discord/identify/short", 1, 8);
        identifyDailyLimiter = new GlobalRateLimiter(getCuratorFramework(), scheduledExecutorService, "discord/identify/daily", 900, 86400);
        lastSeen = getRedisson().getMapCache("Modules|Core|Discord|lastSeen|v2", ByteArrayCodec.INSTANCE);
        sessionIds = getRedisson().getMapCache("Modules|Core|Discord|sessionIds|v2", ByteArrayCodec.INSTANCE);
        roles = getRedisson().getMapCache("Modules|Core|Discord|roles", ByteArrayCodec.INSTANCE);
        voiceStates = getRedisson().getMapCache("Modules|Core|Discord|voiceStates", ByteArrayCodec.INSTANCE);
        eventRouter = getMessageQueueManager().forName(getMainQueueForModule(new Module("event_router")));
        shardDistributor = getLoadBalancingDistributor("/v1x1/discord/shards", 3);
        shardDistributor.addListener(new LoadBalancingDistributor.Listener() {
            @Override
            public void notify(final UUID instanceId, final Map<String, Integer> entries) {
                try {
                    if (getInstanceId().equals(instanceId.getValue())) {
                        setShards(entries);
                    }
                } catch (final Exception e) {
                    LOG.error("Got exception", e);
                }
            }

            @Override
            public int compareTo(final LoadBalancingDistributor.Listener o) {
                return hashCode() - o.hashCode();
            }
        });
        try {
            shardDistributor.addInstance(new UUID(getInstanceId()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        getServiceClient(SchedulerServiceClient.class).schedule(ImmutableSet.of(0), ImmutableSet.of(-1), ImmutableSet.of(-1), ImmutableSet.of(-1), ImmutableSet.of(-1), SCHEDULER_UPDATE_SHARDS, new byte[] {});
        updateShards();
    }

    public static void main(final String[] args) {
        try {
            new DiscordModule().entryPoint(args);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processSchedulerNotifyEvent(final SchedulerNotifyEvent event) {
        if(!event.getModule().equals(toDto()))
            return;
        if(!event.getId().equals(SCHEDULER_UPDATE_SHARDS))
            return;
        updateShards();
    }

    private void updateShards() {
        scheduledExecutorService.submit(() -> {
            try {
                LOG.info("Updating shards.");
                final BotGateway botGateway = discordApi.getGateway().getGatewayBot();
                final Collection<String> newEntries = new ArrayList<>();
                for (int i = 0; i < botGateway.getShards(); i++)
                    newEntries.add(BaseEncoding.base64Url().encode(("" + botGateway.getShards() + ":" + i + ":" + botGateway.getUrl()).getBytes()));
                LOG.info("New Shards: {}", Joiner.on(", ").join(newEntries));
                final List<String> oldEntries = shardDistributor.listEntries();
                LOG.info("Old Shards: {}", Joiner.on(", ").join(oldEntries));
                for(final String entry : oldEntries)
                    if(!newEntries.contains(entry))
                        shardDistributor.removeEntry(entry);
                for(final String entry : newEntries)
                    if(!oldEntries.contains(entry))
                        shardDistributor.addEntry(entry);
                LOG.info("Updated Shards: {}", Joiner.on(", ").join(shardDistributor.listEntries()));
            } catch (final Exception e) {
                LOG.error("Got exception", e);
                throw new RuntimeException(e);
            }
        });
    }

    private void setShards(final Map<String, Integer> shards) {
        final Set<String> processedShards = shards.entrySet().stream()
                .map(entry -> entry.getValue().toString() + ":" + new String(BaseEncoding.base64Url().decode(entry.getKey())))
                .collect(Collectors.toSet());
        LOG.info("Shards: {}", Joiner.on(", ").join(processedShards));
        LOG.info("this.shards: {}", Joiner.on(", ").join(bots.keySet()));
        bots.keySet().stream().filter(shardInfo -> !processedShards.contains(shardInfo)).forEach(this::disconnectShard);
        processedShards.stream().filter(shardInfo -> !bots.keySet().contains(shardInfo)).forEach(this::connectShard);
    }

    private void connectShard(final String shardInfo) {
        LOG.info("Connecting to shard {}", shardInfo);
        final String[] shardParts = shardInfo.split(":", 4);
        if(shardParts.length != 4)
            throw new IllegalArgumentException("Bad shardInfo <" + shardInfo + "> expected 4 parts");
        final URI uri;
        try {
            uri = new URI(shardParts[3] + "/?v=6&encoding=json");
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
        final DiscordBotShard discordBotShard = new DiscordBotShard(this, shardInfo, Integer.parseInt(shardParts[2], 10), Integer.parseInt(shardParts[1], 10), uri);
        final DiscordBotShard oldDiscordBotShard = bots.put(shardInfo, discordBotShard);
        scheduledExecutorService.submit(discordBotShard);
        if (oldDiscordBotShard != null)
            oldDiscordBotShard.shutdown();
    }

    private void disconnectShard(final String shardInfo) {
        try {
            LOG.info("Disconnecting from shard {}", shardInfo);
            final DiscordBotShard oldDiscordBotShard = bots.remove(shardInfo);
            if (oldDiscordBotShard != null)
                oldDiscordBotShard.shutdown();
        } catch (final Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    void rateLimitIdentify(final Runnable runnable) {
        try {
            identifyDailyLimiter.submitAndWait(() -> {
                try {
                    identifyShortLimiter.submitAndWait(runnable);
                } catch (final InterruptedException e) {
                    LOG.error("Got interrupted: ", e);
                }
            });
        } catch (final InterruptedException e) {
            LOG.error("Got interrupted: ", e);
        }
    }

    void setSessionId(final String shardKey, final String sessionId) {
        sessionIds.fastPut(shardKey.getBytes(), sessionId.getBytes(), 7, TimeUnit.DAYS);
    }

    String getSessionId(final String shardKey) {
        final byte[] bytes = sessionIds.get(shardKey.getBytes());
        if(bytes == null)
            return null;
        return new String(bytes);
    }


    void setLastSeen(final String shardKey, final Long lastSeenSequence) {
        lastSeen.fastPut(shardKey.getBytes(), Longs.toByteArray(lastSeenSequence), 7, TimeUnit.DAYS);
    }

    Long getLastSeen(final String shardKey) {
        final byte[] bytes = lastSeen.get(shardKey.getBytes());
        if(bytes == null)
            return null;
        return Longs.fromByteArray(bytes);
    }

    void setRoles(final String guildId, final String userId, final List<String> roleList) {
        roles.fastPut(CompositeKey.makeKey(guildId, userId), CompositeKey.makeKey(roleList.toArray(new String[]{})));
    }

    List<String> getRoles(final String guildId, final String userId) {
        final byte[] roleData = roles.get(CompositeKey.makeKey(guildId, userId));
        if(roleData == null)
            return ImmutableList.of();
        return Arrays.stream(CompositeKey.getKeys(roleData))
                .map(String::new).collect(Collectors.toList());
    }

    void deleteRoles(final String guildId, final String userId) {
        roles.fastRemove(CompositeKey.makeKey(guildId, userId));
    }

    void sendEvent(final Event event) {
        eventRouter.add(event);
    }

    GlobalUser getGlobalUser(final String id, final String username) {
        try {
            return globalUserCache.get(id + ":" + username);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    Tenant getTenant(final String guildId, final String channelId) {
        try {
            return tenantCache.get(guildId + ":" + channelId);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    Tenant getTenant(final String guildId) {
        try {
            return tenantByGuildCache.get(guildId);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    List<Permission> getPermissions(final Tenant tenant, final GlobalUser globalUser, final String channelGroupId, final Set<String> badges) {
        try {
            return permissionCache.get(new PermissionCacheKey(tenant, globalUser, channelGroupId, badges));
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    void handleVoiceState(final VoiceState voiceState) {
        try {
            final byte[] oldVoiceStateBytes;
            final byte[] newVoiceStateBytes = MAPPER.writeValueAsBytes(voiceState);
            if(voiceState.getChannelId() != null)
                oldVoiceStateBytes = voiceStates.put(CompositeKey.makeKey(voiceState.getGuildId(), voiceState.getUserId()), newVoiceStateBytes, 60, TimeUnit.DAYS);
            else
                oldVoiceStateBytes = voiceStates.remove(CompositeKey.makeKey(voiceState.getGuildId(), voiceState.getUserId()));
            final VoiceState oldVoiceState = oldVoiceStateBytes == null ? null : MAPPER.readValue(oldVoiceStateBytes, VoiceState.class);
            LOG.info("oldVoiceState: {}", oldVoiceState);
            LOG.info("newVoiceState: {}", voiceState);
            if(voiceState.equals(oldVoiceState))
                return;
            sendEvent(new DiscordVoiceStateEvent(toDto(), oldVoiceState, voiceState));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
