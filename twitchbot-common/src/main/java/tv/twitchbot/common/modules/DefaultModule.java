package tv.twitchbot.common.modules;

import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;
import tv.twitchbot.common.dto.messages.requests.SendMessageRequest;
import tv.twitchbot.common.dto.messages.responses.ModuleShutdownResponse;
import tv.twitchbot.common.dto.messages.responses.SendMessageResponse;

/**
 * Created by cobi on 10/6/16.
 */
public abstract class DefaultModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends EasyThreadedModule<T, U, V> {
    @Override
    protected void processChatMessageEvent(ChatMessageEvent chatMessageEvent) {
        /* No action */
    }

    @Override
    protected void processSendMessageRequest(SendMessageRequest sendMessageRequest) {
        /* No action */
    }

    @Override
    protected void processModuleShutdownResponse(ModuleShutdownResponse moduleShutdownResponse) {
        /* No action */
    }

    @Override
    protected void processSendMessageResponse(SendMessageResponse sendMessageResponse) {
        /* No action */
    }

    @Override
    protected void initialize() {
        /* No action */
    }
}
