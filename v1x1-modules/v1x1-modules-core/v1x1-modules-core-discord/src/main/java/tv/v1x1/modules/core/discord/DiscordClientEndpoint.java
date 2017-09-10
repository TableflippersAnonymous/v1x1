package tv.v1x1.modules.core.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.CountDownLatch;

/**
 * Created by naomi on 4/10/2018.
 */
@ClientEndpoint
public class DiscordClientEndpoint extends Endpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DiscordModule discordModule;
    private final String shardKey;
    private final int shardNumber;
    private final int totalShards;
    private final CountDownLatch countDownLatch;

    public DiscordClientEndpoint(final DiscordModule discordModule, final String shardKey, final int shardNumber,
                                 final int totalShards, final CountDownLatch countDownLatch) {
        this.discordModule = discordModule;
        this.shardKey = shardKey;
        this.shardNumber = shardNumber;
        this.totalShards = totalShards;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onOpen(final Session session, final EndpointConfig endpointConfig) {
        LOG.info("[{}] Session opened.", shardKey);
        session.addMessageHandler(String.class, new DiscordClientHandler(discordModule, shardKey, shardNumber, totalShards, session, countDownLatch));
    }

    @Override
    public void onClose(final Session session, final CloseReason closeReason) {
        LOG.info("[{}] Session closed.", shardKey);
        countDownLatch.countDown();
    }

    @Override
    public void onError(final Session session, final Throwable thr) {
        LOG.info("[{}] Session error.", shardKey, thr);
        countDownLatch.countDown();
    }
}
