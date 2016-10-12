package tv.twitchbot.modules.core.tmi;

import tv.twitchbot.common.modules.ServiceModule;

/**
 * Created by naomi on 10/8/2016.
 */
public class TmiModule extends ServiceModule<TmiSettings, TmiGlobalConfiguration, TmiTenantConfiguration> {
    @Override
    protected void initialize() {
        super.initialize();

    }



    @Override
    public String getName() {
        return "tmi";
    }
}
