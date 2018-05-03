package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.events.Event;

import java.util.List;

public abstract class MultiPartyConversationBase extends ConversationBase {
    @JsonProperty
    private String name;
    @JsonProperty
    private String creator;
    @JsonProperty("is_mpim")
    private boolean isMpim;
    @JsonProperty
    private List<String> members;

    public MultiPartyConversationBase() {
    }

    public MultiPartyConversationBase(final String id, final long created, final String lastRead, final Event latest,
                                      final long unreadCount, final long unreadCountDisplay, final String name,
                                      final String creator, final boolean isMpim, final List<String> members) {
        super(id, created, lastRead, latest, unreadCount, unreadCountDisplay);
        this.name = name;
        this.creator = creator;
        this.isMpim = isMpim;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(final String creator) {
        this.creator = creator;
    }

    public boolean isMpim() {
        return isMpim;
    }

    public void setMpim(final boolean mpim) {
        isMpim = mpim;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(final List<String> members) {
        this.members = members;
    }
}
