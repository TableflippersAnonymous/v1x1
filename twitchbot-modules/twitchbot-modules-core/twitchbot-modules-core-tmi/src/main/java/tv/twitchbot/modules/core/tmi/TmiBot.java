package tv.twitchbot.modules.core.tmi;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.ClearChatCommand;
import tv.twitchbot.common.dto.irc.commands.GlobalUserStateCommand;
import tv.twitchbot.common.dto.irc.commands.HostTargetCommand;
import tv.twitchbot.common.dto.irc.commands.PingCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.messages.events.TwitchBotGlobalStateEvent;
import tv.twitchbot.common.dto.messages.events.TwitchHostEvent;
import tv.twitchbot.common.dto.messages.events.TwitchRawMessageEvent;
import tv.twitchbot.common.dto.messages.events.TwitchTimeoutEvent;
import tv.twitchbot.common.services.coordination.LoadBalancingDistributor;
import tv.twitchbot.common.services.persistence.DAOManager;
import tv.twitchbot.common.services.queue.MessageQueue;

import java.io.*;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 10/8/2016.
 */
public class TmiBot implements Runnable {
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
    private Set<String> channels = new ConcurrentSkipListSet<>();
    private final LoadingCache<String, Tenant> tenantCache;
    private final LoadingCache<String, GlobalUser> globalUserCache;

    public TmiBot(String username, String oauthToken, LoadBalancingDistributor distributor, MessageQueue queue, Module module, DAOManager daoManager) {
        this.oauthToken = oauthToken;
        this.username = username;
        this.module = module;
        this.channelDistributor = distributor;
        this.messageQueue = queue;
        this.daoManager = daoManager;
        this.bot = new TwitchBot(username);
        listener = (instanceId, entries) -> {
            if (botId.equals(instanceId.getValue())) {
                try {
                    setChannels(entries);
                } catch (IOException e) {
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
    }

    private void event(ClearChatCommand clearChatCommand) {
        event(new TwitchTimeoutEvent(module, getChannel(clearChatCommand.getChannel()), getUser(clearChatCommand.getNickname(), null), clearChatCommand));
    }

    private void event(GlobalUserStateCommand globalUserStateCommand) {
        event(new TwitchBotGlobalStateEvent(module, bot, globalUserStateCommand));
    }

    private void event(HostTargetCommand hostTargetCommand) {
        event(new TwitchHostEvent(module, getChannel(hostTargetCommand.getChannel()), getChannel(hostTargetCommand.getTargetChannel()), hostTargetCommand));
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

    private void join(String channel) throws IOException {
        channels.add(channel);
        sendLine("JOIN " + channel);
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

    public void sendMessage(String channel, String text) throws IOException {
        sendLine("PRIVMSG " + channel + " :" + text);
    }

    private void setChannels(Set<String> channels) throws IOException {
        for(String channel : this.channels)
            if(!channels.contains(channel))
                part(channel);
        for(String channel : channels)
            if(!this.channels.contains(channel))
                join(channel);
    }
}
