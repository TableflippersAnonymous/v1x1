package tv.v1x1.common.services.coordination;

import tv.v1x1.common.dto.core.UUID;

import java.io.IOException;
import java.util.Set;

/**
 * Created by cobi on 10/10/2016.
 */
public interface LoadBalancingDistributor {
    interface Listener extends Comparable<Listener> {
        void notify(UUID instanceId, Set<String> entries);
    }

    void addEntry(String entry) throws Exception;
    void addInstance(UUID instanceId) throws Exception; // Will be bound to our client
    Set<String> getEntriesForInstance(UUID instanceId) throws Exception;
    void addListener(Listener listener);
    void removeListener(Listener listener);
    void removeInstance(UUID instanceId) throws Exception;

    void start() throws Exception;
    void shutdown() throws IOException;
}
