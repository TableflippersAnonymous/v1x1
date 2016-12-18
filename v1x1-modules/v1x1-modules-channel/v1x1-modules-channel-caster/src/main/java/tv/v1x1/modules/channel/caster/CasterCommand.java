package tv.v1x1.modules.channel.caster;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.services.twitch.dto.videos.TotalledVideoList;
import tv.v1x1.common.services.twitch.dto.videos.Video;
import tv.v1x1.common.util.commands.Command;

import java.util.List;

/**
 * @author Josh
 */
class CasterCommand extends Command {
    private Caster caster;

    public CasterCommand(final Caster caster) {
        this.caster = caster;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("caster", "follow", "shoutout");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("caster.use"));
    }

    @Override
    public String getDescription() {
        return "Give a fellow caster a shoutout about their last stream";
    }

    @Override
    public String getUsage() {
        return "<target>";
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final String targetId = args.get(0).toLowerCase();
        final TotalledVideoList videos = caster.getTwitchApi().getVideos().getVideos(targetId, 1, 0, true, true);
        final Channel channel = chatMessage.getChannel();
        if(videos.getVideos() == null) {
            Chat.i18nMessage(caster, channel, "notfound",
                    "commander", chatMessage.getSender().getDisplayName(),
                    "target", targetId);
            return;
        }
        final Video lastVideo;
        if(videos.getTotal() < 1) {
            final TotalledVideoList highlights = caster.getTwitchApi().getVideos().getVideos(targetId, 1, 0, false, true);
            if(highlights.getTotal() < 1) {
                Chat.i18nMessage(caster, channel, "nogame",
                        "commander", chatMessage.getSender().getDisplayName(),
                        "target", targetId);
                return;
            }
            lastVideo = highlights.getVideos().get(0);
        } else {
            lastVideo = videos.getVideos().get(0);
        }
        final String lastGame = lastVideo.getGame();
        final String verb;
        final String targetCaster = lastVideo.getChannel().getDisplayName();
        if(lastGame.equals("Creative"))
            verb = GameVerb.getCreativeVerb(lastVideo.getTitle());
        else
            verb = GameVerb.getVerb(lastGame);
        Chat.i18nMessage(caster, channel, "response",
                "target", targetCaster,
                "targetId", targetId,
                "summary", verb);
    }
}
