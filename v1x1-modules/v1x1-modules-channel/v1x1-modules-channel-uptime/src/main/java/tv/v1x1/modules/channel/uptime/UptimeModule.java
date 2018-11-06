package tv.v1x1.modules.channel.uptime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.scanners.i18n.I18nDefault;
import tv.v1x1.common.scanners.i18n.I18nDefaults;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.util.commands.CommandDelegator;

import java.lang.invoke.MethodHandles;
import java.time.Instant;

/**
 * @author Josh
 */
@Permissions(version = 1, value = {
        @RegisteredPermission(
                node = "uptime.use",
                displayName = "Use Uptime",
                description = "This gives you the permission to use the !uptime command",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS, DefaultGroup.SUBS, DefaultGroup.EVERYONE}
        )
})
@I18nDefaults(version = 1, value = {
        @I18nDefault(
                key = "online",
                message = "%target%: The stream has been live for %uptime%",
                displayName = "Online",
                description = "Response when the stream is live."
        ),
        @I18nDefault(
                key = "offline",
                message = "%target%: The stream isn't online! ResidentSleeper",
                displayName = "Offline",
                description = "Response when the stream is not live."
        )
})
public class UptimeModule extends RegisteredThreadedModule<UptimeModuleGlobalConfiguration, UptimeModuleUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(final String[] args) throws Exception {
        new UptimeModule().entryPoint(args);
    }

    private TwitchApi api;
    private final CommandDelegator delegator = new CommandDelegator("!");

    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    protected void initialize() {
        super.initialize();
        api = getTwitchApi();
        delegator.registerCommand(new UptimeCommand(this));
        registerListener(new UptimeListener());
    }

    /*pkg-private*/ Instant getUptime(final String targetId) {
        final tv.v1x1.common.services.twitch.dto.streams.Stream videoChannel;
        videoChannel = api.getStreams().getStream(targetId).getStream();
        if(videoChannel == null) {
            return null;
        }
        final String dateStr = videoChannel.getCreatedAt();
        if(dateStr == null) {
            return null;
        }
        return Instant.parse(dateStr);
    }

    private boolean isEnabled(final Channel channel) {
        return getConfiguration(channel).isEnabled();
    }

    public class UptimeListener implements EventListener {
        @EventHandler
        public void onChatMessage(final ChatMessageEvent ev) {
            if(isEnabled(ev.getChatMessage().getChannel()))
                delegator.handleChatMessage(ev);
        }
    }
}
