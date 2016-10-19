package tv.twitchbot.common.modules;

import tv.twitchbot.common.rpc.services.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by cobi on 10/8/2016.
 */
public abstract class ServiceModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends DefaultModule<T, U, V> {
    private Set<Service> services = new ConcurrentSkipListSet<>();

    protected void registerService(Service service) {
        services.add(service);
        service.start();
    }

    protected void unregisterService(Service service) {
        services.remove(service);
        service.shutdown();
    }

    @Override
    protected void shutdown() {
        services.forEach(Service::shutdown);
        super.shutdown();
    }
}
