package tv.twitchbot.modules.core.scheduler;

import tv.twitchbot.common.dto.messages.requests.ScheduleRequest;
import tv.twitchbot.common.dto.messages.responses.ScheduleResponse;
import tv.twitchbot.common.modules.GlobalConfiguration;
import tv.twitchbot.common.modules.Module;
import tv.twitchbot.common.modules.ModuleSettings;
import tv.twitchbot.common.modules.TenantConfiguration;
import tv.twitchbot.common.rpc.services.ThreadedService;

/**
 * Created by cobi on 10/22/2016.
 */
public class SchedulerService extends ThreadedService<ScheduleRequest, ScheduleResponse> {
    public SchedulerService(final Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module, final String serviceName, final Class<ScheduleRequest> requestClass) {
        super(module, serviceName, requestClass);
    }

    @Override
    protected ScheduleResponse call(final ScheduleRequest request) {
        return null;
    }
}
