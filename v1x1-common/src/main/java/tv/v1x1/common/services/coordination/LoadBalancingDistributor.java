package tv.v1x1.common.services.coordination;

import tv.v1x1.common.dto.core.UUID;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by naomi on 10/10/2016.
 */
public interface LoadBalancingDistributor {
    interface Listener extends Comparable<Listener> {
        void notify(UUID instanceId, Map<String, Integer> entries);
    }

    void addEntry(String entry) throws Exception;
    void removeEntry(String entry) throws Exception;
    List<String> listEntries() throws Exception;
    void addInstance(UUID instanceId) throws Exception; // Will be bound to our client
    Map<String, Integer> getEntriesForInstance(UUID instanceId) throws Exception;
    void addListener(Listener listener);
    void removeListener(Listener listener);
    void removeInstance(UUID instanceId) throws Exception;

    void start() throws Exception;
    void shutdown() throws IOException;
}
