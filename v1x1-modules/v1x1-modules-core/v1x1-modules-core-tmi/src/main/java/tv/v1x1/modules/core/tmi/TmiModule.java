package tv.v1x1.modules.core.tmi;

import com.google.common.base.Joiner;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.*;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.db.*;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.modules.ServiceModule;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.services.coordination.LoadBalancingDistributor;
import tv.v1x1.common.services.queue.MessageQueue;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.dto.channels.Channel;
import tv.v1x1.common.util.network.IPProvider;
import tv.v1x1.common.util.ratelimiter.GlobalRateLimiter;
import tv.v1x1.common.util.ratelimiter.LocalRateLimiter;
import tv.v1x1.common.util.ratelimiter.RateLimiter;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiModule extends ServiceModule<TmiSettings, TmiGlobalConfiguration, TmiTenantConfiguration, TmiChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final UUID SCHEDULER_UPDATE_CHANNELS = new UUID(java.util.UUID.nameUUIDFromBytes("Module|TMI|UpdateChannels".getBytes()));

    private final LoadingCache<String, Tenant> tenantCache;
    private final LoadingCache<String, GlobalUser> globalUserCache;
    private final LoadingCache<PermissionCacheKey, List<Permission>> permissionCache;
    private LoadBalancingDistributor channelDistributor;
    private final Map<String, TmiBot> bots = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduledExecutorService;
    private RateLimiter joinLimiter;
    private MessageQueue eventRouter;
    private TwitchDisplayNameService twitchDisplayNameService;

    private static class PermissionCacheKey {
        private final Tenant tenant;
        private final GlobalUser globalUser;
        private final String channelId;
        private final Set<String> channelGroups;

        public PermissionCacheKey(final Tenant tenant, final GlobalUser globalUser, final String channelId, final Set<String> channelGroups) {
            this.tenant = tenant;
            this.globalUser = globalUser;
            this.channelId = channelId;
            this.channelGroups = channelGroups;
        }

        public Tenant getTenant() {
            return tenant;
        }

        public GlobalUser getGlobalUser() {
            return globalUser;
        }

        public String getChannelId() {
            return channelId;
        }

        public Set<String> getChannelGroups() {
            return channelGroups;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final PermissionCacheKey that = (PermissionCacheKey) o;

            if (tenant != null ? !tenant.equals(that.tenant) : that.tenant != null) return false;
            if (globalUser != null ? !globalUser.equals(that.globalUser) : that.globalUser != null) return false;
            if (channelId != null ? !channelId.equals(that.channelId) : that.channelId != null) return false;
            return channelGroups != null ? channelGroups.equals(that.channelGroups) : that.channelGroups == null;

        }

        @Override
        public int hashCode() {
            int result = tenant != null ? tenant.hashCode() : 0;
            result = 31 * result + (globalUser != null ? globalUser.hashCode() : 0);
            result = 31 * result + (channelId != null ? channelId.hashCode() : 0);
            result = 31 * result + (channelGroups != null ? channelGroups.hashCode() : 0);
            return result;
        }
    }

    public TmiModule() {
        this.tenantCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Tenant>() {
                    @Override
                    public Tenant load(final String s) throws Exception {
                        try {
                            LOG.debug("Loading tenant for {}", s);
                            return getDaoManager().getDaoTenant().getOrCreate(Platform.TWITCH, s, null).toCore();
                        } catch(final Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
        this.globalUserCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, GlobalUser>() {
                    @Override
                    public GlobalUser load(final String s) throws Exception {
                        try {
                            LOG.debug("Loading global user for {}", s);
                            return getDaoManager().getDaoGlobalUser().getOrCreate(Platform.TWITCH, s, null).toCore();
                        } catch(final Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
        this.permissionCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<PermissionCacheKey, List<Permission>>() {
                    @Override
                    public List<Permission> load(final PermissionCacheKey permissionCacheKey) throws Exception {
                        try {
                            LOG.debug("Loading tenant permissions for tenant={} globalUser={} channelGroups={}",
                                    permissionCacheKey.getTenant().getId(), permissionCacheKey.getGlobalUser().getId(),
                                    Joiner.on(", ").join(permissionCacheKey.getChannelGroups()));
                            final Set<tv.v1x1.common.dto.db.Permission> permissions = getDaoManager().getDaoTenantGroup().getAllPermissions(
                                    permissionCacheKey.getTenant(), permissionCacheKey.getGlobalUser(), Platform.TWITCH,
                                    permissionCacheKey.getChannelId(), permissionCacheKey.getChannelGroups());
                            if(permissions == null)
                                return ImmutableList.of();
                            final List<Permission> permissionList = permissions.stream().map(tv.v1x1.common.dto.db.Permission::toCore).collect(Collectors.toList());
                            LOG.debug("Loaded tenant permissions for tenant={} globalUser={} channelGroups={}: {}",
                                    permissionCacheKey.getTenant().getId(), permissionCacheKey.getGlobalUser().getId(),
                                    Joiner.on(", ").join(permissionCacheKey.getChannelGroups()),
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
    protected void initialize() {
        super.initialize();
        final String myIp;
        try {
            myIp = IPProvider.getMyIp();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        twitchDisplayNameService = getInjector().getInstance(TwitchDisplayNameService.class);
        scheduledExecutorService = Executors.newScheduledThreadPool(getSettings().getMaxConnections());
        joinLimiter = new GlobalRateLimiter(getCuratorFramework(), scheduledExecutorService, "tmi/" + myIp, 48, 15);
        eventRouter = getMessageQueueManager().forName(getMainQueueForModule(new Module("event_router")));
        channelDistributor = getLoadBalancingDistributor("/v1x1/tmi/channels", getGlobalConfiguration().getConnectionsPerChannel());
        channelDistributor.addListener(new LoadBalancingDistributor.Listener() {
            @Override
            public void notify(final UUID instanceId, final Set<String> entries) {
                if (getInstanceId().equals(instanceId.getValue())) {
                    try {
                        setChannels(entries);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public int compareTo(final LoadBalancingDistributor.Listener o) {
                return hashCode() - o.hashCode();
            }
        });
        try {
            channelDistributor.addInstance(new UUID(getInstanceId()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        getServiceClient(SchedulerServiceClient.class).schedule(ImmutableSet.of(-1), ImmutableSet.of(-1), ImmutableSet.of(-1), ImmutableSet.of(-1), ImmutableSet.of(-1), SCHEDULER_UPDATE_CHANNELS, new byte[] {});
    }

    @Override
    protected void shutdown() {
        try {
            channelDistributor.removeInstance(new UUID(getInstanceId()));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        for(final Map.Entry<String, TmiBot> entry : bots.entrySet())
            try {
                entry.getValue().shutdown();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        super.shutdown();
    }

    @Override
    protected void processSchedulerNotifyEvent(SchedulerNotifyEvent event) {
        if(!event.getModule().equals(toDto()))
            return;
        if(!event.getId().equals(SCHEDULER_UPDATE_CHANNELS))
            return;
        LOG.info("Beginning join check.");
        try {
            Set<String> entries = new HashSet<>(channelDistributor.listEntries());
            for(final JoinedTwitchChannel joinedTwitchChannel : getDaoManager().getDaoJoinedTwitchChannel().list()) {
                if(entries.remove(joinedTwitchChannel.getChannel()))
                    continue;
                try {
                    channelDistributor.addEntry(joinedTwitchChannel.getChannel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for(final String entry : entries) {
                try {
                    channelDistributor.removeEntry(entry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("End join check.");
    }

    @Override
    public String getName() {
        return "tmi";
    }

    GlobalUser getGlobalUser(final String id) {
        try {
            return globalUserCache.get(id);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    Tenant getTenant(final String id) {
        try {
            return tenantCache.get(id);
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    List<Permission> getPermissions(final Tenant tenant, final GlobalUser globalUser, final String channelId, final Set<String> badges) {
        try {
            return permissionCache.get(new PermissionCacheKey(tenant, globalUser, channelId, badges));
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void setChannels(final Collection<String> channelIds) throws IOException, InterruptedException {
        LOG.info("Channels: {}", Joiner.on(", ").join(channelIds));
        LOG.info("this.channels: {}", Joiner.on(", ").join(bots.keySet()));
        bots.keySet().stream().filter(channelId -> !channelIds.contains(channelId)).forEach(this::part);
        channelIds.stream().filter(channelId -> !bots.keySet().contains(channelId)).forEach(this::join);
    }

    private void join(final String channelId) {
        try {
            final Channel channel = twitchDisplayNameService.getChannelByChannelId(channelId);
            LOG.info("Joining {}", channel.getDisplayName());
            if (bots.containsKey(channelId))
                return;
            final Tenant tenant = getTenant(channelId);
            LOG.debug("Getting tenant configuration for {}", channel.getDisplayName());
            final TmiTenantConfiguration tenantConfiguration = getTenantConfiguration(tenant);
            final String oauthToken;
            String username = tenantConfiguration.getBotName();
            if (tenantConfiguration.isCustomBot()) {
                oauthToken = tenantConfiguration.getOauthToken();
            } else {
                if (username == null || !getGlobalConfiguration().getGlobalBots().containsKey(username))
                    username = getGlobalConfiguration().getDefaultUsername();
                oauthToken = getGlobalConfiguration().getGlobalBots().get(username);
            }
            LOG.debug("Connecting to {} with username={} password=<removed>", channel.getDisplayName(), username);
            final RateLimiter messageLimiter = new LocalRateLimiter(scheduledExecutorService, 18, 30);
            final TmiBot tmiBot = new TmiBot(username, oauthToken, eventRouter, toDto(), joinLimiter, messageLimiter, getDeduplicator(), this, channel, twitchDisplayNameService);
            scheduledExecutorService.submit(tmiBot);
            final TmiBot oldTmiBot = bots.put(channelId, tmiBot);
            try {
                if (oldTmiBot != null)
                    oldTmiBot.shutdown();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void part(final String channelId) {
        try {
            LOG.info("Leaving {}", channelId);
            final TmiBot oldTmiBot = bots.remove(channelId);
            try {
                if (oldTmiBot != null)
                    oldTmiBot.shutdown();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(final String[] args) {
        try {
            new TmiModule().entryPoint(args);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
