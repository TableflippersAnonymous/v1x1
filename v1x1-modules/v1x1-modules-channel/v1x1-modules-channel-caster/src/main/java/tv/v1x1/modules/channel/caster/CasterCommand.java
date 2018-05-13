package tv.v1x1.modules.channel.caster;

import com.google.common.collect.ImmutableList;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.services.state.NoSuchUserException;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.text.CaseChanger;
import tv.v1x1.modules.channel.caster.streaminfo.StreamInfo;
import tv.v1x1.modules.channel.caster.streaminfo.StreamInfoFactory;

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
        return ImmutableList.of("caster", "follow", "shoutout", "twitch", "mixer");
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
        final Channel channel = chatMessage.getChannel();
        String targetStreamer = args.get(0);
        Platform targetPlatform = channel.getPlatform();
        final String commander = chatMessage.getSender().getDisplayName();
        try {
            targetPlatform = Platform.valueOf(command.toUpperCase());
        } catch(IllegalArgumentException ex) {
            // This space intentionally left blank
        }
        if(targetStreamer.contains("/")) {
            final String[] targetAndPlatform = targetStreamer.split("/");
            targetStreamer = targetAndPlatform[0];
            final String targetPlatformStr = targetAndPlatform[1];
            try {
                targetPlatform = Platform.valueOf(targetPlatformStr.toUpperCase());
            } catch(IllegalArgumentException ex) {
                Chat.i18nMessage(caster, channel, "invalidplatform",
                        "commander", commander,
                        "platform", CaseChanger.titlecase(targetPlatformStr));
                return;
            }
        }
        try {
            final StreamInfo info = StreamInfoFactory.getInfo(targetStreamer, targetPlatform);
            if(info.getGame() == null) {
                Chat.i18nMessage(caster, channel, "nogame",
                        "target", info.getDisplayName(),
                        "url", info.getUrl());
            } else {
                Chat.i18nMessage(caster, channel, "response",
                        "target", info.getDisplayName(),
                        "url", info.getUrl(),
                        "summary", info.getActivity(),
                        "game", info.getGame(),
                        "platform", targetPlatform.stylize());
            }
        } catch(NoSuchUserException ex) {
            Chat.i18nMessage(caster, channel, "notfound",
                    "commander", commander,
                    "target", targetStreamer,
                    "platform", targetPlatform.stylize());
        } catch(WebApplicationException ex) {
            // Problem with upstream APIs; just fake it 'till you make it
            Chat.i18nMessage(caster, channel, "nogame",
                    "target", targetStreamer,
                    "targetId", targetPlatform.stylize());
        } catch(IllegalArgumentException ex) {
            Chat.i18nMessage(caster, channel, "nostreams",
                    "commander", commander,
                    "platform", targetPlatform.stylize());
        } catch(UnsupportedOperationException ex) {
            Chat.i18nMessage(caster, channel, "invalidplatform",
                    "commander", commander,
                    "platform", targetPlatform.stylize());
        }
    }
}
