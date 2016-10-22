package tv.v1x1.common.modules;

import tv.v1x1.common.rpc.services.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by cobi on 10/8/2016.
 */
public abstract class ServiceModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends DefaultModule<T, U, V> {
    private final Collection<Service> services = new ConcurrentSkipListSet<>();

    protected void registerService(final Service service) {
        services.add(service);
        service.start();
    }

    protected void unregisterService(final Service service) {
        services.remove(service);
        service.shutdown();
    }

    @Override
    protected void shutdown() {
        services.forEach(Service::shutdown);
        super.shutdown();
    }
}
