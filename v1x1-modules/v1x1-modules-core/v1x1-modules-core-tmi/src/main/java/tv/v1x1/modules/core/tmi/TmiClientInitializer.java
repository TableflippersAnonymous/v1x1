package tv.v1x1.modules.core.tmi;

import brave.Tracer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.dto.channels.Channel;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;


public class TmiClientInitializer extends ChannelInitializer<SocketChannel> {
    private final TmiBot tmiBot;
    private final String username;
    private final Channel channel;
    private final Deduplicator deduplicator;
    private final Tracer tracer;
    private final TwitchDisplayNameService twitchDisplayNameService;

    public TmiClientInitializer(final TmiBot tmiBot, final String username, final Channel channel,
                                final Deduplicator deduplicator, final Tracer tracer,
                                final TwitchDisplayNameService twitchDisplayNameService) {
        this.tmiBot = tmiBot;
        this.username = username;
        this.channel = channel;
        this.deduplicator = deduplicator;
        this.tracer = tracer;
        this.twitchDisplayNameService = twitchDisplayNameService;
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {
        final SslContext sslContext = SslContextBuilder.forClient()
                .build();
        final ChannelPipeline channelPipeline = ch.pipeline();
        /* Set up SSLEngine */
        final SslHandler sslHandler = sslContext.newHandler(ch.alloc(), "irc.chat.twitch.tv", 6697);
        final SSLEngine sslEngine = sslHandler.engine();
        final SSLParameters sslParameters = sslEngine.getSSLParameters();
        sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
        sslEngine.setSSLParameters(sslParameters);
        channelPipeline.addLast(sslHandler);

        // Decoder
        channelPipeline.addLast(new TmiFrameDecoder());
        channelPipeline.addLast(new StringDecoder());
        channelPipeline.addLast(new TmiProtocolDecoder());
        channelPipeline.addLast(new TmiClientHandler(tmiBot, username, channel, deduplicator, tracer, twitchDisplayNameService));

        // Encoder
        channelPipeline.addLast(new StringEncoder());
    }
}
