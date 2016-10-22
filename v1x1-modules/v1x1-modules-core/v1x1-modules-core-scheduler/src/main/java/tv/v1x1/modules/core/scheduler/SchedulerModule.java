package tv.v1x1.modules.core.scheduler;

import tv.v1x1.common.modules.ServiceModule;

/**
 * Created by cobi on 10/22/2016.
 */
public class SchedulerModule extends ServiceModule<SchedulerSettings, SchedulerGlobalConfiguration, SchedulerTenantConfiguration> {
    @Override
    public String getName() {
        return "scheduler";
    }

    @Override
    protected void initialize() {
        super.initialize();
    }
}
