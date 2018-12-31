package tv.v1x1.modules.core.tmi;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.TwitchBot;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.IrcUser;
import tv.v1x1.common.dto.irc.MessageTaggedIrcStanza;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.queue.MessageQueue;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.dto.channels.Channel;
import tv.v1x1.common.util.ratelimiter.RateLimiter;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

/**
 * Created by cobi on 10/8/2016.
 */
public class TmiBot {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private volatile boolean running;
    private volatile ChannelFuture channelFuture;
    private final String oauthToken;
    private final String username;
    final Module module;
    final TwitchBot bot;
    private final MessageQueue messageQueue;
    private final RateLimiter joinLimiter;
    private final RateLimiter messageLimiter;
    private final Deduplicator deduplicator;
    private TmiService service;
    final TmiModule tmiModule;
    private final Channel channel;
    private final UUID id = UUID.randomUUID();
    private final TwitchDisplayNameService twitchDisplayNameService;
    private final Tracer tracer;
    private final EventLoopGroup eventLoopGroup;

    public TmiBot(final String username, final String oauthToken, final MessageQueue queue,
                  final Module module, final RateLimiter joinLimiter, final RateLimiter messageLimiter,
                  final Deduplicator deduplicator, final TmiModule tmiModule, final Channel channel,
                  final TwitchDisplayNameService twitchDisplayNameService, final EventLoopGroup eventLoopGroup) {
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
        this.eventLoopGroup = eventLoopGroup;
        log("Init: Constructed!");
    }

    void cleanup() {
        unregisterService();
    }

    void disconnect() {
        unregisterService();
        quit();
    }

    void event(final Event event, final Span parentSpan) {
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

    void cache(final IrcStanza stanza, final Span parentSpan) {
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

    void connect() {
        final Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new TmiClientInitializer(this, username, channel, deduplicator, tracer, twitchDisplayNameService));
        channelFuture = b.connect("irc.chat.twitch.tv", 6697);
    }

    void sendLine(final String line) {
        log("Write: " + line);
        if(channelFuture != null && channelFuture.channel().isOpen())
            channelFuture.channel().writeAndFlush(line + "\r\n");
    }

    void joinChannels() {
        join("#" + channel.getName());
    }

    private void join(final String channel) {
        joinLimiter.submit(() -> sendLine("JOIN " + channel));
    }

    private void quit() {
        sendLine("QUIT :Disconnecting.");
        if(channelFuture != null && channelFuture.channel().isOpen())
            channelFuture.channel().close();
    }

    public void shutdown() {
        running = false;
        disconnect();
    }

    public void sendMessage(final String channelId, final String text) {
        messageLimiter.submit(() -> {
            try {
                sendLine("PRIVMSG #" + twitchDisplayNameService.getChannelNameFromChannelId(channelId) + " :" + text);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    void registerService() {
        service = new TmiService(tmiModule, channel.getId() + ":main", this);
        service.start();
    }

    void unregisterService() {
        if(service != null)
            service.shutdown();
    }

    void log(final String m) {
        LOG.info("[{}:{}] [{}] {}", username, id, channel.getDisplayName(), m.replace(oauthToken, "<oauth token removed>"));
    }

    public boolean isRunning() {
        return running;
    }

    void login() {
        joinLimiter.submit(() -> {
            sendLine("PASS :oauth:" + oauthToken);
            sendLine("USER " + username + " \"v1x1.tv\" \"irc.chat.twitch.tv\" :" + username);
            sendLine("NICK " + username);
            sendLine("CAP REQ :twitch.tv/commands twitch.tv/tags");
            this.joinChannels();
        });
    }

    public Channel getChannel() {
        return channel;
    }
}
