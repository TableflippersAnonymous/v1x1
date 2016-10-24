package tv.v1x1.common.services.coordination;

import com.google.common.collect.ImmutableSet;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.ModuleInstance;
import tv.v1x1.common.dto.proto.core.ModuleOuterClass;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/7/2016.
 */
public class ModuleRegistry {
    private final ServiceDiscovery<ModuleInstance> serviceDiscovery;
    private final ServiceCache<ModuleInstance> serviceCache;

    public ModuleRegistry(final CuratorFramework framework, final ModuleInstance moduleInstance) {
        try {
            serviceDiscovery = ServiceDiscoveryBuilder.builder(ModuleInstance.class)
                    .basePath("/v1x1/module_registry")
                    .client(framework)
                    .serializer(new InstanceSerializer<ModuleInstance>() {
                        @Override
                        public byte[] serialize(final ServiceInstance<ModuleInstance> serviceInstance) throws Exception {
                            return serviceInstance.getPayload().toProto().toByteArray();
                        }

                        @Override
                        public ServiceInstance<ModuleInstance> deserialize(final byte[] bytes) throws Exception {
                            final ModuleInstance instance = ModuleInstance.fromProto(ModuleOuterClass.ModuleInstance.parseFrom(bytes));
                            return serviceInstanceFromModuleInstance(instance);
                        }
                    })
                    .thisInstance(serviceInstanceFromModuleInstance(moduleInstance))
                    .build();
            serviceDiscovery.start();
            serviceCache = serviceDiscovery.serviceCacheBuilder().build();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ServiceInstance<ModuleInstance> serviceInstanceFromModuleInstance(final ModuleInstance moduleInstance) throws Exception {
        return ServiceInstance.<ModuleInstance>builder()
                .id(moduleInstance.getId().getValue().toString())
                .name(moduleInstance.getModule().getName())
                .payload(moduleInstance)
                .address("NOT_USED")
                .port(0)
                .build();
    }

    public Set<Module> modules() {
        try {
            return ImmutableSet.copyOf(serviceDiscovery.queryForNames().stream().map(Module::new).collect(Collectors.toList()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<ModuleInstance> getInstances(final Module module) {
        try {
            return ImmutableSet.copyOf(serviceDiscovery.queryForInstances(module.getName()).stream().map(ServiceInstance::getPayload).collect(Collectors.toList()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Module> liveModules() {
        try {
            return ImmutableSet.copyOf(serviceCache.getInstances().stream().map(moduleInstanceServiceInstance -> moduleInstanceServiceInstance.getPayload().getModule()).collect(Collectors.toSet()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() throws IOException {
        serviceCache.close();
        serviceDiscovery.close();
    }
}
