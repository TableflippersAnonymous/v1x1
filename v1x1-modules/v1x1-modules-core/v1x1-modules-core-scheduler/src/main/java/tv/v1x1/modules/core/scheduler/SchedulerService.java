package tv.v1x1.modules.core.scheduler;

import tv.v1x1.common.dto.messages.requests.ScheduleRequest;
import tv.v1x1.common.dto.messages.responses.ScheduleResponse;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.modules.ModuleSettings;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.rpc.services.ThreadedService;

/**
 * Created by naomi on 10/22/2016.
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
