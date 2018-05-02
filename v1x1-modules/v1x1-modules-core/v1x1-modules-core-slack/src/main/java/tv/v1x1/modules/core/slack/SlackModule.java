package tv.v1x1.modules.core.slack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.modules.ServiceModule;

import java.lang.invoke.MethodHandles;

public class SlackModule extends ServiceModule<SlackGlobalConfiguration, SlackUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SlackModule() {
    }

    @Override
    public String getName() {
        return "slack";
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    public static void main(final String[] args) {
        try {
            new SlackModule().entryPoint(args);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
