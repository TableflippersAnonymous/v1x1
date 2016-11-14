package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberList {
    @JsonProperty("_total")
    private long total;
    @JsonProperty
    private List<Subscriber> subscribers;

    public SubscriberList() {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(final List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }
}
