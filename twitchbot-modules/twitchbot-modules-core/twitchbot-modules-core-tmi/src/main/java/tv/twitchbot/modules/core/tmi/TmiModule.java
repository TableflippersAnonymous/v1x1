package tv.twitchbot.modules.core.tmi;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.db.TenantUserPermissions;
import tv.twitchbot.common.modules.ServiceModule;
import tv.twitchbot.common.services.coordination.LoadBalancingDistributor;
import tv.twitchbot.common.services.queue.MessageQueue;
import tv.twitchbot.common.util.network.IPProvider;
import tv.twitchbot.common.util.ratelimiter.GlobalRateLimiter;
import tv.twitchbot.common.util.ratelimiter.LocalRateLimiter;
import tv.twitchbot.common.util.ratelimiter.RateLimiter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiModule extends ServiceModule<TmiSettings, TmiGlobalConfiguration, TmiTenantConfiguration> {
    private final LoadingCache<String, Tenant> tenantCache;
    private final LoadingCache<String, GlobalUser> globalUserCache;
    private final LoadingCache<Pair<Tenant, GlobalUser>, List<Permission>> permissionCache;
    private final Set<String> channels = new ConcurrentSkipListSet<>();
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
                    public Tenant load(String s) throws Exception {
                        try {
                            System.out.println("Loading tenant for " + s);
                            return getDaoManager().getDaoTenant().getOrCreate(Platform.TWITCH, s, s).toCore();
                        } catch(Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
        this.globalUserCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, GlobalUser>() {
                    @Override
                    public GlobalUser load(String s) throws Exception {
                        try {
                            System.out.println("Loading global user for " + s);
                            return getDaoManager().getDaoGlobalUser().getOrCreate(Platform.TWITCH, s, s).toCore();
                        } catch(Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
        this.permissionCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<Pair<Tenant, GlobalUser>, List<Permission>>() {
                    @Override
                    public List<Permission> load(Pair<Tenant, GlobalUser> tenantGlobalUserPair) throws Exception {
                        try {
                            System.out.println("Loading tenant permissions for tenant=" + tenantGlobalUserPair.getFirst().getId() + " globalUser=" + tenantGlobalUserPair.getSecond().getId());
                            TenantUserPermissions permissions = getDaoManager().getDaoTenantUserPermissions().getByTenantAndUser(tenantGlobalUserPair.getFirst().getId().getValue(), tenantGlobalUserPair.getSecond().getId().getValue());
                            if(permissions == null)
                                return ImmutableList.of();
                            return permissions.getPermissions().stream().map(TenantUserPermissions.Permission::toCore).collect(Collectors.toList());
                        } catch(Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
    }

    @Override
    protected void initialize() {
        super.initialize();
        String myIp;
        try {
            myIp = IPProvider.getMyIp();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scheduledExecutorService = Executors.newScheduledThreadPool(getSettings().getMaxConnections());
        joinLimiter = new GlobalRateLimiter(getCuratorFramework(), scheduledExecutorService, "tmi/" + myIp, 48, 15);
        eventRouter = getMessageQueueManager().forName(getMainQueueForModule(new Module("event_router")));
        channelDistributor = getLoadBalancingDistributor("/channels", getGlobalConfiguration().getConnectionsPerChannel());
        channelDistributor.addListener(new LoadBalancingDistributor.Listener() {
            @Override
            public void notify(UUID instanceId, Set<String> entries) {
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
            public int compareTo(LoadBalancingDistributor.Listener o) {
                return hashCode() - o.hashCode();
            }
        });
        try {
            channelDistributor.addInstance(new UUID(getInstanceId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void shutdown() {
        try {
            channelDistributor.removeInstance(new UUID(getInstanceId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Map.Entry<String, TmiBot> entry : bots.entrySet())
            try {
                entry.getValue().shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.shutdown();
    }

    @Override
    public String getName() {
        return "tmi";
    }

    GlobalUser getGlobalUser(String id) {
        try {
            return globalUserCache.get(id);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    Tenant getTenant(String id) {
        try {
            return tenantCache.get(id);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    List<Permission> getPermissions(Tenant tenant, GlobalUser globalUser) {
        try {
            return permissionCache.get(new Pair<>(tenant, globalUser));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void setChannels(Set<String> channels) throws IOException, InterruptedException {
        this.channels.stream().filter(channel -> !channels.contains(channel)).forEach(channel -> part(channel));
        channels.stream().filter(channel -> !this.channels.contains(channel)).forEach(channel -> join(channel));
    }

    private void join(String channel) {
        if(bots.containsKey(channel))
            return;
        System.out.println("Joining " + channel);
        System.out.println("Getting tenant for " + channel);
        Tenant tenant = getTenant(channel);
        System.out.println("Getting tenant configuration for " + channel);
        TmiTenantConfiguration tenantConfiguration = getTenantConfiguration(tenant);
        String oauthToken;
        String username = tenantConfiguration.getBotName();
        if(tenantConfiguration.isCustomBot()) {
            oauthToken = tenantConfiguration.getOauthToken();
        } else {
            if(username == null || !getGlobalConfiguration().getGlobalBots().containsKey(username))
                username = getGlobalConfiguration().getDefaultUsername();
            oauthToken = getGlobalConfiguration().getGlobalBots().get(username);
        }
        System.out.println("channel: " + channel + " username: " + username + " oauth: " + oauthToken);
        RateLimiter messageLimiter = new LocalRateLimiter(scheduledExecutorService, 18, 30);
        System.out.println("Built messageLimiter for " + channel);
        TmiBot tmiBot = new TmiBot(username, oauthToken, eventRouter, toDto(), joinLimiter, messageLimiter, getDeduplicator(), this, channel);
        scheduledExecutorService.submit(tmiBot);
        TmiBot oldTmiBot = bots.put(channel, tmiBot);
        try {
            if(oldTmiBot != null)
                oldTmiBot.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void part(String channel) {
        if(!bots.containsKey(channel))
            return;
        TmiBot oldTmiBot = bots.remove(channel);
        try {
            if(oldTmiBot != null)
                oldTmiBot.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new TmiModule().entryPoint(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
