package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.events.Event;

public abstract class ConversationBase {
    @JsonProperty
    private String id;
    @JsonProperty
    private long created;
    @JsonProperty("last_read")
    private String lastRead;
    @JsonProperty
    private Event latest;
    @JsonProperty("unread_count")
    private long unreadCount;
    @JsonProperty("unread_count_display")
    private long unreadCountDisplay;

    public ConversationBase() {
    }

    public ConversationBase(final String id, final long created, final String lastRead, final Event latest,
                            final long unreadCount, final long unreadCountDisplay) {
        this.id = id;
        this.created = created;
        this.lastRead = lastRead;
        this.latest = latest;
        this.unreadCount = unreadCount;
        this.unreadCountDisplay = unreadCountDisplay;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(final long created) {
        this.created = created;
    }

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(final String lastRead) {
        this.lastRead = lastRead;
    }

    public Event getLatest() {
        return latest;
    }

    public void setLatest(final Event latest) {
        this.latest = latest;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(final long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public long getUnreadCountDisplay() {
        return unreadCountDisplay;
    }

    public void setUnreadCountDisplay(final long unreadCountDisplay) {
        this.unreadCountDisplay = unreadCountDisplay;
    }
}
