package tv.twitchbot.modules.core.tmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.IrcUser;
import tv.twitchbot.common.dto.irc.commands.*;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.messages.events.*;
import tv.twitchbot.common.services.persistence.Deduplicator;
import tv.twitchbot.common.services.queue.MessageQueue;
import tv.twitchbot.common.util.ratelimiter.RateLimiter;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiBot implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private volatile boolean running;
    private volatile Socket socket;
    private volatile BufferedReader reader;
    private volatile BufferedOutputStream outputStream;
    private final String oauthToken;
    private final String username;
    private final Module module;
    private final TwitchBot bot;
    private final MessageQueue messageQueue;
    private final RateLimiter joinLimiter;
    private final RateLimiter messageLimiter;
    private final Deduplicator deduplicator;
    private TmiService service;
    private final TmiModule tmiModule;
    private final String channel;
    private final UUID id = UUID.randomUUID();

    public TmiBot(final String username, final String oauthToken, final MessageQueue queue,
                  final Module module, final RateLimiter joinLimiter, final RateLimiter messageLimiter,
                  final Deduplicator deduplicator, final TmiModule tmiModule, final String channel) {
        this.oauthToken = oauthToken;
        this.username = username;
        this.module = module;
        this.messageQueue = queue;
        this.joinLimiter = joinLimiter;
        this.messageLimiter = messageLimiter;
        this.deduplicator = deduplicator;
        this.tmiModule = tmiModule;
        this.channel = channel;
        this.bot = new TwitchBot(username);
        log("Init: Constructed!");
    }

    @Override
    public void run() {
        log("Init: Hello, World!");
        running = true;
        while(running) {
            try {
                log("Init: Attempting connect");
                connect();
                log("Init: Attempting auth");
                authenticate();
                log("Init: Attempting caps");
                requestCaps();
                log("Init: Attempting join");
                joinChannels();
                for(;;) {
                    final String line = getLine();
                    if (line == null)
                        break;
                    processLine(line);
                }
                log("Init: Attempting disconnect");
                disconnect();
            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
                log("Init: Attempting cleanup");
                cleanup();
            }
        }
    }

    private void cleanup() {
        unregisterService();
    }

    private void disconnect() throws IOException {
        unregisterService();
        quit();
        socket.close();
    }

    private void requestCaps() throws IOException {
        sendLine("CAP REQ :twitch.tv/membership");
        sendLine("CAP REQ :twitch.tv/commands");
        sendLine("CAP REQ :twitch.tv/tags");
    }

    private void processLine(final String line) throws IOException {
        log("Read: " + line);
        final IrcStanza stanza = IrcParser.parse(line);
        if(stanza == null)
            return;
        if(stanza instanceof PingCommand)
            sendLine("PONG :" + ((PingCommand) stanza).getToken());
        if(stanza instanceof JoinCommand)
            handleJoin((JoinCommand) stanza);
        if(stanza instanceof PartCommand)
            handlePart((PartCommand) stanza);
        if(stanza.getTags().containsKey("id") && deduplicator.seenAndAdd(new tv.twitchbot.common.dto.core.UUID(UUID.fromString(stanza.getTags().get("id")))))
            return;
        event(stanza);
    }

    private void handleJoin(final JoinCommand stanza) {
        if(!(stanza.getSource() instanceof IrcUser))
            return;
        final IrcUser user = (IrcUser) stanza.getSource();
        if(!user.getNickname().equals(username))
            return;
        if(!stanza.getChannel().equals(channel))
            return;
        registerService();
    }

    private void handlePart(final PartCommand stanza) {
        if(!(stanza.getSource() instanceof IrcUser))
            return;
        final IrcUser user = (IrcUser) stanza.getSource();
        if(!user.getNickname().equals(username))
            return;
        if(!stanza.getChannel().equals(channel))
            return;
        unregisterService();
    }

    private void event(final Event event) {
        messageQueue.add(event);
    }

    private void event(final IrcStanza stanza) {
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

    private void event(final ClearChatCommand clearChatCommand) {
        event(new TwitchTimeoutEvent(module, getChannel(clearChatCommand.getChannel()), getUser(clearChatCommand.getNickname()), clearChatCommand));
    }

    private void event(final GlobalUserStateCommand globalUserStateCommand) {
        event(new TwitchBotGlobalStateEvent(module, bot, globalUserStateCommand));
    }

    private void event(final HostTargetCommand hostTargetCommand) {
        event(new TwitchHostEvent(module, getChannel(hostTargetCommand.getChannel()), getChannel(hostTargetCommand.getTargetChannel()), hostTargetCommand));
    }

    private void event(final JoinCommand joinCommand) {
        event(new TwitchChatJoinEvent(module, getUser(joinCommand), getChannel(joinCommand.getChannel()), joinCommand));
    }

    private void event(final ModeCommand modeCommand) {
        for(final String username : modeCommand.getNicknames())
            event(new TwitchUserModChangeEvent(module, getChannel(modeCommand.getChannel()), getUser(username), modeCommand.getModeString().startsWith("+"), modeCommand));
    }

    private void event(final NoticeCommand noticeCommand) {
        event(new TwitchChannelEvent(module, getChannel(noticeCommand.getChannel()), noticeCommand.getMessage(), noticeCommand));
    }

    private void event(final PartCommand partCommand) {
        event(new TwitchChatPartEvent(module, getUser(partCommand), getChannel(partCommand.getChannel()), partCommand));
    }

    private void event(final PingCommand pingCommand) {
        event(new TwitchPingEvent(module, pingCommand.getToken(), pingCommand));
    }

    private void event(final PrivmsgCommand privmsgCommand) {
        final TwitchChannel channel = getChannel(privmsgCommand.getChannel());
        final TwitchUser user = getUser(privmsgCommand);
        if(user.getId().equals(username))
            return;
        event(new TwitchChatMessageEvent(module, new ChatMessage(channel, user, privmsgCommand.getMessage(), tmiModule.getPermissions(channel.getTenant(), user.getGlobalUser())), privmsgCommand));
    }

    private void event(final ReconnectCommand reconnectCommand) {
        event(new TwitchReconnectEvent(module, bot, reconnectCommand));
    }

    private void event(final RoomStateCommand roomStateCommand) {
        event(new TwitchRoomStateEvent(module, getChannel(roomStateCommand.getChannel()), roomStateCommand));
    }

    private void event(final RplEndOfMotdCommand rplEndOfMotdCommand) {
        event(new TwitchBotConnectedEvent(module, bot, rplEndOfMotdCommand));
    }

    private void event(final RplNameReplyCommand rplNameReplyCommand) {
        event(new TwitchChannelUsersEvent(module, getChannel(rplNameReplyCommand.getChannel()), rplNameReplyCommand.getMembers().stream().map(member -> getUser(member.getNickname())).collect(Collectors.toList()), rplNameReplyCommand));
    }

    private void event(final UserNoticeCommand userNoticeCommand) {
        event(new TwitchUserEvent(module, getChannel(userNoticeCommand.getChannel()), getUser(userNoticeCommand.getLogin()), userNoticeCommand.getMessage(), userNoticeCommand));
    }

    private void event(final UserStateCommand userStateCommand) {
        event(new TwitchBotChannelStateEvent(module, getChannel(userStateCommand.getChannel()), bot, userStateCommand));
    }

    private TwitchUser getUser(final IrcStanza stanza) {
        final IrcSource source = stanza.getSource();
        if(source instanceof IrcUser)
            return getUser(((IrcUser) source).getNickname(), stanza.getTags().get("display-name"));
        else if(stanza.getTags().containsKey("login"))
            return getUser(stanza.getTags().get("login"), stanza.getTags().get("display-name"));
        throw new IllegalArgumentException("Not able to find a TwitchUser in stanza: " + stanza.getRawLine());
    }

    private TwitchUser getUser(final String id) {
        return getUser(id, null);
    }

    private TwitchUser getUser(final String id, final String displayName) {
        return new TwitchUser(id, tmiModule.getGlobalUser(id), displayName == null ? id : displayName);
    }

    private TwitchChannel getChannel(final String id) {
        return new TwitchChannel(id, tmiModule.getTenant(id), id);
    }

    private void connect() throws IOException {
        socket = new Socket("irc.chat.twitch.tv", 6667);
        final InputStream inputStream = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        outputStream = new BufferedOutputStream(socket.getOutputStream());
    }

    private void sendLine(final String line) throws IOException {
        log("Write: " + line);
        outputStream.write((line + "\r\n").getBytes());
        outputStream.flush();
    }

    private void authenticate() throws IOException, InterruptedException {
        joinLimiter.submitAndWait(() -> {
            try {
                sendLine("PASS :" + oauthToken);
                sendLine("USER " + username + " \"twitchbot.tv\" \"irc.chat.twitch.tv\" :" + username);
                sendLine("NICK " + username);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void joinChannels() throws Exception {
        join(channel);
    }

    private void join(final String channel) throws IOException, InterruptedException {
        joinLimiter.submitAndWait(() -> {
            try {
                sendLine("JOIN " + channel);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void part(final String channel) throws IOException {
        sendLine("PART " + channel);
    }

    private void quit() throws IOException {
        sendLine("QUIT :Disconnecting.");
    }

    private String getLine() throws IOException {
        return reader.readLine();
    }

    public void shutdown() throws IOException {
        running = false;
        quit();
    }

    public void sendMessage(final String channel, final String text) throws IOException, InterruptedException {
        messageLimiter.submitAndWait(() -> {
            try {
                sendLine("PRIVMSG " + channel + " :" + text);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void registerService() {
        service = new TmiService(tmiModule, channel, this);
        service.start();
    }

    private void unregisterService() {
        if(service != null)
            service.shutdown();
    }

    private void log(final String m) {
        LOG.info("[{}:{}] [{}] {}", username, id, channel, m);
    }
}
