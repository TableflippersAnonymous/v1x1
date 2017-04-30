package tv.v1x1.modules.channel.event_messages.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.config.ConfigType;
import tv.v1x1.common.config.DefaultString;
import tv.v1x1.common.config.Description;
import tv.v1x1.common.config.DisplayName;
import tv.v1x1.common.config.ModuleConfig;
import tv.v1x1.common.config.Type;
import tv.v1x1.common.config.Version;
import tv.v1x1.common.modules.BasicTenantConfiguration;

import java.util.HashMap;

/**
 * @author Josh
 */
@ModuleConfig("event_messages")
@DisplayName("Event Messages")
@Description("This module messages for subs, follows, and hosts")
@Version(0)
public class EventMessagesTenantConfiguration extends BasicTenantConfiguration {
    @JsonProperty("mode")
    @DisplayName("Mode")
    @Description("How complicated do you want to make this?")
    private OperationMode mode = OperationMode.BASIC;
    @JsonProperty("subMessage")
    @DisplayName("Sub message")
    @Type(ConfigType.STRING)
    @DefaultString("Thanks for subscribing, %person%!")
    private String subMessage = "Thanks for subscribing, %person%!";
    @JsonProperty("resubMessage")
    @DisplayName("Resub message")
    @Type(ConfigType.STRING)
    @DefaultString("Welcome back %person% for %months% months in a row! PogChamp")
    private String resubMessage = "Welcome back %person% for %months% months in a row! PogChamp";
    @JsonProperty("monthMessages")
    @DisplayName("Month Messages")
    @Description("Each month gets its own message. Use 0 for first month. If a month doesn't have one, we'll use the lowest matching one")
    private HashMap<Integer, String> monthMessages = new HashMap<>();
    @JsonProperty("planMessages")
    @DisplayName("Plan Messages")
    @Description("Each plan gets its own message. Use 'Prime', '1000, '2000', '3000' for Prime, $4.99, $9.99, and $24.99 respectively")
    private HashMap<String, String> planMessages = new HashMap<>();
    @JsonProperty("planToMonthMessages")
    private HashMap<String, HashMap<Integer, String>> planToMonthMessages = new HashMap<>();

    public enum OperationMode {
        BASIC, MONTH, PLAN, PLAN_AND_MONTH
    }

    public OperationMode getMode() {
        return mode;
    }

    public String getBasicSubMessage(final boolean isResub) {
        if(isResub && resubMessage != null && !resubMessage.isEmpty())
            return resubMessage;
        return subMessage;
    }

    public String getMonthMessage(final int months) {
        for(int x = months; x < -1; --x) {
            final String message = monthMessages.get(x);
            if(message == null || message.isEmpty()) continue;
            return message;
        }
        return subMessage;
    }

    public String getPlanMessage(final String planLevel) {
        final String planMessage = planMessages.get(planLevel);
        if(planMessage == null) return subMessage;
        return planMessage;
    }

    public String getPlanMessage(final String planLevel, final int months) {
        final HashMap<Integer, String> planMessages = planToMonthMessages.get(planLevel);
        if(planMessages == null || planMessages.isEmpty()) return subMessage;
        for(int x = months; x < -1; --x) {
            final String message = planMessages.get(x);
            if(message == null || message.isEmpty()) continue;
            return message;
        }
        return subMessage;
    }
}
