package tv.v1x1.modules.core.api;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.services.pubsub.TopicManager;
import tv.v1x1.modules.core.api.auth.Authorizer;
import tv.v1x1.modules.core.api.config.ApiConfiguration;
import tv.v1x1.modules.core.api.resources.ws.PubsubResource;


import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.lang.invoke.MethodHandles;
import java.util.EnumSet;

/**
 * Created by cobi on 10/24/2016.
 */
public class ApiApplication extends Application<ApiConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ApiModule module;
    private GuiceBundle<ApiConfiguration> guiceBundle;

    public ApiApplication(final ApiModule module) {
        this.module = module;
    }

    @Override
    public void initialize(final Bootstrap<ApiConfiguration> bootstrap) {
        guiceBundle = GuiceBundle.<ApiConfiguration>newBuilder()
                .addModule(new ApiGuiceModule(module))
                .enableAutoConfig(getClass().getPackage().getName())
                .setConfigClass(ApiConfiguration.class)
                .build();
        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(new WebsocketBundle(PubsubResource.class));
    }

    @Override
    public String getName() {
        return "v1x1-modules-core-api";
    }

    @Override
    public void run(final ApiConfiguration configuration, final Environment environment) throws Exception {
        LOG.info("Acquiring module lock.");
        synchronized (module) {
            LOG.info("Waking module.");
            module.notifyAll();
            LOG.info("Waiting on module.");
            module.wait();
        }
        LOG.info("Woken by module.");

        PubsubResource.initialize(guiceBundle.getInjector().getInstance(TopicManager.class), guiceBundle.getInjector().getInstance(Authorizer.class));

        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin,Client-ID,Authorization");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
