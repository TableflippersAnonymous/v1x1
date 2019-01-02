package tv.v1x1.modules.channel.spotify;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.modules.channel.spotify.playlist.PlaylistEntry;
import tv.v1x1.modules.channel.spotify.playlist.PlaylistManager;

public class SpotifyListener implements EventListener {
    private final SpotifyModule module;
    private final CommandDelegator delegator;

    public SpotifyListener(final SpotifyModule module, final CommandDelegator delegator) {
        this.module = module;
        this.delegator = delegator;
    }

    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        if(module.isEnabled(ev.getChatMessage().getChannel()))
            delegator.handleChatMessage(ev);
    }

    @EventHandler
    public void onScheduleNotify(final SchedulerNotifyEvent ev) {
        if(!ev.getModule().equals(module.toDto()))
            return;
        final Channel channel = Channel.VAL_CODEC.decode(ev.getPayload());
        if(module.isEnabled(channel)) {
            final PlaylistEntry playlistEntry = module.getChildInjector().getInstance(PlaylistManager.class)
                    .getPlaylistFor(channel).handleTimer();
            if(playlistEntry != null)
                Chat.i18nMessage(module, channel, "song.nowplaying",
                        "title", playlistEntry.getTitle(),
                        "artist", playlistEntry.getArtist());
        }
    }
}
