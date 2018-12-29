package tv.v1x1.common.modules;

import tv.v1x1.common.dto.messages.events.ChannelConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.ChannelGroupConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.ChatJoinEvent;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.ChatPartEvent;
import tv.v1x1.common.dto.messages.events.ConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.DiscordVoiceStateEvent;
import tv.v1x1.common.dto.messages.events.GlobalConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.PrivateMessageEvent;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.TwitchBotChannelStateEvent;
import tv.v1x1.common.dto.messages.events.TwitchBotConnectedEvent;
import tv.v1x1.common.dto.messages.events.TwitchBotGlobalStateEvent;
import tv.v1x1.common.dto.messages.events.TwitchChannelEvent;
import tv.v1x1.common.dto.messages.events.TwitchChannelUsersEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatJoinEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatPartEvent;
import tv.v1x1.common.dto.messages.events.TwitchHostEvent;
import tv.v1x1.common.dto.messages.events.TwitchPingEvent;
import tv.v1x1.common.dto.messages.events.TwitchPrivateMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchRawMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchReconnectEvent;
import tv.v1x1.common.dto.messages.events.TwitchRoomStateEvent;
import tv.v1x1.common.dto.messages.events.TwitchTimeoutEvent;
import tv.v1x1.common.dto.messages.events.TwitchUserEvent;
import tv.v1x1.common.dto.messages.events.TwitchUserModChangeEvent;

/**
 * Created by naomi on 10/6/16.
 */
public abstract class DefaultModule<T extends GlobalConfiguration, U extends UserConfiguration> extends EasyThreadedModule<T, U> {
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
    protected void processTwitchPrivateMessageEvent(final TwitchPrivateMessageEvent event) {
        /* No action */
    }

    @Override
    protected void processPrivateMessageEvent(final PrivateMessageEvent event) {
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

    @Override
    protected void processConfigChangeEvent(final ConfigChangeEvent event) {
        /* No action */
    }

    @Override
    protected void processGlobalConfigChangeEvent(final GlobalConfigChangeEvent event) {
        /* No action */
    }

    @Override
    protected void processTenantConfigChangeEvent(final TenantConfigChangeEvent event) {
        /* No action */
    }

    @Override
    protected void processChannelGroupConfigChangeEvent(final ChannelGroupConfigChangeEvent event) {
        /* No action */
    }

    @Override
    protected void processChannelConfigChangeEvent(final ChannelConfigChangeEvent event) {
        /* No action */
    }

    @Override
    protected void processDiscordVoiceStateEvent(final DiscordVoiceStateEvent event) {
        /* No action */
    }
}
