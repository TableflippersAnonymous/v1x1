package tv.v1x1.modules.core.tmi;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.PrivateMessage;
import tv.v1x1.common.dto.core.TwitchBot;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.TwitchUser;
import tv.v1x1.common.dto.irc.IrcSource;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.IrcUser;
import tv.v1x1.common.dto.irc.MessageTaggedIrcStanza;
import tv.v1x1.common.dto.irc.commands.ClearChatCommand;
import tv.v1x1.common.dto.irc.commands.GlobalUserStateCommand;
import tv.v1x1.common.dto.irc.commands.HostTargetCommand;
import tv.v1x1.common.dto.irc.commands.JoinCommand;
import tv.v1x1.common.dto.irc.commands.ModeCommand;
import tv.v1x1.common.dto.irc.commands.NoticeCommand;
import tv.v1x1.common.dto.irc.commands.PartCommand;
import tv.v1x1.common.dto.irc.commands.PingCommand;
import tv.v1x1.common.dto.irc.commands.PrivmsgCommand;
import tv.v1x1.common.dto.irc.commands.ReconnectCommand;
import tv.v1x1.common.dto.irc.commands.RoomStateCommand;
import tv.v1x1.common.dto.irc.commands.RplEndOfMotdCommand;
import tv.v1x1.common.dto.irc.commands.RplNameReplyCommand;
import tv.v1x1.common.dto.irc.commands.UserNoticeCommand;
import tv.v1x1.common.dto.irc.commands.UserStateCommand;
import tv.v1x1.common.dto.irc.commands.WhisperCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.messages.events.TwitchBotChannelStateEvent;
import tv.v1x1.common.dto.messages.events.TwitchBotConnectedEvent;
import tv.v1x1.common.dto.messages.events.TwitchBotGlobalStateEvent;
import tv.v1x1.common.dto.messages.events.TwitchChannelEvent;
import tv.v1x1.common.dto.messages.events.TwitchChannelUsersEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatJoinEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatPartEvent;
import tv.v1x1.common.dto.messages.events.TwitchHostEvent;
import tv.v1x1.common.dto.messages.events.TwitchPingEvent;
import tv.v1x1.common.dto.messages.events.TwitchPrivateMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchRawMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchReconnectEvent;
import tv.v1x1.common.dto.messages.events.TwitchRoomStateEvent;
import tv.v1x1.common.dto.messages.events.TwitchTimeoutEvent;
import tv.v1x1.common.dto.messages.events.TwitchUserEvent;
import tv.v1x1.common.dto.messages.events.TwitchUserModChangeEvent;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.queue.MessageQueue;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.dto.channels.Channel;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.common.util.ratelimiter.RateLimiter;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/8/2016.
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
    private final Channel channel;
    private final UUID id = UUID.randomUUID();
    private Thread thread;
    private final TwitchDisplayNameService twitchDisplayNameService;
    private final Tracer tracer;

    public TmiBot(final String username, final String oauthToken, final MessageQueue queue,
                  final Module module, final RateLimiter joinLimiter, final RateLimiter messageLimiter,
                  final Deduplicator deduplicator, final TmiModule tmiModule, final Channel channel,
                  final TwitchDisplayNameService twitchDisplayNameService) {
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
        this.twitchDisplayNameService = twitchDisplayNameService;
        this.tracer = tmiModule.getInjector().getInstance(Tracer.class);
        log("Init: Constructed!");
    }

    @Override
    public void run() {
        thread = Thread.currentThread();
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
                for (; ; ) {
                    final String line = getLine();
                    final Span span = tracer.newTrace()
                            .name("TMI recv")
                            .start();
                    try {
                        if (line == null)
                            break;
                        processLine(line, span);
                    } finally {
                        span.finish();
                    }
                }
                log("Init: Attempting disconnect");
                disconnect();
            } catch (final IOException e) {
                LOG.info("IOException", e);
            } catch (final Throwable e) {
                LOG.error("Exception while parsing TMI line", e);
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
        if(socket != null)
            socket.close();
    }

    private void requestCaps() throws IOException {
        //sendLine("CAP REQ :twitch.tv/membership");
        sendLine("CAP REQ :twitch.tv/commands");
        sendLine("CAP REQ :twitch.tv/tags");
    }

    private void processLine(final String line, final Span rootSpan) throws IOException {
        log("Read: " + line);
        final IrcStanza stanza = IrcParser.parse(line, tracer, rootSpan);
        if(stanza == null)
            return;
        if(stanza instanceof PingCommand)
            sendLine("PONG :" + ((PingCommand) stanza).getToken());
        if(stanza instanceof JoinCommand)
            handleJoin((JoinCommand) stanza);
        if(stanza instanceof PartCommand)
            handlePart((PartCommand) stanza);
        if(stanza.getTags().containsKey("id") && deduplicator.seenAndAdd(new tv.v1x1.common.dto.core.UUID(UUID.fromString(stanza.getTags().get("id")))))
            return;
        if(stanza instanceof WhisperCommand && stanza.getTags().containsKey("thread-id") && stanza.getTags().containsKey("message-id")
                && deduplicator.seenAndAdd(new tv.v1x1.common.dto.core.UUID(UUID.nameUUIDFromBytes(CompositeKey.makeKey(stanza.getTags().get("thread-id"), stanza.getTags().get("message-id"))))))
            return;
        final Span span = tracer.newChild(rootSpan.context()).name("TMI event stanza").start();
        try {
            event(stanza, span);
        } catch(final NoSuchTargetException e) {
            throw new IllegalStateException(e);
        } finally {
            span.finish();
        }
    }

    private void handleJoin(final JoinCommand stanza) {
        if(!(stanza.getSource() instanceof IrcUser))
            return;
        final IrcUser user = (IrcUser) stanza.getSource();
        if(!user.getNickname().equals(username))
            return;
        if(!stanza.getChannel().equals("#" + channel.getName()))
            return;
        registerService();
    }

    private void handlePart(final PartCommand stanza) {
        if(!(stanza.getSource() instanceof IrcUser))
            return;
        final IrcUser user = (IrcUser) stanza.getSource();
        if(!user.getNickname().equals(username))
            return;
        if(!stanza.getChannel().equals("#" + channel.getName()))
            return;
        unregisterService();
    }

    private void event(final Event event, final Span parentSpan) {
        final TraceContext ctx = parentSpan.context();
        event.setContext(new Context(
                new tv.v1x1.common.dto.core.UUID(UUID.randomUUID()),
                new tv.v1x1.common.dto.core.UUID(new UUID(ctx.traceIdHigh(), ctx.traceId())),
                ctx.parentId(),
                ctx.spanId(),
                ctx.sampled()
        ));
        messageQueue.add(event);
    }

    private void event(final IrcStanza stanza, final Span parentSpan) throws NoSuchTargetException {
        cache(stanza, parentSpan);
        event(new TwitchRawMessageEvent(module, bot, stanza), parentSpan);
        if(stanza instanceof ClearChatCommand)
            event((ClearChatCommand) stanza, parentSpan);
        else if(stanza instanceof GlobalUserStateCommand)
            event((GlobalUserStateCommand) stanza, parentSpan);
        else if(stanza instanceof HostTargetCommand)
            event((HostTargetCommand) stanza, parentSpan);
        else if(stanza instanceof JoinCommand)
            event((JoinCommand) stanza, parentSpan);
        else if(stanza instanceof ModeCommand)
            event((ModeCommand) stanza, parentSpan);
        else if(stanza instanceof NoticeCommand)
            event((NoticeCommand) stanza, parentSpan);
        else if(stanza instanceof PartCommand)
            event((PartCommand) stanza, parentSpan);
        else if(stanza instanceof PingCommand)
            event((PingCommand) stanza, parentSpan);
        else if(stanza instanceof PrivmsgCommand)
            event((PrivmsgCommand) stanza, parentSpan);
        else if(stanza instanceof ReconnectCommand)
            event((ReconnectCommand) stanza, parentSpan);
        else if(stanza instanceof RoomStateCommand)
            event((RoomStateCommand) stanza, parentSpan);
        else if(stanza instanceof RplEndOfMotdCommand)
            event((RplEndOfMotdCommand) stanza, parentSpan);
        else if(stanza instanceof RplNameReplyCommand)
            event((RplNameReplyCommand) stanza, parentSpan);
        else if(stanza instanceof UserNoticeCommand)
            event((UserNoticeCommand) stanza, parentSpan);
        else if(stanza instanceof UserStateCommand)
            event((UserStateCommand) stanza, parentSpan);
        else if(stanza instanceof WhisperCommand)
            event((WhisperCommand) stanza, parentSpan);
        else
            throw new IllegalStateException("Unknown IrcStanza: " + stanza.getClass().getCanonicalName());
    }

    private void cache(final IrcStanza stanza, final Span parentSpan) {
        final Span span = tracer.newChild(parentSpan.context()).name("TMI populate cache").start();
        try {
            if (!(stanza instanceof MessageTaggedIrcStanza))
                return;
            final MessageTaggedIrcStanza messageTaggedIrcStanza = (MessageTaggedIrcStanza) stanza;
            final String userId = String.valueOf(messageTaggedIrcStanza.getUserId());
            final String displayName = messageTaggedIrcStanza.getDisplayName();
            if (!(messageTaggedIrcStanza.getSource() instanceof IrcUser))
                return;
            final String username = ((IrcUser) messageTaggedIrcStanza.getSource()).getNickname();
            twitchDisplayNameService.cache(userId, username, displayName);
        } finally {
            span.finish();
        }
    }

    private void event(final ClearChatCommand clearChatCommand, final Span parentSpan) throws NoSuchTargetException {
        event(new TwitchTimeoutEvent(module, getChannel(clearChatCommand), getUserByUsername(clearChatCommand.getNickname()), clearChatCommand), parentSpan);
    }

    private void event(final GlobalUserStateCommand globalUserStateCommand, final Span parentSpan) {
        event(new TwitchBotGlobalStateEvent(module, bot, globalUserStateCommand), parentSpan);
    }

    private void event(final HostTargetCommand hostTargetCommand, final Span parentSpan) throws NoSuchTargetException {
        event(new TwitchHostEvent(module, getChannel(hostTargetCommand), hostTargetCommand.getTargetChannel().equals("-") ? null : getChannelByName(hostTargetCommand.getTargetChannel()), hostTargetCommand), parentSpan);
    }

    private void event(final JoinCommand joinCommand, final Span parentSpan) throws NoSuchTargetException {
        event(new TwitchChatJoinEvent(module, getUser(joinCommand), getChannel(joinCommand), joinCommand), parentSpan);
    }

    private void event(final ModeCommand modeCommand, final Span parentSpan) throws NoSuchTargetException {
        for (final String username : modeCommand.getNicknames())
            event(new TwitchUserModChangeEvent(module, getChannel(modeCommand), getUserByUsername(username), modeCommand.getModeString().startsWith("+"), modeCommand), parentSpan);
    }

    private void event(final NoticeCommand noticeCommand, final Span parentSpan) throws NoSuchTargetException {
        event(new TwitchChannelEvent(module, getChannel(noticeCommand), noticeCommand.getMessage(), noticeCommand), parentSpan);
    }

    private void event(final PartCommand partCommand, final Span parentSpan) throws NoSuchTargetException {
        event(new TwitchChatPartEvent(module, getUser(partCommand), getChannel(partCommand), partCommand), parentSpan);
    }

    private void event(final PingCommand pingCommand, final Span parentSpan) {
        event(new TwitchPingEvent(module, pingCommand.getToken(), pingCommand), parentSpan);
    }

    private void event(final PrivmsgCommand privmsgCommand, final Span parentSpan) throws NoSuchTargetException {
        final TwitchChannel channel = getChannel(privmsgCommand);
        final TwitchUser user = getUser(privmsgCommand);
        if((privmsgCommand.getSource() instanceof IrcUser) && ((IrcUser) privmsgCommand.getSource()).getNickname().equals(username))
            return;
        final Set<String> badges = privmsgCommand.getBadges().stream().map(MessageTaggedIrcStanza.Badge::name).collect(Collectors.toSet());
        badges.add("_DEFAULT_");
        final List<Permission> permissions = tmiModule.getPermissions(channel.getChannelGroup().getTenant(), user.getGlobalUser(), channel.getChannelGroup().getId(), badges);
        final ChatMessage chatMessage = new ChatMessage(channel, user, privmsgCommand.getMessage(), permissions);
        event(new TwitchChatMessageEvent(module, chatMessage, privmsgCommand), parentSpan);
    }

    private void event(final ReconnectCommand reconnectCommand, final Span parentSpan) {
        event(new TwitchReconnectEvent(module, bot, reconnectCommand), parentSpan);
    }

    private void event(final RoomStateCommand roomStateCommand, final Span parentSpan) throws NoSuchTargetException {
        event(new TwitchRoomStateEvent(module, getChannel(roomStateCommand), roomStateCommand), parentSpan);
    }

    private void event(final RplEndOfMotdCommand rplEndOfMotdCommand, final Span parentSpan) {
        event(new TwitchBotConnectedEvent(module, bot, rplEndOfMotdCommand), parentSpan);
    }

    private void event(final RplNameReplyCommand rplNameReplyCommand, final Span parentSpan) throws NoSuchTargetException {
        event(new TwitchChannelUsersEvent(module, getChannel(rplNameReplyCommand), rplNameReplyCommand.getMembers().stream().map(member -> {
            try {
                return getUserByUsername(member.getNickname());
            } catch (final NoSuchTargetException e) {
                throw new IllegalStateException(e);
            }
        }).collect(Collectors.toList()), rplNameReplyCommand), parentSpan);
    }

    private void event(final UserNoticeCommand userNoticeCommand, final Span parentSpan) {
        try {
            event(new TwitchUserEvent(module, getChannel(userNoticeCommand), getUserByUsername(userNoticeCommand.getLogin()), userNoticeCommand.getMessage(), userNoticeCommand), parentSpan);
        } catch (final NoSuchTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private void event(final UserStateCommand userStateCommand, final Span parentSpan) throws NoSuchTargetException {
        event(new TwitchBotChannelStateEvent(module, getChannel(userStateCommand), bot, userStateCommand), parentSpan);
    }

    private void event(final WhisperCommand whisperCommand, final Span parentSpan) {
        event(new TwitchPrivateMessageEvent(module, new PrivateMessage(bot, getUser(whisperCommand), whisperCommand.getMessage()), whisperCommand), parentSpan);
    }

    private TwitchUser getUser(final IrcStanza stanza) {
        final IrcSource source = stanza.getSource();

        final String displayName = stanza.getTags().get("display-name");

        try {
            // If we have a user-id, use that.
            if(stanza.getTags().containsKey("user-id"))
                return getUser(stanza.getTags().get("user-id"), displayName);

            // Otherwise, we need to extract a username and look it up in the TwitchDisplayNameService
            if (source instanceof IrcUser)
                return getUser(twitchDisplayNameService.getUserIdFromUsername(((IrcUser) source).getNickname()), displayName);
            else if (stanza.getTags().containsKey("login"))
                return getUser(twitchDisplayNameService.getUserIdFromUsername(stanza.getTags().get("login")), displayName);
        } catch(final NoSuchTargetException e) {
            throw new IllegalArgumentException(e);
        }
        throw new IllegalArgumentException("Not able to find a TwitchUser in stanza: " + stanza.getRawLine());
    }

    private TwitchUser getUserByUsername(final String username) throws NoSuchTargetException {
        return getUser(twitchDisplayNameService.getUserIdFromUsername(username), null);
    }

    private TwitchUser getUser(final String id, final String displayName) throws NoSuchTargetException {
        return new TwitchUser(id, tmiModule.getGlobalUser(id, displayName), displayName == null ? twitchDisplayNameService.getDisplayNameFromUserId(id) : displayName);
    }

    private TwitchChannel getChannel(final IrcStanza stanza) throws NoSuchTargetException {
        // If we have a room-id, use that.
        if(stanza.getTags().containsKey("room-id"))
            return getChannel(stanza.getTags().get("room-id"));

        switch(stanza.getCommand()) {
            case JOIN: return getChannelByName(((JoinCommand) stanza).getChannel());
            case PART: return getChannelByName(((PartCommand) stanza).getChannel());
            case PRIVMSG: return getChannelByName(((PrivmsgCommand) stanza).getChannel());
            case RPL_NAMREPLY: return getChannelByName(((RplNameReplyCommand) stanza).getChannel());
            case MODE: return getChannelByName(((ModeCommand) stanza).getChannel());
            case NOTICE: return getChannelByName(((NoticeCommand) stanza).getChannel());
            case HOSTTARGET: return getChannelByName(((HostTargetCommand) stanza).getChannel());
            case CLEARCHAT: return getChannelByName(((ClearChatCommand) stanza).getChannel());
            case USERSTATE: return getChannelByName(((UserStateCommand) stanza).getChannel());
            case ROOMSTATE: return getChannelByName(((RoomStateCommand) stanza).getChannel());
            case USERNOTICE: return getChannelByName(((UserNoticeCommand) stanza).getChannel());
            default: throw new IllegalArgumentException("Not able to find a TwitchChannel in stanza: " + stanza.getRawLine());
        }
    }

    private TwitchChannel getChannelByName(String channelName) throws NoSuchTargetException {
        if(channelName.startsWith("#"))
            channelName = channelName.substring(1);
        return getChannel(twitchDisplayNameService.getChannelIdByChannelName(channelName));
    }

    private TwitchChannel getChannel(final String id) throws NoSuchTargetException {
        final String displayName = twitchDisplayNameService.getDisplayNameFromChannelId(id);
        return getChannel(id, displayName);
    }

    private TwitchChannel getChannel(final String id, final String displayName) {
        return new TwitchChannel(id + ":main", tmiModule.getChannelGroup(id), displayName);
    }

    private void connect() throws IOException {
        socket = SSLSocketFactory.getDefault().createSocket("irc.chat.twitch.tv", 6697);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new BufferedOutputStream(socket.getOutputStream());
    }

    private void sendLine(final String line) throws IOException {
        log("Write: " + line);
        if(outputStream != null) {
            outputStream.write((line + "\r\n").getBytes());
            outputStream.flush();
        }
    }

    private void authenticate() throws InterruptedException {
        joinLimiter.submitAndWait(() -> {
            try {
                sendLine("PASS :" + oauthToken);
                sendLine("USER " + username + " \"v1x1.tv\" \"irc.chat.twitch.tv\" :" + username);
                sendLine("NICK " + username);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void joinChannels() throws Exception {
        join("#" + channel.getName());
    }

    private void join(final String channel) throws InterruptedException {
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
        disconnect();
        if(thread != null)
            thread.interrupt();
    }

    public void sendMessage(final String channelId, final String text) throws InterruptedException {
        messageLimiter.submitAndWait(() -> {
            try {
                sendLine("PRIVMSG #" + twitchDisplayNameService.getChannelNameFromChannelId(channelId) + " :" + text);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void registerService() {
        service = new TmiService(tmiModule, String.valueOf(channel.getId() + ":main"), this);
        service.start();
    }

    private void unregisterService() {
        if(service != null)
            service.shutdown();
    }

    private void log(final String m) {
        LOG.info("[{}:{}] [{}] {}", username, id, channel.getDisplayName(), m.replace(oauthToken, "<oauth token removed>"));
    }
}
