package tv.v1x1.modules.core.tmi;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.db.JoinedTwitchChannel;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.irc.MessageTaggedIrcStanza;
import tv.v1x1.common.dto.messages.events.ChannelConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.ChannelGroupConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.modules.ServiceModule;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.services.coordination.LoadBalancingDistributor;
import tv.v1x1.common.services.queue.MessageQueue;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.dto.auth.RootResponse;
import tv.v1x1.common.services.twitch.dto.channels.Channel;
import tv.v1x1.common.services.twitch.dto.users.ChatUser;
import tv.v1x1.common.util.ratelimiter.GlobalRateLimiter;
import tv.v1x1.common.util.ratelimiter.RateLimiter;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiModule extends ServiceModule<TmiGlobalConfiguration, TmiUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final UUID SCHEDULER_UPDATE_CHANNELS = new UUID(java.util.UUID.nameUUIDFromBytes("Module|TMI|UpdateChannels".getBytes()));
    private static final int RECONNECT_DELAY_MS = 1000;
    private static final int RECONNECT_SPLAY_MS = 1000;
    private static final Random RANDOM = new Random();

    private final LoadingCache<String, Tenant> tenantCache;
    private final LoadingCache<UserCacheKey, GlobalUser> globalUserCache;
    private final LoadingCache<PermissionCacheKey, List<Permission>> permissionCache;
    private LoadBalancingDistributor channelDistributor;
    private final Map<String, TmiBot> bots = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduledExecutorService;
    private RateLimiter joinLimiter;
    private MessageQueue eventRouter;
    private TwitchDisplayNameService twitchDisplayNameService;
    private EventLoopGroup eventLoopGroup;

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

            if (!Objects.equal(tenant, that.tenant)) return false;
            if (!Objects.equal(globalUser, that.globalUser)) return false;
            if (!Objects.equal(channelId, that.channelId)) return false;
            return Objects.equal(channelGroups, that.channelGroups);

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

    private static class UserCacheKey {
        private final String id;
        private final String displayName;

        public UserCacheKey(final String id, final String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final UserCacheKey that = (UserCacheKey) o;
            return Objects.equal(id, that.id) &&
                    Objects.equal(displayName, that.displayName);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id, displayName);
        }
    }

    public TmiModule() {
        this.tenantCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Tenant>() {
                    @Override
                    public Tenant load(final String s) {
                        try {
                            LOG.debug("Loading tenant for {}", s);
                            return getDaoManager().getDaoTenant().getOrCreate(Platform.TWITCH, s, s + ":main", null).toCore(getDaoManager().getDaoTenant());
                        } catch(final Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                });
        this.globalUserCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<UserCacheKey, GlobalUser>() {
                    @Override
                    public GlobalUser load(final UserCacheKey key) {
                        try {
                            LOG.debug("Loading global user for {}/{}", key.id, key.displayName);
                            return getDaoManager().getDaoGlobalUser().getOrCreate(Platform.TWITCH, key.id, key.displayName).toCore();
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
                    public List<Permission> load(final PermissionCacheKey permissionCacheKey) {
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
        eventLoopGroup = new NioEventLoopGroup();
        twitchDisplayNameService = getInjector().getInstance(TwitchDisplayNameService.class);
        scheduledExecutorService = Executors.newScheduledThreadPool(3);
        joinLimiter = new GlobalRateLimiter(getCuratorFramework(), scheduledExecutorService, "tmi-join", 48, 15);
        eventRouter = getMessageQueueManager().forName(getMainQueueForModule(new Module("event_router")));
        channelDistributor = getLoadBalancingDistributor("/v1x1/tmi/channels", getGlobalConfiguration().getConnectionsPerChannel());
        channelDistributor.addListener(new LoadBalancingDistributor.Listener() {
            @Override
            public void notify(final UUID instanceId, final Map<String, Integer> entries) {
                if (getInstanceId().equals(instanceId.getValue())) {
                    setChannels(entries.keySet());
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
            part(entry.getKey());
        eventLoopGroup.shutdownGracefully();
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

    GlobalUser getGlobalUser(final String id, final String displayName) {
        try {
            return globalUserCache.get(new UserCacheKey(id, displayName));
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

    ChannelGroup getChannelGroup(final String id) {
        return getTenant(id).getChannelGroup(Platform.TWITCH, id).get();
    }

    List<Permission> getPermissions(final Tenant tenant, final GlobalUser globalUser, final String channelId, final Set<String> badges) {
        try {
            return permissionCache.get(new PermissionCacheKey(tenant, globalUser, channelId, badges));
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void setChannels(final Collection<String> channelIds) {
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
            LOG.debug("Setting ChannelGroupPlatformGroups for {}", channel.getDisplayName());
            getDaoManager().getDaoChannelGroupPlatformGroups().putOnly(Platform.TWITCH, channelId, ImmutableMap.<String, String>builder()
                    .put("_DEFAULT_", "Everyone")
                    .put(MessageTaggedIrcStanza.Badge.ADMIN.name(), "Twitch Admin")
                    .put(MessageTaggedIrcStanza.Badge.BITS.name(), "Bits")
                    .put(MessageTaggedIrcStanza.Badge.BITS_CHARITY.name(), "Bits Charity")
                    .put(MessageTaggedIrcStanza.Badge.BITS_LEADER.name(), "Bits Leader")
                    .put(MessageTaggedIrcStanza.Badge.BROADCASTER.name(), "Broadcaster")
                    .put(MessageTaggedIrcStanza.Badge.GLOBAL_MOD.name(), "Global Mod")
                    .put(MessageTaggedIrcStanza.Badge.MODERATOR.name(), "Channel Moderator")
                    .put(MessageTaggedIrcStanza.Badge.PREMIUM.name(), "Twitch Prime")
                    .put(MessageTaggedIrcStanza.Badge.STAFF.name(), "Twitch Staff")
                    .put(MessageTaggedIrcStanza.Badge.SUBSCRIBER.name(), "Subscriber")
                    .put(MessageTaggedIrcStanza.Badge.SUB_GIFTER.name(), "Sub Gifter")
                    .put(MessageTaggedIrcStanza.Badge.TURBO.name(), "Twitch Turbo")
                    .put(MessageTaggedIrcStanza.Badge.TWITCHCON2017.name(), "TwitchCon17 Attendee")
                    .put(MessageTaggedIrcStanza.Badge.TWITCHCON2018.name(), "TwitchCon18 Attendee")
                    .put(MessageTaggedIrcStanza.Badge.PARTNER.name(), "Verified")
                    .put(MessageTaggedIrcStanza.Badge.VIP.name(), "VIP")
                    .put(MessageTaggedIrcStanza.Badge.CLIP_CHAMP.name(), "Clip Champ")
                    .put(MessageTaggedIrcStanza.Badge.OVERWATCH_LEAGUE_INSIDER_1.name(), "Overwatch League Insider")
                    .build()
            );
            LOG.debug("Getting tenant configuration for {}/{}", channel.getDisplayName(), tenant);
            final TmiUserConfiguration tenantConfiguration = getConfiguration(tenant.getChannel(Platform.TWITCH, channelId, channelId + ":main").get());
            final String oauthToken;
            if (tenantConfiguration.isCustomBot()) {
                oauthToken = tenantConfiguration.getOauthToken().replace("oauth:", "");
            } else {
                String username = tenantConfiguration.getBotName();
                if (username == null || !getGlobalConfiguration().getGlobalBots().containsKey(username))
                    username = getGlobalConfiguration().getDefaultUsername();
                oauthToken = getGlobalConfiguration().getGlobalBots().get(username).replace("oauth:", "");
            }
            final RootResponse rootResponse = getTwitchApi().withToken(oauthToken).getRoot().get();
            final String userId = rootResponse.getToken().getUserId();
            final ChatUser chatUser = getTwitchApi().getUsers().getChatUser(userId);
            LOG.debug("Connecting to {} with username={} password=<removed>", channel.getDisplayName(), chatUser.getLogin());
            final RateLimiter messageLimiter = new GlobalRateLimiter(getCuratorFramework(), scheduledExecutorService, "tmi-chat/" + userId, chatUser.getChatRateLimit(), 30);
            final TmiBot tmiBot = new TmiBot(chatUser.getLogin(), oauthToken, eventRouter, toDto(), joinLimiter,
                    messageLimiter, getDeduplicator(), this, channel, twitchDisplayNameService, eventLoopGroup);
            tmiBot.connect();
            final TmiBot oldTmiBot = bots.put(channelId, tmiBot);
            if (oldTmiBot != null)
                oldTmiBot.shutdown();
        } catch (final Exception e) {
            LOG.warn("Exception", e);
            throw new RuntimeException(e);
        }
    }

    private void part(final String channelId) {
        try {
            LOG.info("Leaving {}", channelId);
            final TmiBot oldTmiBot = bots.remove(channelId);
            if (oldTmiBot != null)
                oldTmiBot.shutdown();
        } catch (final Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    void handleBotClose(final TmiBot tmiBot) {
        if(bots.remove(String.valueOf(tmiBot.getChannel().getId()), tmiBot))
            scheduledExecutorService.schedule(() -> join(String.valueOf(tmiBot.getChannel().getId())),
                    RECONNECT_DELAY_MS + RANDOM.nextInt(RECONNECT_SPLAY_MS), TimeUnit.MILLISECONDS);
    }

    @Override
    protected void processTenantConfigChangeEvent(final TenantConfigChangeEvent event) {
        reconfigure(event.getTenant());
    }

    @Override
    protected void processChannelGroupConfigChangeEvent(final ChannelGroupConfigChangeEvent event) {
        reconfigure(event.getChannelGroup());
    }

    @Override
    protected void processChannelConfigChangeEvent(final ChannelConfigChangeEvent event) {
        reconfigure(event.getChannel());
    }

    private void reconfigure(final Tenant tenant) {
        tenant.getChannelGroups().forEach(this::reconfigure);
    }

    private void reconfigure(final ChannelGroup channelGroup) {
        channelGroup.getChannels().forEach(this::reconfigure);
    }

    private void reconfigure(final tv.v1x1.common.dto.core.Channel channel) {
        if(!channel.getPlatform().equals(Platform.TWITCH))
            return;
        LOG.info("Configuration update for {} - scheduling rejoin", channel.getId());
        scheduledExecutorService.schedule(() -> rejoin(channel), 2, TimeUnit.SECONDS);
    }

    private void rejoin(final tv.v1x1.common.dto.core.Channel channel) {
        try {
            LOG.info("Leaving {} for rejoin", channel.getId());
            channelDistributor.removeEntry(channel.getChannelGroup().getId());
            LOG.info("Scheduling join for {}", channel.getId());
            scheduledExecutorService.schedule(() -> rejoinStage2(channel), 10, TimeUnit.SECONDS);
        } catch (final Exception e) {
            LOG.error("Got exception during rejoin", e);
        }
    }

    private void rejoinStage2(final tv.v1x1.common.dto.core.Channel channel) {
        try {
            LOG.info("Joining {} for rejoin", channel.getId());
            channelDistributor.addEntry(channel.getChannelGroup().getId());
        } catch (final Exception e) {
            LOG.error("Got exception during rejoin", e);
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
