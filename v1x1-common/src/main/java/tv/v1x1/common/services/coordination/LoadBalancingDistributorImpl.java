package tv.v1x1.common.services.coordination;

import com.google.common.collect.ImmutableSet;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.proto.core.UUIDOuterClass;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/10/2016.
 */
public class LoadBalancingDistributorImpl implements LoadBalancingDistributor {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String INSTANCE_NAME = "INSTANCE";

    public static class InstanceId {
        private final UUID instanceId;

        public InstanceId(final UUID instanceId) {
            this.instanceId = instanceId;
        }

        public UUID getInstanceId() {
            return instanceId;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final InstanceId that = (InstanceId) o;

            return instanceId != null ? instanceId.equals(that.instanceId) : that.instanceId == null;

        }

        @Override
        public int hashCode() {
            return instanceId != null ? instanceId.hashCode() : 0;
        }
    }

    /*
     * /some/path
     *       |- /entries
     *       |   |- #jacobicarter
     *       |   `- #draskia
     *       `- /instances
     *           |- 1
     *           `- 2
     */

    private final PathChildrenCache entryCache;
    private final ServiceDiscovery<InstanceId> instanceDiscovery;
    private final CuratorFramework framework;
    private final String path;
    private final int redundancy;
    private final Collection<Listener> listeners = new ConcurrentSkipListSet<>();
    private final ServiceCache<InstanceId> serviceCache;

    private final Map<InstanceId, Set<String>> entriesByInstance = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Set<InstanceId>> instancesByEntry = Collections.synchronizedMap(new HashMap<>());
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LoadBalancingDistributorImpl(final CuratorFramework framework, final String path, final int redundancy) {
        this(framework, path, redundancy, null);
    }

    public LoadBalancingDistributorImpl(final CuratorFramework framework, final String path, final int redundancy, final Listener listener) {
        this.framework = framework;
        this.path = path;
        this.redundancy = redundancy;
        if(listener != null)
            addListener(listener);
        entryCache = new PathChildrenCache(framework, path + "/entries", true);
        entryCache.getListenable().addListener((curatorFramework, pathChildrenCacheEvent) -> recalculateEntries());
        instanceDiscovery = ServiceDiscoveryBuilder.builder(InstanceId.class)
                .basePath(path + "/instances")
                .client(framework)
                .serializer(new InstanceIdInstanceSerializer())
                .watchInstances(true)
                .build();
        serviceCache = instanceDiscovery.serviceCacheBuilder()
                .name(INSTANCE_NAME)
                .executorService(executorService)
                .build();
        serviceCache.addListener(new ServiceCacheListener() {
            @Override
            public void cacheChanged() {
                try {
                    recalculateEntries();
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void stateChanged(final CuratorFramework curatorFramework, final ConnectionState connectionState) {
                try {
                    recalculateEntries();
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void addEntry(final String entry) throws Exception {
        framework.create().withMode(CreateMode.PERSISTENT).forPath(path + "/entries/" + entry, entry.getBytes());
    }

    @Override
    public void removeEntry(String entry) throws Exception {
        framework.delete().forPath(path + "/entries/" + entry);
    }

    @Override
    public List<String> listEntries() throws Exception {
        return entryCache.getCurrentData().stream().map(ChildData::getData).map(String::new).collect(Collectors.toList());
    }

    @Override
    public void addInstance(final UUID instanceId) throws Exception {
        instanceDiscovery.registerService(serviceInstanceFor(new InstanceId(instanceId)));
    }

    @Override
    public Set<String> getEntriesForInstance(final UUID instanceId) throws Exception {
        recalculateEntries();
        return entriesByInstance.get(new InstanceId(instanceId));
    }

    private void recalculateEntries() throws Exception {
        LOG.info("Recalculating entries.");
        final List<InstanceId> instances = instanceDiscovery.queryForInstances(INSTANCE_NAME).stream().map(this::instanceIdFor).collect(Collectors.toList());
        if(instances.size() == 0)
            return;
        final int realisticRedundancy = Math.min(redundancy, instances.size());
        final List<String> entries = listEntries();
        final TreeMap<java.util.UUID, InstanceId> instanceLookupTable = new TreeMap<>();
        final Map<InstanceId, Set<String>> newEntriesByInstance = new HashMap<>();
        for(final InstanceId instanceId : instances) {
            newEntriesByInstance.put(instanceId, new HashSet<>());
            java.util.UUID uuid = instanceId.getInstanceId().getValue();
            instanceLookupTable.put(uuid, instanceId);
            for(int i = 1; i < 256; i++) {
                uuid = java.util.UUID.nameUUIDFromBytes(uuid.toString().getBytes());
                instanceLookupTable.put(uuid, instanceId);
            }
        }
        final Map<String, Set<InstanceId>> newInstancesByEntry = new HashMap<>();
        for(final String entry : entries) {
            java.util.UUID key = java.util.UUID.nameUUIDFromBytes(entry.getBytes());
            for(int i = 0; i < realisticRedundancy; i++) {
                InstanceId instanceId;
                do {
                    key = getNext(instanceLookupTable, key);
                    instanceId = instanceLookupTable.get(key);
                } while(!newEntriesByInstance.get(instanceId).add(entry));
                if(!newInstancesByEntry.containsKey(entry))
                    newInstancesByEntry.put(entry, new HashSet<>());
                newInstancesByEntry.get(entry).add(instanceId);
            }
        }
        final Map<InstanceId, Set<String>> diff = generateDiff(entriesByInstance, newEntriesByInstance);
        synchronized(entriesByInstance) {
            synchronized(instancesByEntry) {
                entriesByInstance.clear();
                entriesByInstance.putAll(newEntriesByInstance);
                instancesByEntry.clear();
                instancesByEntry.putAll(newInstancesByEntry);
            }
        }
        notifyListeners(diff);
    }

    private void notifyListeners(final Map<InstanceId, Set<String>> diff) {
        for(final Map.Entry<InstanceId, Set<String>> entry : diff.entrySet())
            for(final Listener listener : listeners)
                listener.notify(entry.getKey().getInstanceId(), ImmutableSet.copyOf(entry.getValue()));
    }

    private Map<InstanceId, Set<String>> generateDiff(final Map<InstanceId, Set<String>> entriesByInstance, final Map<InstanceId, Set<String>> newEntriesByInstance) {
        final Map<InstanceId, Set<String>> diffMap = new HashMap<>();
        entriesByInstance.entrySet().stream()
                .filter(entry -> !newEntriesByInstance.containsKey(entry.getKey()))
                .forEach(entry -> diffMap.put(entry.getKey(), ImmutableSet.of()));
        newEntriesByInstance.entrySet().stream()
                .filter(entry -> !entriesByInstance.containsKey(entry.getKey()) || !entry.getValue().equals(entriesByInstance.get(entry.getKey())))
                .forEach(entry -> diffMap.put(entry.getKey(), entry.getValue()));
        return diffMap;
    }

    private <K, V> K getNext(final NavigableMap<K, V> map, final K key) {
        final K newKey = map.higherKey(key);
        if(newKey == null)
            return map.firstKey();
        else
            return newKey;
    }

    @Override
    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(final Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void removeInstance(final UUID instanceId) throws Exception {
        instanceDiscovery.unregisterService(serviceInstanceFor(new InstanceId(instanceId)));
    }

    @Override
    public void start() throws Exception {
        entryCache.start();
        instanceDiscovery.start();
        serviceCache.start();
        recalculateEntries();
    }

    @Override
    public void shutdown() throws IOException {
        entryCache.close();
        serviceCache.close();
        instanceDiscovery.close();
        executorService.shutdown();
    }

    private ServiceInstance<InstanceId> serviceInstanceFor(final InstanceId instanceId) throws Exception {
        return ServiceInstance.<InstanceId>builder()
                .name(INSTANCE_NAME)
                .id(instanceId.getInstanceId().getValue().toString())
                .payload(instanceId)
                .address("NOT_USED")
                .port(0)
                .serviceType(ServiceType.DYNAMIC)
                .build();
    }

    private InstanceId instanceIdFor(final ServiceInstance<InstanceId> serviceInstance) {
        return serviceInstance.getPayload();
    }

    private class InstanceIdInstanceSerializer implements InstanceSerializer<InstanceId> {
        @Override
        public byte[] serialize(final ServiceInstance<InstanceId> serviceInstance) throws Exception {
            return serviceInstance.getPayload().getInstanceId().toProto().toByteArray();
        }

        @Override
        public ServiceInstance<InstanceId> deserialize(final byte[] bytes) throws Exception {
            final UUID id = UUID.fromProto(UUIDOuterClass.UUID.parseFrom(bytes));
            final InstanceId instanceId = new InstanceId(id);
            return serviceInstanceFor(instanceId);
        }
    }
}
