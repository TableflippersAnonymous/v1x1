package tv.v1x1.modules.channel.spotify.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.annotations.Command;
import tv.v1x1.common.util.commands.annotations.CommandSet;
import tv.v1x1.modules.channel.spotify.SpotifyModule;
import tv.v1x1.modules.channel.spotify.playlist.Playlist;
import tv.v1x1.modules.channel.spotify.playlist.PlaylistEntry;
import tv.v1x1.modules.channel.spotify.playlist.PlaylistManager;

import java.util.List;

@Singleton
@CommandSet("song")
public class SongCommand {
    private final SpotifyModule module;

    @Inject
    public SongCommand(final SpotifyModule module) {
        this.module = module;
    }

    @Command(
            value = "request",
            usage = "<spotify link>",
            description = "Request a song",
            permissions = "spotify.enqueue",
            help = "This allows you to request songs via Spotify.",
            minArgs = 1,
            maxArgs = 1
    )
    public void handleSongRequest(final ChatMessage chatMessage, final List<String> args,
                                  final PlaylistManager playlistManager) {
        final Playlist playlist = playlistManager.getPlaylistFor(chatMessage.getChannel());
        final PlaylistEntry entry = playlist.add(chatMessage.getSender().getGlobalUser(), args.get(0));
        Chat.i18nMessage(module, chatMessage.getChannel(), "song.request.added",
                "commander", chatMessage.getSender().getMention(),
                "title", entry.getTitle(),
                "artist", entry.getArtist());
    }

    @Command(
            value = "start",
            description = "Start playback",
            permissions = "spotify.start",
            help = "Enable spotify",
            minArgs = 0,
            maxArgs = 0
    )
    public void handleSongStart(final ChatMessage chatMessage, final PlaylistManager playlistManager) {
        final Playlist playlist = playlistManager.getPlaylistFor(chatMessage.getChannel());
        playlist.start();
    }

    @Command(
            value = "stop",
            description = "Stop playback",
            permissions = "spotify.stop",
            help = "Disable spotify",
            minArgs = 0,
            maxArgs = 0
    )
    public void handleSongStop(final ChatMessage chatMessage, final PlaylistManager playlistManager) {
        final Playlist playlist = playlistManager.getPlaylistFor(chatMessage.getChannel());
        playlist.stop();
    }
}
