package tv.twitchbot.common.modules;

import tv.twitchbot.common.dto.messages.events.*;
import tv.twitchbot.common.dto.messages.requests.SendMessageRequest;
import tv.twitchbot.common.dto.messages.responses.ModuleShutdownResponse;
import tv.twitchbot.common.dto.messages.responses.SendMessageResponse;

/**
 * Created by naomi on 10/6/16.
 */
public abstract class DefaultModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends EasyThreadedModule<T, U, V> {
    @Override
    protected void processChatMessageEvent(ChatMessageEvent chatMessageEvent) {
        /* No action */
    }

    @Override
    protected void processTwitchChatPartEvent(TwitchChatPartEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchChatJoinEvent(TwitchChatJoinEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchChatMessageEvent(TwitchChatMessageEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchUserModChangeEvent(TwitchUserModChangeEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchUserEvent(TwitchUserEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchTimeoutEvent(TwitchTimeoutEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchRoomStateEvent(TwitchRoomStateEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchReconnectEvent(TwitchReconnectEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchRawMessageEvent(TwitchRawMessageEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchPingEvent(TwitchPingEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchHostEvent(TwitchHostEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchChannelUsersEvent(TwitchChannelUsersEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchChannelEvent(TwitchChannelEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchBotGlobalStateEvent(TwitchBotGlobalStateEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchBotConnectedEvent(TwitchBotConnectedEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchBotChannelStateEvent(TwitchBotChannelStateEvent event) {
        /* No action */
    }

    @Override
    protected void processChatPartEvent(ChatPartEvent event) {
        /* No action */
    }

    @Override
    protected void processChatJoinEvent(ChatJoinEvent event) {
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
