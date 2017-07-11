package tv.v1x1.common.services.pubsub;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

/**
 * Created by cobi on 7/10/2017.
 */
public class TopicName {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // topic:$tenantId:$module:$topic_name
    public static TopicName parse(final String topicName) {
        final List<String> topicParts = Splitter.on(":").splitToList(topicName);

        if(topicParts.size() != 4) {
            return null;
        }

        if(!topicParts.get(0).equals("topic")) {
            return null;
        }

        if(topicParts.get(2).contains(".")) {
            return null;
        }

        try {
            final UUID tenantId = UUID.fromString(topicParts.get(1));
            final Module module = new Module(topicParts.get(2));
            final String name = topicParts.get(3);
            return new TopicName(tenantId, module, name);
        } catch(final Exception e) {
            LOG.warn("Got exception parsing topic.", e);
            return null;
        }
    }

    private final UUID tenantId;
    private final Module module;
    private final String name;

    public TopicName(final UUID tenantId, final Module module, final String name) {
        this.tenantId = tenantId;
        this.module = module;
        this.name = name;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public Module getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public String getFullTopicName() {
        return "topic:" + tenantId + ":" + module.getName() + ":" + name;
    }

    @Override
    public String toString() {
        return getFullTopicName();
    }
}
