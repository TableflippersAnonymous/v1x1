package tv.v1x1.modules.core.api.resources.ws;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.messages.PubSubMessage;
import tv.v1x1.common.services.pubsub.TopicManager;
import tv.v1x1.common.services.pubsub.TopicName;
import tv.v1x1.modules.core.api.api.pubsub.protocol.AuthRequestWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.AuthResponseWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.ErrorWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.HelloWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.ListenRequestWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.ListenResponseWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.PublishRequestWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.PublishResponseWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.TopicMessageWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.UnlistenRequestWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.UnlistenResponseWebSocketFrame;
import tv.v1x1.modules.core.api.api.pubsub.protocol.WebSocketFrame;
import tv.v1x1.modules.core.api.auth.AuthorizationContext;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cobi on 4/30/2017.
 */

@ServerEndpoint("/pubsub")
public class PubsubResource {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper mapper = new ObjectMapper(new JsonFactory());

    private static TopicManager topicManager;
    private static Authorizer authorizer;

    private final Map<String, Integer> topics = new HashMap<>();
    private AuthorizationContext authContext = null;

    public static void initialize(final TopicManager topicManager, final Authorizer authorizer) {
        PubsubResource.topicManager = topicManager;
        PubsubResource.authorizer = authorizer;
    }

    @OnOpen
    public void onOpen(final Session session) {
        session.setMaxIdleTimeout(1800000);
        send(session, new HelloWebSocketFrame(UUID.randomUUID()));
    }

    @OnClose
    public void onClose(final Session session) {
        for(final String topic : new HashSet<>(topics.keySet())) {
            unsubscribe(topic);
        }
    }

    @OnMessage
    public void onMessage(final String message, final Session session) {
        try {
            LOG.debug("WS Read: {}", message);
            final WebSocketFrame frame = mapper.readValue(message, WebSocketFrame.class);
            LOG.debug("FRAME Read: {}", frame);
            switch(frame.getType()) {
                case HELLO:
                case ERROR:
                case AUTH_RESPONSE:
                case LISTEN_RESPONSE:
                case PUBLISH_RESPONSE:
                case UNLISTEN_RESPONSE:
                case TOPIC_MESSAGE:
                    // Should never receive these.
                    send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "BAD_DIRECTION", "Response frame received by server."));
                    break;

                case AUTH_REQUEST:
                    processAuthRequest(session, (AuthRequestWebSocketFrame) frame);
                    break;
                case LISTEN_REQUEST:
                    processListenRequest(session, (ListenRequestWebSocketFrame) frame);
                    break;
                case UNLISTEN_REQUEST:
                    processUnlistenRequest(session, (UnlistenRequestWebSocketFrame) frame);
                    break;
                case PUBLISH_REQUEST:
                    processPublishRequest(session, (PublishRequestWebSocketFrame) frame);
                    break;

                default:
                    send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "BAD_TYPE", "Unknown type"));
            }
        } catch (final IOException e) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), new UUID(0, 0), e.getClass().getCanonicalName(), e.getMessage()));
        }
    }

    private void processPublishRequest(final Session session, final PublishRequestWebSocketFrame frame) {
        if(authContext == null) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "AUTH_REQUIRED", "You must authenticate before PUBLISHing."));
            return;
        }

        final TopicName topicName = TopicName.parse(frame.getTopic());
        if(topicName == null) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "BAD_TOPIC", "Invalid topic."));
            return;
        }

        final AuthorizationContext tenantAuthContext = authorizer.tenantContext(authContext, topicName.getTenantId());
        final String permission = "api.pubsub.write." + topicName.getModule().getName() + "." + topicName.getName();
        if(!tenantAuthContext.hasPermission(permission)) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "INSUFFICIENT_PRIVILEGES", "You require permission " + permission));
            return;
        }

        final PubSubMessage pubSubMessage = new PubSubMessage(
                new Module("api|" + authContext.getGlobalUser().getId().getValue()),
                topicName.getFullTopicName(),
                frame.getPayload());

        topicManager.publish(topicName.getFullTopicName(), pubSubMessage);
        send(session, new PublishResponseWebSocketFrame(UUID.randomUUID(), frame.getId(), pubSubMessage.getFrom().getName(),
                pubSubMessage.getMessageId().getValue(), pubSubMessage.getTimestamp(), pubSubMessage.getTopic(),
                pubSubMessage.getJson()));
    }

    private void processUnlistenRequest(final Session session, final UnlistenRequestWebSocketFrame frame) {
        final TopicName topicName = TopicName.parse(frame.getTopic());
        if(topicName == null) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "BAD_TOPIC", "Invalid topic."));
            return;
        }

        if(!unsubscribe(topicName.getFullTopicName())) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "NOT_LISTENING", "You are not listening to " + topicName.getFullTopicName()));
            return;
        }

        send(session, new UnlistenResponseWebSocketFrame(UUID.randomUUID(), frame.getId(), topicName.getFullTopicName()));
    }

    private void processListenRequest(final Session session, final ListenRequestWebSocketFrame frame) {
        if(authContext == null) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "AUTH_REQUIRED", "You must authenticate before LISTENing."));
            return;
        }

        final TopicName topicName = TopicName.parse(frame.getTopic());
        if(topicName == null) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "BAD_TOPIC", "Invalid topic."));
            return;
        }

        final AuthorizationContext tenantAuthContext = authorizer.tenantContext(authContext, topicName.getTenantId());
        final String permission = "api.pubsub.read." + topicName.getModule().getName() + "." + topicName.getName();
        if(!tenantAuthContext.hasPermission(permission)) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "INSUFFICIENT_PRIVILEGES", "You require permission " + permission));
            return;
        }

        if(!subscribe(topicName.getFullTopicName(), session)) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), "ALREADY_LISTENING", "You are already listening to " + topicName.getFullTopicName()));
            return;
        }

        send(session, new ListenResponseWebSocketFrame(UUID.randomUUID(), frame.getId(), topicName.getFullTopicName()));
    }

    private void processAuthRequest(final Session session, final AuthRequestWebSocketFrame frame) {
        try {
            authContext = authorizer.forAuthorization(frame.getAuthorization());
            send(session, new AuthResponseWebSocketFrame(UUID.randomUUID(), frame.getId(), authContext.getGlobalUser().getId().getValue()));
        } catch(final Exception e) {
            send(session, new ErrorWebSocketFrame(UUID.randomUUID(), frame.getId(), e.getClass().getCanonicalName(), e.getMessage()));
        }
    }

    @OnError
    public void onError(final Session session, final Throwable throwable) {
        LOG.warn("Got error onError", throwable);
    }

    private synchronized boolean subscribe(final String topic, final Session session) {
        if(topics.containsKey(topic))
            return false;
        final int listenerId = topicManager.addListener(topic,
                pubSubMessage -> send(session, new TopicMessageWebSocketFrame(UUID.randomUUID(), pubSubMessage.getFrom().getName(),
                pubSubMessage.getMessageId().getValue(), pubSubMessage.getTimestamp(), pubSubMessage.getTopic(),
                pubSubMessage.getJson())));
        topics.put(topic, listenerId);
        return true;
    }

    private synchronized boolean unsubscribe(final String topic) {
        if(!topics.containsKey(topic))
            return false;
        topicManager.removeListener(topic, topics.remove(topic));
        return true;
    }

    private static void send(final Session session, final WebSocketFrame webSocketFrame) {
        try {
            LOG.debug("FRAME Send: {}", webSocketFrame);
            final String frameJson = mapper.writeValueAsString(webSocketFrame);
            LOG.debug("WS Send: {}", frameJson);
            session.getAsyncRemote().sendText(frameJson);
        } catch (final JsonProcessingException e) {
            LOG.error("Error while handling send", e);
            try {
                session.close();
            } catch (final IOException e1) {
                LOG.error("Error while closing WS", e1);
            }
        }
    }
}
