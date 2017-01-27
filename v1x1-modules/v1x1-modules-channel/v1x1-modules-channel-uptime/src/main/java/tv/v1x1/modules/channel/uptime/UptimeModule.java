package tv.v1x1.modules.channel.uptime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.util.commands.CommandDelegator;

import java.lang.invoke.MethodHandles;
import java.time.Instant;

/**
 * @author Josh
 */
public class UptimeModule extends RegisteredThreadedModule<UptimeModuleSettings, UptimeModuleGlobalConfiguration, UptimeModuleTenantConfiguration, UptimeModuleChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static {
        final Module module = new Module("uptime");
        I18n.registerDefault(module, "online", "%target%: The stream has been live for %uptime%");
        I18n.registerDefault(module, "offline", "%target%: The stream isn't online! ResidentSleeper");
    }

    public static void main(final String[] args) throws Exception {
        new UptimeModule().entryPoint(args);
    }

    private TwitchApi api;
    private CommandDelegator delegator = new CommandDelegator("!");

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

    /*pkg-private*/ Instant getUptime(final String targetId) throws Exception {
        final tv.v1x1.common.services.twitch.dto.streams.Stream videoChannel;
        videoChannel = api.getStreams().getStream(targetId).getStream();
        if(videoChannel == null) {
            LOG.warn("videoChannel is null??");
            return null;
        }
        final String dateStr = videoChannel.getCreatedAt();
        if(dateStr == null) {
            return null;
        }
        return Instant.parse(dateStr);
    }

    private boolean isEnabled(final Channel channel) {
        if(getChannelConfiguration(channel).isOverridden()) {
            return getChannelConfiguration(channel).isEnabled();
        } else {
            return getTenantConfiguration(channel.getTenant()).isEnabled();
        }
    }

    public class UptimeListener implements EventListener {
        @EventHandler
        public void onChatMessage(ChatMessageEvent ev) {
            if(isEnabled(ev.getChatMessage().getChannel()))
                delegator.handleChatMessage(ev);
        }
    }
}
