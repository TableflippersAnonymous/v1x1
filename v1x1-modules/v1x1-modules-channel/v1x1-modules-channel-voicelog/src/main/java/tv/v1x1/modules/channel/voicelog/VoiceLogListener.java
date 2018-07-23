package tv.v1x1.modules.channel.voicelog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.ChannelRef;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.DiscordVoiceStateEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.services.state.NoSuchTargetException;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

/**
 * @author Josh
 */
public class VoiceLogListener implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final VoiceLog module;

    VoiceLogListener(final VoiceLog module) {
        this.module = module;
    }

    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        if(module.isEnabled(ev.getChatMessage().getChannel()))
            module.delegator.handleChatMessage(ev);
    }

    @EventHandler
    public void onDiscordVoiceState(final DiscordVoiceStateEvent ev) {
        LOG.trace("Permissions: " + ev.getPermissions().stream().map(Permission::getNode).collect(Collectors.joining(", ")));
        final DAOTenant daoTenant = module.getDaoManager().getDaoTenant();
        final String userId = ev.getNewVoiceState().getUserId();
        final String guildId = ev.getNewVoiceState().getGuildId();
        final Tenant tenant = daoTenant.getAsCore(Platform.DISCORD, guildId);
        final ChannelGroup guild = tenant.getChannelGroup(Platform.DISCORD, guildId).orElse(null);
        if(guild == null) {
            LOG.warn("Event on unknown Discord Guild, I guess");
            return;
        }
        final VoiceLogUserConfiguration config = module.getConfiguration(guild);
        if(!config.isEnabled())
            return;
        if(ev.getPermissions().contains(new Permission("voicelog.hidden")))
            return;
        final ChannelRef targetRef = config.getChannel();
        final Channel target = guild.getChannel(targetRef.getId()).orElse(null);
        if(target == null) {
            // this is a valid condition for final
            LOG.warn("VoiceLog channel set, but not found. Disabling...");
            config.setChannel(null);
            config.setEnabled(false);
            return;
        }
        String realDisplayName = null;
        String realUsername = null;
        String oldVoiceChannelId = null;
        String newVoiceChannelId = null;
        String oldVoiceChannelName = null;
        String newVoiceChannelName = null;
        if(ev.getOldVoiceState() != null)
            oldVoiceChannelId = ev.getOldVoiceState().getChannelId();
        if(ev.getNewVoiceState() != null)
            newVoiceChannelId = ev.getNewVoiceState().getChannelId();
        try {
            realDisplayName = module.getDisplayNameService().getDisplayNameFromId(target, userId);
            realUsername = module.getDisplayNameService().getUsernameFromId(target, userId);
            if(newVoiceChannelId != null)
                newVoiceChannelName = module.getDisplayNameService().getChannelDisplayNameFromId(Platform.DISCORD, newVoiceChannelId);
            if(oldVoiceChannelId != null)
                oldVoiceChannelName = module.getDisplayNameService().getChannelDisplayNameFromId(Platform.DISCORD, oldVoiceChannelId);
        } catch(NoSuchTargetException e) {
            LOG.warn("Got a voice state for a user/channel I don't know. User: " + userId + ". OldChan: " + oldVoiceChannelId + ". NewChan: " + newVoiceChannelId);
        }
        sendLogMessage(target, realUsername, realDisplayName, oldVoiceChannelName, newVoiceChannelName);

    }

    private void sendLogMessage(final Channel channel, final String userName, final String displayName, final String oldChannel, final String newChannel) {
        if(oldChannel != null && newChannel != null) {
            // Both channels = move
            Chat.i18nMessage(module, channel, "logline.move",
                    "displayname", displayName,
                    "username", userName,
                    "oldchannel", oldChannel,
                    "newchannel", newChannel);
        } else if(oldChannel == null && newChannel != null) {
            // No old channel and a new channel = join
            Chat.i18nMessage(module, channel, "logline.join",
                    "displayname", displayName,
                    "username", userName,
                    "channel", newChannel);
        } else if(oldChannel != null) {
            // Old channel and no new channel = part
            Chat.i18nMessage(module, channel, "logline.part",
                    "displayname", displayName,
                    "username", userName,
                    "mention", displayName,
                    "channel", oldChannel);
        } else {
            LOG.error("Awkward. Both oldChannel and newChannel are null.");
        }
    }
}
