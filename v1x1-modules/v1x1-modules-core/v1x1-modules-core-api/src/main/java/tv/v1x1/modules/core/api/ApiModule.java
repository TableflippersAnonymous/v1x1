package tv.v1x1.modules.core.api;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.messages.PubSubMessage;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.modules.ServiceModule;
import tv.v1x1.common.services.pubsub.TopicManager;
import tv.v1x1.common.services.pubsub.TopicName;
import tv.v1x1.modules.core.api.api.pubsub.events.ChatMessagePubSub;
import tv.v1x1.modules.core.api.api.pubsub.events.ConfigChangePubSub;
import tv.v1x1.modules.core.api.api.rest.Channel;
import tv.v1x1.modules.core.api.api.rest.User;
import tv.v1x1.modules.core.api.config.ApiGlobalConfiguration;
import tv.v1x1.modules.core.api.config.ApiUserConfiguration;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/24/2016.
 */
public class ApiModule extends ServiceModule<ApiGlobalConfiguration, ApiUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper mapper = new ObjectMapper(new JsonFactory());

    private Thread applicationThread;
    private ApiApplication application;

    @Override
    public String getName() {
        return "api";
    }

    @Override
    protected void preinit() {
        super.preinit();
        LOG.info("Creating application.");
        application = new ApiApplication(this);
        applicationThread = new Thread(() -> {
            try {
                application.run("server", getConfigFile());
            } catch (Exception e) {
                LOG.error("Caught exception in dropwizard ApiApplication thread", e);
            }
        });
        applicationThread.start();
        LOG.info("Waiting on application.");
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOG.info("Woken by application.");
        final LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
        context.reset();
        final ContextInitializer initializer = new ContextInitializer(context);
        try {
            initializer.autoConfig();
        } catch (final JoranException e) {
            LOG.warn("Caught exception during startup: ", e);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        LOG.info("Acquiring module lock.");
        synchronized (this) {
            LOG.info("Waking application.");
            notifyAll();
        }
    }

    @Override
    protected void shutdown() {
        applicationThread.interrupt();
        super.shutdown();
    }

    public static void main(final String[] args) throws Exception {
        new ApiModule().entryPoint(args);
    }

    @Override
    protected void processChatMessageEvent(final ChatMessageEvent chatMessageEvent) {
        final TopicManager topicManager = getInjector().getInstance(TopicManager.class);
        final TopicName topic = new TopicName(
                chatMessageEvent.getChatMessage().getChannel().getChannelGroup().getTenant().getId().getValue(),
                toDto(),
                "chat");
        final ChatMessagePubSub chatMessagePubSub = new ChatMessagePubSub(
                chatMessageEvent.getMessageId().getValue(),
                chatMessageEvent.getFrom().getName(),
                new Channel(chatMessageEvent.getChatMessage().getChannel()),
                new User(
                        chatMessageEvent.getChatMessage().getSender().getGlobalUser().getId().getValue(),
                        chatMessageEvent.getChatMessage().getSender().getPlatform(),
                        chatMessageEvent.getChatMessage().getSender().getId(),
                        chatMessageEvent.getChatMessage().getSender().getDisplayName()
                ),
                chatMessageEvent.getChatMessage().getText(),
                chatMessageEvent.getChatMessage().getPermissions().stream().map(Permission::getNode).collect(Collectors.toList())
        );
        try {
            final PubSubMessage pubSubMessage = new PubSubMessage(toDto(), topic.getFullTopicName(), mapper.writeValueAsString(chatMessagePubSub));
            topicManager.publish(topic.getFullTopicName(), pubSubMessage);
        } catch (final JsonProcessingException e) {
            LOG.error("Caught JsonProcessingException while serializing a ChatMessagePubSub", e);
        }
    }

    public void handleConfigChangeEvent(final Tenant tenant, final Module module) {
        final TopicManager topicManager = getInjector().getInstance(TopicManager.class);
        final TopicName topic = new TopicName(tenant.getId().getValue(), toDto(), "config");
        final ConfigChangePubSub configChangePubSub = new ConfigChangePubSub(module.getName());
        try {
            final PubSubMessage pubSubMessage = new PubSubMessage(toDto(), topic.getFullTopicName(), mapper.writeValueAsString(configChangePubSub));
            topicManager.publish(topic.getFullTopicName(), pubSubMessage);
        } catch (final JsonProcessingException e) {
            LOG.error("Caught JsonProcessingException while serializing a ChatMessagePubSub", e);
        }
    }
}
