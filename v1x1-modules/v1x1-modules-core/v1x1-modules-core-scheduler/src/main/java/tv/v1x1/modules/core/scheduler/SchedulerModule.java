package tv.v1x1.modules.core.scheduler;

import tv.v1x1.common.modules.ServiceModule;
import tv.v1x1.modules.core.scheduler.config.SchedulerGlobalConfiguration;
import tv.v1x1.modules.core.scheduler.config.SchedulerUserConfiguration;

/**
 * Created by cobi on 10/22/2016.
 */
public class SchedulerModule extends ServiceModule<SchedulerGlobalConfiguration, SchedulerUserConfiguration> {
    @Override
    public String getName() {
        return "scheduler";
    }

    @Override
    protected void initialize() {
        super.initialize();
        registerService(new SchedulerService(this));
    }

    public static void main(String[] args) {
        try {
            new SchedulerModule().entryPoint(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
