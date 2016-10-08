package tv.twitchbot.common.services.coordination;

import com.google.common.collect.ImmutableSet;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.ModuleInstance;
import tv.twitchbot.common.dto.proto.core.ModuleOuterClass;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/7/2016.
 */
public class ModuleRegistry {
    private ServiceDiscovery<ModuleInstance> serviceDiscovery;

    public ModuleRegistry(CuratorFramework framework, final ModuleInstance moduleInstance) {
        try {
            serviceDiscovery = ServiceDiscoveryBuilder.builder(ModuleInstance.class)
                    .basePath("/twitchbot/module_registry")
                    .client(framework)
                    .serializer(new InstanceSerializer<ModuleInstance>() {
                        @Override
                        public byte[] serialize(ServiceInstance<ModuleInstance> serviceInstance) throws Exception {
                            return serviceInstance.getPayload().toProto().toByteArray();
                        }

                        @Override
                        public ServiceInstance<ModuleInstance> deserialize(byte[] bytes) throws Exception {
                            ModuleInstance instance = ModuleInstance.fromProto(ModuleOuterClass.ModuleInstance.parseFrom(bytes));
                            return serviceInstanceFromModuleInstance(instance);
                        }
                    })
                    .thisInstance(serviceInstanceFromModuleInstance(moduleInstance))
                    .build();
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ServiceInstance<ModuleInstance> serviceInstanceFromModuleInstance(ModuleInstance moduleInstance) throws Exception {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<ModuleInstance> getInstances(Module module) {
        try {
            return ImmutableSet.copyOf(serviceDiscovery.queryForInstances(module.getName()).stream().map(ServiceInstance::getPayload).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() throws IOException {
        serviceDiscovery.close();
    }
}
