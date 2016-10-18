package tv.twitchbot.modules.core.tmi;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.db.TenantUserPermissions;
import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.IrcUser;
import tv.twitchbot.common.dto.irc.commands.*;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.messages.events.*;
import tv.twitchbot.common.services.coordination.LoadBalancingDistributor;
import tv.twitchbot.common.services.persistence.DAOManager;
import tv.twitchbot.common.services.persistence.Deduplicator;
import tv.twitchbot.common.services.queue.MessageQueue;
import tv.twitchbot.common.util.ratelimiter.RateLimiter;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiBot implements Runnable {
    private static class Pair<A, B> {
        private A first;
        private B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair<?, ?> pair = (Pair<?, ?>) o;

            if (first != null ? !first.equals(pair.first) : pair.first != null) return false;
            return second != null ? second.equals(pair.second) : pair.second == null;

        }

        @Override
        public int hashCode() {
            int result = first != null ? first.hashCode() : 0;
            result = 31 * result + (second != null ? second.hashCode() : 0);
            return result;
        }
    }

    private final LoadBalancingDistributor.Listener listener;
    private volatile boolean running;
    private volatile Socket socket;
    private volatile InputStream inputStream;
    private volatile BufferedReader reader;
    private volatile BufferedOutputStream outputStream;
    private final String oauthToken;
    private final String username;
    private final Module module;
    private final TwitchBot bot;
    private final UUID botId = UUID.randomUUID();
    private final LoadBalancingDistributor channelDistributor;
    private final MessageQueue messageQueue;
    private final DAOManager daoManager;
    private final Set<String> channels = new ConcurrentSkipListSet<>();
    private final LoadingCache<String, Tenant> tenantCache;
    private final LoadingCache<String, GlobalUser> globalUserCache;
    private final LoadingCache<Pair<Tenant, GlobalUser>, List<Permission>> permissionCache;
    private final RateLimiter joinLimiter;
    private final RateLimiter messageLimiter;
    private final Deduplicator deduplicator;

    public TmiBot(String username, String oauthToken, LoadBalancingDistributor distributor, MessageQueue queue, Module module, DAOManager daoManager, RateLimiter joinLimiter, RateLimiter messageLimiter, Deduplicator deduplicator) {
        this.oauthToken = oauthToken;
        this.username = username;
        this.module = module;
        this.channelDistributor = distributor;
        this.messageQueue = queue;
        this.daoManager = daoManager;
        this.joinLimiter = joinLimiter;
        this.messageLimiter = messageLimiter;
        this.deduplicator = deduplicator;
        this.bot = new TwitchBot(username);
        listener = (instanceId, entries) -> {
            if (botId.equals(instanceId.getValue())) {
                try {
                    setChannels(entries);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        channelDistributor.addListener(listener);
        this.tenantCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Tenant>() {
                    @Override
                    public Tenant load(String s) throws Exception {
                        return daoManager.getDaoTenant().getByChannel(Platform.TWITCH, s).toCore();
                    }
                });
        this.globalUserCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, GlobalUser>() {
                    @Override
                    public GlobalUser load(String s) throws Exception {
                        return daoManager.getDaoGlobalUser().getByUser(Platform.TWITCH, s).toCore();
                    }
                });
        this.permissionCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(new CacheLoader<Pair<Tenant, GlobalUser>, List<Permission>>() {
                    @Override
                    public List<Permission> load(Pair<Tenant, GlobalUser> tenantGlobalUserPair) throws Exception {
                        TenantUserPermissions permissions = daoManager.getDaoTenantUserPermissions().getByTenantAndUser(tenantGlobalUserPair.getFirst().getId().getValue(), tenantGlobalUserPair.getSecond().getId().getValue());
                        return permissions.getPermissions().stream().map(TenantUserPermissions.Permission::toCore).collect(Collectors.toList());
                    }
                });
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                connect();
                authenticate();
                requestCaps();
                joinChannels();
                for(;;) {
                    String line = getLine();
                    if (line == null)
                        break;
                    processLine(line);
                }
                disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cleanup();
            }
        }
        channelDistributor.removeListener(listener);
    }

    private void cleanup() {
        try {
            channelDistributor.removeInstance(new tv.twitchbot.common.dto.core.UUID(botId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() throws IOException {
        try {
            channelDistributor.removeInstance(new tv.twitchbot.common.dto.core.UUID(botId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        quit();
        socket.close();
    }

    private void requestCaps() throws IOException {
        sendLine("CAP REQ :twitch.tv/membership");
        sendLine("CAP REQ :twitch.tv/commands");
        sendLine("CAP REQ :twitch.tv/tags");
    }

    private void processLine(String line) throws IOException {
        IrcStanza stanza = IrcParser.parse(line);
        if(stanza instanceof PingCommand)
            sendLine("PONG :" + ((PingCommand) stanza).getToken());
        if(stanza.getTags().containsKey("id") && deduplicator.seenAndAdd(new tv.twitchbot.common.dto.core.UUID(UUID.fromString(stanza.getTags().get("id")))))
            return;
        event(stanza);
    }

    private void event(Event event) {
        messageQueue.add(event);
    }

    private void event(IrcStanza stanza) {
        event(new TwitchRawMessageEvent(module, bot, stanza));
        if(stanza instanceof ClearChatCommand)
            event((ClearChatCommand) stanza);
        else if(stanza instanceof GlobalUserStateCommand)
            event((GlobalUserStateCommand) stanza);
        else if(stanza instanceof HostTargetCommand)
            event((HostTargetCommand) stanza);
        else if(stanza instanceof JoinCommand)
            event((JoinCommand) stanza);
        else if(stanza instanceof ModeCommand)
            event((ModeCommand) stanza);
        else if(stanza instanceof NoticeCommand)
            event((NoticeCommand) stanza);
        else if(stanza instanceof PartCommand)
            event((PartCommand) stanza);
        else if(stanza instanceof PingCommand)
            event((PingCommand) stanza);
        else if(stanza instanceof PrivmsgCommand)
            event((PrivmsgCommand) stanza);
        else if(stanza instanceof ReconnectCommand)
            event((ReconnectCommand) stanza);
        else if(stanza instanceof RoomStateCommand)
            event((RoomStateCommand) stanza);
        else if(stanza instanceof RplEndOfMotdCommand)
            event((RplEndOfMotdCommand) stanza);
        else if(stanza instanceof RplNameReplyCommand)
            event((RplNameReplyCommand) stanza);
        else if(stanza instanceof UserNoticeCommand)
            event((UserNoticeCommand) stanza);
        else if(stanza instanceof UserStateCommand)
            event((UserStateCommand) stanza);
        else
            throw new IllegalStateException("Unknown IrcStanza: " + stanza.getClass().getCanonicalName());
    }

    private void event(ClearChatCommand clearChatCommand) {
        event(new TwitchTimeoutEvent(module, getChannel(clearChatCommand.getChannel()), getUser(clearChatCommand.getNickname()), clearChatCommand));
    }

    private void event(GlobalUserStateCommand globalUserStateCommand) {
        event(new TwitchBotGlobalStateEvent(module, bot, globalUserStateCommand));
    }

    private void event(HostTargetCommand hostTargetCommand) {
        event(new TwitchHostEvent(module, getChannel(hostTargetCommand.getChannel()), getChannel(hostTargetCommand.getTargetChannel()), hostTargetCommand));
    }

    private void event(JoinCommand joinCommand) {
        event(new TwitchChatJoinEvent(module, getUser(joinCommand), getChannel(joinCommand.getChannel()), joinCommand));
    }

    private void event(ModeCommand modeCommand) {
        for(String username : modeCommand.getNicknames())
            event(new TwitchUserModChangeEvent(module, getChannel(modeCommand.getChannel()), getUser(username), modeCommand.getModeString().startsWith("+"), modeCommand));
    }

    private void event(NoticeCommand noticeCommand) {
        event(new TwitchChannelEvent(module, getChannel(noticeCommand.getChannel()), noticeCommand.getMessage(), noticeCommand));
    }

    private void event(PartCommand partCommand) {
        event(new TwitchChatPartEvent(module, getUser(partCommand), getChannel(partCommand.getChannel()), partCommand));
    }

    private void event(PingCommand pingCommand) {
        event(new TwitchPingEvent(module, pingCommand.getToken(), pingCommand));
    }

    private void event(PrivmsgCommand privmsgCommand) {
        TwitchChannel channel = getChannel(privmsgCommand.getChannel());
        TwitchUser user = getUser(privmsgCommand);
        event(new TwitchChatMessageEvent(module, new ChatMessage(channel, user, privmsgCommand.getMessage(), getPermissions(channel.getTenant(), user.getGlobalUser())), privmsgCommand));
    }

    private void event(ReconnectCommand reconnectCommand) {
        event(new TwitchReconnectEvent(module, bot, reconnectCommand));
    }

    private void event(RoomStateCommand roomStateCommand) {
        event(new TwitchRoomStateEvent(module, getChannel(roomStateCommand.getChannel()), roomStateCommand));
    }

    private void event(RplEndOfMotdCommand rplEndOfMotdCommand) {
        event(new TwitchBotConnectedEvent(module, bot, rplEndOfMotdCommand));
    }

    private void event(RplNameReplyCommand rplNameReplyCommand) {
        event(new TwitchChannelUsersEvent(module, getChannel(rplNameReplyCommand.getChannel()), rplNameReplyCommand.getMembers().stream().map(member -> getUser(member.getNickname())).collect(Collectors.toList()), rplNameReplyCommand));
    }

    private void event(UserNoticeCommand userNoticeCommand) {
        event(new TwitchUserEvent(module, getChannel(userNoticeCommand.getChannel()), getUser(userNoticeCommand.getLogin()), userNoticeCommand.getMessage(), userNoticeCommand));
    }

    private void event(UserStateCommand userStateCommand) {
        event(new TwitchBotChannelStateEvent(module, getChannel(userStateCommand.getChannel()), bot, userStateCommand));
    }

    private List<Permission> getPermissions(Tenant tenant, GlobalUser globalUser) {
        try {
            return permissionCache.get(new Pair<>(tenant, globalUser));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private TwitchUser getUser(IrcStanza stanza) {
        IrcSource source = stanza.getSource();
        if(source instanceof IrcUser)
            return getUser(((IrcUser) source).getNickname(), stanza.getTags().get("display-name"));
        else if(stanza.getTags().containsKey("login"))
            return getUser(stanza.getTags().get("login"), stanza.getTags().get("display-name"));
        throw new IllegalArgumentException("Not able to find a TwitchUser in stanza: " + stanza.getRawLine());
    }

    private TwitchUser getUser(String id) {
        return getUser(id, null);
    }

    private TwitchUser getUser(String id, String displayName) {
        return new TwitchUser(id, getGlobalUser(id), displayName == null ? id : displayName);
    }

    private GlobalUser getGlobalUser(String id) {
        try {
            return globalUserCache.get(id);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private TwitchChannel getChannel(String id) {
        return new TwitchChannel(id, getTenant(id), id);
    }

    private Tenant getTenant(String id) {
        try {
            return tenantCache.get(id);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect() throws IOException {
        socket = new Socket("irc.chat.twitch.tv", 6667);
        inputStream = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        outputStream = new BufferedOutputStream(socket.getOutputStream());
    }

    private void sendLine(String line) throws IOException {
        outputStream.write((line + "\r\n").getBytes());
        outputStream.flush();
    }

    private void authenticate() throws IOException {
        sendLine("PASS oauth:" + oauthToken);
        sendLine("USER " + username + " \"twitchbot.tv\" \"irc.chat.twitch.tv\" :" + username);
        sendLine("NICK " + username);
    }

    private void joinChannels() throws Exception {
        channelDistributor.addInstance(new tv.twitchbot.common.dto.core.UUID(botId));
    }

    private void join(String channel) throws IOException, InterruptedException {
        joinLimiter.submitAndWait(() -> {
            channels.add(channel);
            try {
                sendLine("JOIN " + channel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void part(String channel) throws IOException {
        channels.remove(channel);
        sendLine("PART " + channel);
    }

    private void quit() throws IOException {
        channels.clear();
        sendLine("QUIT :Disconnecting.");
    }

    private String getLine() throws IOException {
        return reader.readLine();
    }

    public void shutdown() throws IOException {
        running = false;
        quit();
    }

    public void sendMessage(String channel, String text) throws IOException, InterruptedException {
        messageLimiter.submitAndWait(() -> {
            try {
                sendLine("PRIVMSG " + channel + " :" + text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setChannels(Set<String> channels) throws IOException, InterruptedException {
        for(String channel : this.channels)
            if(!channels.contains(channel))
                part(channel);
        for(String channel : channels)
            if(!this.channels.contains(channel))
                join(channel);
    }
}
