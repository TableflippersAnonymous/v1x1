package tv.v1x1.common.modules;

import tv.v1x1.common.dto.messages.events.*;
import tv.v1x1.common.dto.messages.requests.SendMessageRequest;
import tv.v1x1.common.dto.messages.responses.ModuleShutdownResponse;
import tv.v1x1.common.dto.messages.responses.SendMessageResponse;

/**
 * Created by cobi on 10/6/16.
 */
public abstract class DefaultModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends EasyThreadedModule<T, U, V> {
    @Override
    protected void processChatMessageEvent(final ChatMessageEvent chatMessageEvent) {
        /* No action */
    }

    @Override
    protected void processTwitchChatPartEvent(final TwitchChatPartEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchChatJoinEvent(final TwitchChatJoinEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchChatMessageEvent(final TwitchChatMessageEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchUserModChangeEvent(final TwitchUserModChangeEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchUserEvent(final TwitchUserEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchTimeoutEvent(final TwitchTimeoutEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchRoomStateEvent(final TwitchRoomStateEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchReconnectEvent(final TwitchReconnectEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchRawMessageEvent(final TwitchRawMessageEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchPingEvent(final TwitchPingEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchHostEvent(final TwitchHostEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchChannelUsersEvent(final TwitchChannelUsersEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchChannelEvent(final TwitchChannelEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchBotGlobalStateEvent(final TwitchBotGlobalStateEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchBotConnectedEvent(final TwitchBotConnectedEvent event) {
        /* No action */
    }

    @Override
    protected void processTwitchBotChannelStateEvent(final TwitchBotChannelStateEvent event) {
        /* No action */
    }

    @Override
    protected void processChatPartEvent(final ChatPartEvent event) {
        /* No action */
    }

    @Override
    protected void processChatJoinEvent(final ChatJoinEvent event) {
        /* No action */
    }


    @Override
    protected void processSendMessageRequest(final SendMessageRequest sendMessageRequest) {
        /* No action */
    }

    @Override
    protected void processModuleShutdownResponse(final ModuleShutdownResponse moduleShutdownResponse) {
        /* No action */
    }

    @Override
    protected void processSendMessageResponse(final SendMessageResponse sendMessageResponse) {
        /* No action */
    }

    @Override
    protected void initialize() {
        /* No action */
    }

    @Override
    protected void processSchedulerNotifyEvent(final SchedulerNotifyEvent event) {
        /* No action */
    }
}
