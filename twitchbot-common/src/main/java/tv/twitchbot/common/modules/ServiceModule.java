package tv.twitchbot.common.modules;

import tv.twitchbot.common.rpc.services.Service;

import java.util.Set;

/**
 * Created by cobi on 10/8/2016.
 */
public abstract class ServiceModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends DefaultModule<T, U, V> {
    private Set<Service> services;

    protected void registerService(Service service) {
        services.add(service);
        service.start();
    }

    @Override
    protected void shutdown() {
        for(Service service : services)
            service.shutdown();
        super.shutdown();
    }
}
