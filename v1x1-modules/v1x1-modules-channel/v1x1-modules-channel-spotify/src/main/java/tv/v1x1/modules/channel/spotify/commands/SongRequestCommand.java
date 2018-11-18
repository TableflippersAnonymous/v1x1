package tv.v1x1.modules.channel.spotify.commands;

import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.util.commands.annotations.Command;
import tv.v1x1.common.util.commands.annotations.CommandSet;

import java.util.List;

@Singleton
@CommandSet
public class SongRequestCommand {
    @Command(
            value = "songrequest",
            usage = "<spotify link>",
            description = "Request a song",
            permissions = "spotify.enqueue",
            help = "This allows you to request songs via Spotify.",
            maxArgs = 1
    )
    public void handleSongRequest(final ChatMessage chatMessage, final String command, final List<String> args,
                                  final SongCommand songCommand) {
        // Alias to !song request
        songCommand.handleSongRequest(chatMessage, command, args);
    }
}
