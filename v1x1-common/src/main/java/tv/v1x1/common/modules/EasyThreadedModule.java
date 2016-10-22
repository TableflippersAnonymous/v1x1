package tv.v1x1.common.modules;

import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.dto.messages.events.*;
import tv.v1x1.common.dto.messages.requests.SendMessageRequest;
import tv.v1x1.common.dto.messages.responses.ModuleShutdownResponse;
import tv.v1x1.common.dto.messages.responses.SendMessageResponse;

/**
 * Created by naomi on 10/6/16.
 */
public abstract class EasyThreadedModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends ThreadedModule<T, U, V> {

    @Override
    protected void processMessage(final Message message) {
        if(message instanceof Event)
            processEvent((Event) message);
        else if(message instanceof Request)
            processRequest((Request) message);
        else if(message instanceof Response)
            processResponse((Response) message);
        else
            throw new IllegalStateException("Unknown message type " + message.getClass().getCanonicalName());
    }

    protected void processEvent(final Event event) {
        if(event instanceof ChatMessageEvent) {
            processChatMessageEvent((ChatMessageEvent) event);
            if(event instanceof TwitchChatMessageEvent)
                processTwitchChatMessageEvent((TwitchChatMessageEvent) event);
        } else if(event instanceof ChatJoinEvent) {
            processChatJoinEvent((ChatJoinEvent) event);
            if(event instanceof TwitchChatJoinEvent)
                processTwitchChatJoinEvent((TwitchChatJoinEvent) event);
        } else if(event instanceof ChatPartEvent) {
            processChatPartEvent((ChatPartEvent) event);
            if(event instanceof TwitchChatPartEvent)
                processTwitchChatPartEvent((TwitchChatPartEvent) event);
        } else if(event instanceof TwitchBotChannelStateEvent)
            processTwitchBotChannelStateEvent((TwitchBotChannelStateEvent) event);
        else if(event instanceof TwitchBotConnectedEvent)
            processTwitchBotConnectedEvent((TwitchBotConnectedEvent) event);
        else if(event instanceof TwitchBotGlobalStateEvent)
            processTwitchBotGlobalStateEvent((TwitchBotGlobalStateEvent) event);
        else if(event instanceof TwitchChannelEvent)
            processTwitchChannelEvent((TwitchChannelEvent) event);
        else if(event instanceof TwitchChannelUsersEvent)
            processTwitchChannelUsersEvent((TwitchChannelUsersEvent) event);
        else if(event instanceof TwitchHostEvent)
            processTwitchHostEvent((TwitchHostEvent) event);
        else if(event instanceof TwitchPingEvent)
            processTwitchPingEvent((TwitchPingEvent) event);
        else if(event instanceof TwitchRawMessageEvent)
            processTwitchRawMessageEvent((TwitchRawMessageEvent) event);
        else if(event instanceof TwitchReconnectEvent)
            processTwitchReconnectEvent((TwitchReconnectEvent) event);
        else if(event instanceof TwitchRoomStateEvent)
            processTwitchRoomStateEvent((TwitchRoomStateEvent) event);
        else if(event instanceof TwitchTimeoutEvent)
            processTwitchTimeoutEvent((TwitchTimeoutEvent) event);
        else if(event instanceof TwitchUserEvent)
            processTwitchUserEvent((TwitchUserEvent) event);
        else if(event instanceof TwitchUserModChangeEvent)
            processTwitchUserModChangeEvent((TwitchUserModChangeEvent) event);
        else
            throw new IllegalStateException("Unknown event type " + event.getClass().getCanonicalName());
    }

    protected abstract void processTwitchChatPartEvent(TwitchChatPartEvent event);

    protected abstract void processTwitchChatJoinEvent(TwitchChatJoinEvent event);

    protected abstract void processTwitchChatMessageEvent(TwitchChatMessageEvent event);

    protected abstract void processTwitchUserModChangeEvent(TwitchUserModChangeEvent event);

    protected abstract void processTwitchUserEvent(TwitchUserEvent event);

    protected abstract void processTwitchTimeoutEvent(TwitchTimeoutEvent event);

    protected abstract void processTwitchRoomStateEvent(TwitchRoomStateEvent event);

    protected abstract void processTwitchReconnectEvent(TwitchReconnectEvent event);

    protected abstract void processTwitchRawMessageEvent(TwitchRawMessageEvent event);

    protected abstract void processTwitchPingEvent(TwitchPingEvent event);

    protected abstract void processTwitchHostEvent(TwitchHostEvent event);

    protected abstract void processTwitchChannelUsersEvent(TwitchChannelUsersEvent event);

    protected abstract void processTwitchChannelEvent(TwitchChannelEvent event);

    protected abstract void processTwitchBotGlobalStateEvent(TwitchBotGlobalStateEvent event);

    protected abstract void processTwitchBotConnectedEvent(TwitchBotConnectedEvent event);

    protected abstract void processTwitchBotChannelStateEvent(TwitchBotChannelStateEvent event);

    protected abstract void processChatPartEvent(ChatPartEvent event);

    protected abstract void processChatJoinEvent(ChatJoinEvent event);

    protected abstract void processChatMessageEvent(ChatMessageEvent chatMessageEvent);

    protected void processRequest(final Request request) {
        if(request instanceof SendMessageRequest) /* ModuleShutdownRequest is handled elsewhere */
            processSendMessageRequest((SendMessageRequest) request);
        else
            throw new IllegalStateException("Unknown request type " + request.getClass().getCanonicalName());
    }

    protected abstract void processSendMessageRequest(SendMessageRequest sendMessageRequest);

    protected void processResponse(final Response response) {
        if(response instanceof ModuleShutdownResponse)
            processModuleShutdownResponse((ModuleShutdownResponse) response);
        else if(response instanceof SendMessageResponse)
            processSendMessageResponse((SendMessageResponse) response);
        else
            throw new IllegalStateException("Unknown response type " + response.getClass().getCanonicalName());
    }

    protected abstract void processModuleShutdownResponse(ModuleShutdownResponse moduleShutdownResponse);

    protected abstract void processSendMessageResponse(SendMessageResponse sendMessageResponse);
}
