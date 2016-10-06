package tv.twitchbot.common.modules;

import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.messages.Message;
import tv.twitchbot.common.dto.messages.Request;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;
import tv.twitchbot.common.dto.messages.requests.SendMessageRequest;
import tv.twitchbot.common.dto.messages.responses.ModuleShutdownResponse;
import tv.twitchbot.common.dto.messages.responses.SendMessageResponse;

/**
 * Created by naomi on 10/6/16.
 */
public abstract class EasyThreadedModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends ThreadedModule<T, U, V> {

    @Override
    protected void processMessage(Message message) {
        if(message instanceof Event)
            processEvent((Event) message);
        else if(message instanceof Request)
            processRequest((Request) message);
        else if(message instanceof Response)
            processResponse((Response) message);
        else
            throw new IllegalStateException("Unknown message type " + message.getClass().getCanonicalName());
    }

    protected void processEvent(Event event) {
        if(event instanceof ChatMessageEvent)
            processChatMessageEvent((ChatMessageEvent) event);
        else
            throw new IllegalStateException("Unknown event type " + event.getClass().getCanonicalName());
    }

    protected abstract void processChatMessageEvent(ChatMessageEvent chatMessageEvent);

    protected void processRequest(Request request) {
        if(request instanceof SendMessageRequest) /* ModuleShutdownRequest is handled elsewhere */
            processSendMessageRequest((SendMessageRequest) request);
        else
            throw new IllegalStateException("Unknown request type " + request.getClass().getCanonicalName());
    }

    protected abstract void processSendMessageRequest(SendMessageRequest sendMessageRequest);

    protected void processResponse(Response response) {
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
