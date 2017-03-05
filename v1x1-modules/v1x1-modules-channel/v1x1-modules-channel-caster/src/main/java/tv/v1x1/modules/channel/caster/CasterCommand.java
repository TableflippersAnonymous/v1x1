package tv.v1x1.modules.channel.caster;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchUserException;
import tv.v1x1.common.services.twitch.dto.videos.TotalledVideoList;
import tv.v1x1.common.services.twitch.dto.videos.Video;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.validation.TwitchValidator;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
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
        final DisplayNameService displayNameService = caster.getInjector().getInstance(DisplayNameService.class);
        final Channel channel = chatMessage.getChannel();
        try {
            final String targetId = displayNameService.getIdFromDisplayName(channel, args.get(0));
            final String targetDisplayName = displayNameService.getDisplayNameFromId(channel, targetId);
            final tv.v1x1.common.services.twitch.dto.channels.Channel videoChannel;
            try {
                videoChannel = caster.getTwitchApi().getChannels().getChannel(targetId);
            } catch (NotFoundException ex) {
                Chat.i18nMessage(caster, channel, "notfound",
                        "commander", chatMessage.getSender().getDisplayName(),
                        "target", targetDisplayName);
                return;
            } catch (WebApplicationException ex) {
                Chat.i18nMessage(caster, channel, "nogame",
                        "target", targetId,
                        "targetId", targetDisplayName);
                throw ex;
            }
            final String targetCaster = videoChannel.getDisplayName();
            final String lastGame = videoChannel.getGame();
            if (lastGame == null) {
                Chat.i18nMessage(caster, channel, "nogame",
                        "target", targetCaster,
                        "targetId", targetDisplayName);
                return;
            }
            final String verb;
            if (lastGame.equals("Creative"))
                verb = GameVerb.getCreativeVerb(videoChannel.getStatus());
            else
                verb = GameVerb.getVerb(lastGame);
            Chat.i18nMessage(caster, channel, "response",
                    "target", targetCaster,
                    "targetId", targetDisplayName,
                    "summary", verb);
        } catch (final NoSuchUserException e) {
            Chat.i18nMessage(caster, channel, "generic.invalid.user",
                    "commander", chatMessage.getSender().getDisplayName(),
                    "input", args.get(0));
        }
    }
}
