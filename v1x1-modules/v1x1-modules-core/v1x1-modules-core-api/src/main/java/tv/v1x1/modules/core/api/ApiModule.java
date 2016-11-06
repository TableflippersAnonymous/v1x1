package tv.v1x1.modules.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.modules.ServiceModule;
import tv.v1x1.modules.core.api.config.ApiGlobalConfiguration;
import tv.v1x1.modules.core.api.config.ApiSettings;
import tv.v1x1.modules.core.api.config.ApiTenantConfiguration;

import java.lang.invoke.MethodHandles;

/**
 * Created by naomi on 10/24/2016.
 */
public class ApiModule extends ServiceModule<ApiSettings, ApiGlobalConfiguration, ApiTenantConfiguration, ApiChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Thread applicationThread;
    private ApiApplication application;

    @Override
    public String getName() {
        return "api";
    }

    @Override
    protected void initialize() {
        super.initialize();
        application = new ApiApplication(this);
        applicationThread = new Thread(() -> {
            try {
                application.run("server", getConfigFile());
            } catch (Exception e) {
                LOG.error("Caught exception in dropwizard ApiApplication thread", e);
            }
        });
        applicationThread.start();
    }

    @Override
    protected void shutdown() {
        applicationThread.interrupt();
        super.shutdown();
    }
}
