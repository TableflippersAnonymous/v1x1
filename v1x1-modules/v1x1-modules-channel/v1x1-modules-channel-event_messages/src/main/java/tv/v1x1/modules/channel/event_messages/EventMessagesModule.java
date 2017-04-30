package tv.v1x1.modules.channel.event_messages;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.TwitchUser;
import tv.v1x1.common.i18n.FakeLanguage;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.modules.channel.event_messages.config.EventMessagesChannelConfiguration;
import tv.v1x1.modules.channel.event_messages.config.EventMessagesGlobalConfiguration;
import tv.v1x1.modules.channel.event_messages.config.EventMessagesModuleSettings;
import tv.v1x1.modules.channel.event_messages.config.EventMessagesTenantConfiguration;

import java.lang.invoke.MethodHandles;

/**
 * @author Josh
 */
public class EventMessagesModule extends RegisteredThreadedModule<EventMessagesModuleSettings, EventMessagesGlobalConfiguration, EventMessagesTenantConfiguration, EventMessagesChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static {
        final Module module = new Module("event_messages");
    }

    final private FakeLanguage formatter = new FakeLanguage();
    private EventMessagesListener listener;

    @Override
    public String getName() {
        return "Event Messages";
    }

    @Override
    protected void initialize() {
        super.initialize();
        registerListener(new EventMessagesListener(this));
    }

    void sendTwitchSub(final TwitchChannel channel, final TwitchUser user, final boolean isResub, final String planLevel,
                              final String planName, final int months) {
        final String templateMessage = getTemplateForTwitchSub(channel, isResub, planLevel, months);
        Chat.message(this, channel, format(user, templateMessage, planName, months));
    }

    private String getTemplateForTwitchSub(final TwitchChannel channel, final boolean isResub, final String planLevel,
                                          final int months) {
        EventMessagesTenantConfiguration config = getTenantConfiguration(channel.getTenant());
        switch(config.getMode()) {
            case BASIC:
                return config.getBasicSubMessage(isResub);
            case MONTH:
                return config.getMonthMessage((isResub ? months : 0));
            case PLAN:
                return config.getPlanMessage(planLevel);
            case PLAN_AND_MONTH:
                return config.getPlanMessage(planLevel, (isResub ? months : 0));
            default:
                return config.getBasicSubMessage(isResub);
        }
    }

    private String format(final TwitchUser user, final String template, final String planName, final int months) {
        return formatter.message(this.toDto(), template, ImmutableMap.of(
                "person", user.getDisplayName(),
                "plan", planName,
                "months", months));
    }
}
