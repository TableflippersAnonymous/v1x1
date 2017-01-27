package tv.v1x1.modules.channel.uptime;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;

import javax.ws.rs.WebApplicationException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.List;

/**
 * @author Josh
 */
public class UptimeCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private UptimeModule module;

    public UptimeCommand(final UptimeModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("uptime");
    }

    @Override
    public String getUsage() {
        return "[target]";
    }

    @Override
    public String getDescription() {
        return "get the uptime of the stream";
    }

    @Override
    public String getHelp() {
        return "if you give a target, it'll ping them instead of you";
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("uptime.use"));
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final String commander = chatMessage.getSender().getDisplayName();
        final String messageTarget = (args.size() > 0 ? args.get(0) : commander);
        final Channel channel = chatMessage.getChannel();
        if(! channel.getPlatform().equals(Platform.TWITCH)) {
            Chat.i18nMessage(module, channel, "invalid.platform",
                    "commander", commander);
        }
        final String target = channel.getId().substring(1);
        final Instant uptime;
        try {
            uptime = module.getUptime(target);
        } catch(WebApplicationException ex) {
            Chat.i18nMessage(module, channel, "generic.twitchapi.error");
            LOG.warn("Failed to query the Twitch API for uptime", ex);
            return;
        } catch(Exception ex) {
            Chat.i18nMessage(module, channel, "generic.error",
                    "commander", commander,
                    "message", "Underlying exception: " + ex.getClass().getSimpleName());
            LOG.error("Unknown exception during !uptime for {}", ex);
            return;
        }
        if(uptime == null) {
            Chat.i18nMessage(module, channel, "offline",
                    "target", messageTarget);
        } else {
            final Instant now = Calendar.getInstance().toInstant().truncatedTo(ChronoUnit.MINUTES);
            final Duration duration = Duration.between(uptime.truncatedTo(ChronoUnit.MINUTES), now).abs();
            final String durationWords = DurationFormatUtils.formatDurationWords(duration
                    .toMillis(), true, true);
            Chat.i18nMessage(module, channel, "online",
                    "target", messageTarget,
                    "uptime", durationWords);
        }
    }
}
