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
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
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
 * Created by naomi on 10/24/2016.
 */
@Permissions(version = 1, value = {
        @RegisteredPermission(
                node = "api.permissions.read",
                displayName = "Web Read Permissions",
                description = "This allows you to read permissions and groups on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT, DefaultGroup.WEB_READ}
        ),
        @RegisteredPermission(
                node = "api.permissions.write",
                displayName = "Web Write Permissions",
                description = "This allows you to change permissions and groups on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL}
        ),
        @RegisteredPermission(
                node = "api.tenants.config.read",
                displayName = "Web Read Config",
                description = "This allows you to read module configurations on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT, DefaultGroup.WEB_READ}
        ),
        @RegisteredPermission(
                node = "api.tenants.config.write",
                displayName = "Web Write Config",
                description = "This allows you to change module configurations on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT}
        ),
        @RegisteredPermission(
                node = "api.tenants.read",
                displayName = "Web Read Basic",
                description = "This allows you to read basic information about a tenant on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT, DefaultGroup.WEB_READ}
        ),
        @RegisteredPermission(
                node = "api.tenants.write",
                displayName = "Web Write Basic",
                description = "This allows you to set basic attributes about a tenant on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT}
        ),
        @RegisteredPermission(
                node = "api.tenants.link",
                displayName = "Web Link Tenants",
                description = "This allows you to add channels to the tenant on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT}
        ),
        @RegisteredPermission(
                node = "api.tenants.unlink",
                displayName = "Web Unlink Tenants",
                description = "This allows you to remove channels from the tenant on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT}
        ),
        @RegisteredPermission(
                node = "api.tenants.message",
                displayName = "Web Send Message",
                description = "This allows you to send messages as the bot to channels on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT}
        ),
        @RegisteredPermission(
                node = "api.pubsub.read.api.chat",
                displayName = "Web Read Chat",
                description = "This allows you to read messages that the bot can see on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT, DefaultGroup.WEB_READ}
        ),
        @RegisteredPermission(
                node = "api.pubsub.read.api.config",
                displayName = "Web Read Chat",
                description = "This allows you to see instantaneous config updates on the website.",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.WEB_ALL, DefaultGroup.WEB_EDIT, DefaultGroup.WEB_READ}
        )
})
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
