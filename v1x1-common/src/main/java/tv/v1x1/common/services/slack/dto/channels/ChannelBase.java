package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.events.Event;

import java.util.List;

public abstract class ChannelBase extends MultiPartyConversationBase {
    @JsonProperty("is_archived")
    private boolean isArchived;
    @JsonProperty
    private TrackedValue topic;
    @JsonProperty
    private TrackedValue purpose;

    public ChannelBase() {
    }

    public ChannelBase(final String id, final long created, final String lastRead, final Event latest,
                       final long unreadCount, final long unreadCountDisplay, final String name, final String creator,
                       final boolean isMpim, final List<String> members, final boolean isArchived,
                       final TrackedValue topic, final TrackedValue purpose) {
        super(id, created, lastRead, latest, unreadCount, unreadCountDisplay, name, creator, isMpim, members);
        this.isArchived = isArchived;
        this.topic = topic;
        this.purpose = purpose;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(final boolean archived) {
        isArchived = archived;
    }

    public TrackedValue getTopic() {
        return topic;
    }

    public void setTopic(final TrackedValue topic) {
        this.topic = topic;
    }

    public TrackedValue getPurpose() {
        return purpose;
    }

    public void setPurpose(final TrackedValue purpose) {
        this.purpose = purpose;
    }
}
