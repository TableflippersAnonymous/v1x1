package tv.twitchbot.modules.core.tmi;

import com.google.common.base.Joiner;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.db.TenantUserPermissions;
import tv.twitchbot.common.modules.ServiceModule;
import tv.twitchbot.common.services.coordination.LoadBalancingDistributor;
import tv.twitchbot.common.services.queue.MessageQueue;
import tv.twitchbot.common.util.data.Pair;
import tv.twitchbot.common.util.network.IPProvider;
import tv.twitchbot.common.util.ratelimiter.GlobalRateLimiter;
import tv.twitchbot.common.util.ratelimiter.LocalRateLimiter;
import tv.twitchbot.common.util.ratelimiter.RateLimiter;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/8/2016.
 */
public class TmiModule extends ServiceModule<TmiSettings, TmiGlobalConfiguration, TmiTenantConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LoadingCache<String, Tenant> tenantCache;
    private final LoadingCache<String, GlobalUser> globalUserCache;
    private final LoadingCache<Pair<Tenant, GlobalUser>, List<Permission>> permissionCache;
    private LoadBalancingDistributor channelDistributor;
    private final Map<String, TmiBot> bots = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduledExecutorService;
    private RateLimiter joinLimiter;
    private MessageQueue eventRouter;

    public TmiModule() {
        this.tenantCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Tenant>() {
                    @Override
                    public Tenant load(final String s) throws Exception {
                        try {
                            LOG.debug("Loading tenant for {}", s);
                            return getDaoManager().getDaoTenant().getOrCreate(Platform.TWITCH, s, s).toCore();
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
                            return getDaoManager().getDaoGlobalUser().getOrCreate(Platform.TWITCH, s, s).toCore();
                        } catch(final Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
        this.permissionCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<Pair<Tenant, GlobalUser>, List<Permission>>() {
                    @Override
                    public List<Permission> load(final Pair<Tenant, GlobalUser> tenantGlobalUserPair) throws Exception {
                        try {
                            LOG.debug("Loading tenant permissions for tenant={} globalUser={}", tenantGlobalUserPair.getFirst().getId(), tenantGlobalUserPair.getSecond().getId());
                            final TenantUserPermissions permissions = getDaoManager().getDaoTenantUserPermissions().getByTenantAndUser(tenantGlobalUserPair.getFirst().getId().getValue(), tenantGlobalUserPair.getSecond().getId().getValue());
                            if(permissions == null)
                                return ImmutableList.of();
                            return permissions.getPermissions().stream().map(TenantUserPermissions.Permission::toCore).collect(Collectors.toList());
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
        scheduledExecutorService = Executors.newScheduledThreadPool(getSettings().getMaxConnections());
        joinLimiter = new GlobalRateLimiter(getCuratorFramework(), scheduledExecutorService, "tmi/" + myIp, 48, 15);
        eventRouter = getMessageQueueManager().forName(getMainQueueForModule(new Module("event_router")));
        channelDistributor = getLoadBalancingDistributor("/twitchbot/tmi/channels", getGlobalConfiguration().getConnectionsPerChannel());
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

    List<Permission> getPermissions(final Tenant tenant, final GlobalUser globalUser) {
        try {
            return permissionCache.get(new Pair<>(tenant, globalUser));
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void setChannels(final Collection<String> channels) throws IOException, InterruptedException {
        LOG.info("Channels: {}", Joiner.on(", ").join(channels));
        LOG.info("this.channels: {}", Joiner.on(", ").join(bots.keySet()));
        bots.keySet().stream().filter(channel -> !channels.contains(channel)).forEach(this::part);
        channels.stream().filter(channel -> !bots.keySet().contains(channel)).forEach(this::join);
    }

    private void join(final String channel) {
        try {
            LOG.info("Joining {}", channel);
            if (bots.containsKey(channel))
                return;
            final Tenant tenant = getTenant(channel);
            LOG.debug("Getting tenant configuration for {}", channel);
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
            LOG.debug("Connecting to {} with username={} password={}", channel, username, oauthToken);
            final RateLimiter messageLimiter = new LocalRateLimiter(scheduledExecutorService, 18, 30);
            final TmiBot tmiBot = new TmiBot(username, oauthToken, eventRouter, toDto(), joinLimiter, messageLimiter, getDeduplicator(), this, channel);
            scheduledExecutorService.submit(tmiBot);
            final TmiBot oldTmiBot = bots.put(channel, tmiBot);
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

    private void part(final String channel) {
        try {
            LOG.info("Leaving {}", channel);
            final TmiBot oldTmiBot = bots.remove(channel);
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
