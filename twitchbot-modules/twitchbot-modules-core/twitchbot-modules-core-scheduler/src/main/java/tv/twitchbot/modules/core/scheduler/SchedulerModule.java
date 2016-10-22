package tv.twitchbot.modules.core.scheduler;

import tv.twitchbot.common.modules.ServiceModule;

/**
 * Created by naomi on 10/22/2016.
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
