package tv.v1x1.modules.core.discord;

import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

/**
 * Created by naomi on 4/10/2018.
 */
public class DiscordBotShard implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DiscordModule discordModule;
    private final String shardKey;
    private final int shardNumber;
    private final int totalShards;
    private final URI uri;

    @Nullable
    private volatile Session session;
    private volatile Thread thread;
    private volatile boolean running = true;

    public DiscordBotShard(final DiscordModule discordModule, final String shardKey, final int shardNumber, final int totalShards, final URI uri) {
        this.discordModule = discordModule;
        this.shardKey = shardKey;
        this.shardNumber = shardNumber;
        this.totalShards = totalShards;
        this.uri = uri;
    }

    @Override
    public void run() {
        LOG.info("[{}] Thread running.", shardKey);
        thread = Thread.currentThread();
        while(running) {
            try {
                connectToServerAndWait();
            } catch (final Exception e) {
                LOG.error("Got exception", e);
            }
            try {
                Thread.sleep(2000);
            } catch (final InterruptedException e) {
                LOG.error("Got exception sleeping", e);
            }
        }
    }

    private void connectToServerAndWait() {
        LOG.info("[{}] Beginning connect loop ...", shardKey);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        LOG.debug("[{}] Got latch", shardKey);
        final ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create().build();
        LOG.debug("[{}] Got client endpoint config", shardKey);
        final ClientManager client = ClientManager.createClient();
        LOG.debug("[{}] Got client", shardKey);
        try {
            LOG.info("[{}] Connecting to websocket ...", shardKey);
            session = client.connectToServer(new DiscordClientEndpoint(discordModule, shardKey,
                    shardNumber, totalShards, countDownLatch), clientEndpointConfig, uri);
            LOG.info("[{}] Waiting ...", shardKey);
            countDownLatch.await();
        } catch (final InterruptedException | DeploymentException | IOException e) {
            LOG.error("Got exception", e);
        } finally {
            disconnect();
        }
    }

    public void shutdown() {
        running = false;
        disconnect();
        if(thread != null)
            thread.interrupt();
    }

    private void disconnect() {
        final Session session = this.session;
        if(session != null)
            try {
                session.close();
                this.session = null;
            } catch (final IOException e) {
                LOG.warn("Got exception", e);
            }
    }
}
