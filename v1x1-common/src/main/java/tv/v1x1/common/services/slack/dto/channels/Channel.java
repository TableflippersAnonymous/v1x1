package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.events.Event;

import java.util.List;

public class Channel extends ChannelBase {
    @JsonProperty("is_channel")
    private boolean isChannel;
    @JsonProperty("is_general")
    private boolean isGeneral;
    @JsonProperty("name_normalized")
    private String nameNormalized;
    @JsonProperty("is_shared")
    private boolean isShared;
    @JsonProperty("is_org_shared")
    private boolean isOrgShared;
    @JsonProperty("is_member")
    private boolean isMember;
    @JsonProperty("is_private")
    private boolean isPrivate;
    @JsonProperty("previous_names")
    private List<String> previousNames;

    public Channel() {
    }

    public Channel(final String id, final long created, final String lastRead, final Event latest,
                   final long unreadCount, final long unreadCountDisplay, final String name, final String creator,
                   final boolean isMpim, final List<String> members, final boolean isArchived, final TrackedValue topic,
                   final TrackedValue purpose, final boolean isChannel, final boolean isGeneral,
                   final String nameNormalized, final boolean isShared, final boolean isOrgShared,
                   final boolean isMember, final boolean isPrivate, final List<String> previousNames) {
        super(id, created, lastRead, latest, unreadCount, unreadCountDisplay, name, creator, isMpim, members,
                isArchived, topic, purpose);
        this.isChannel = isChannel;
        this.isGeneral = isGeneral;
        this.nameNormalized = nameNormalized;
        this.isShared = isShared;
        this.isOrgShared = isOrgShared;
        this.isMember = isMember;
        this.isPrivate = isPrivate;
        this.previousNames = previousNames;
    }

    public boolean isChannel() {
        return isChannel;
    }

    public void setChannel(final boolean channel) {
        isChannel = channel;
    }

    public boolean isGeneral() {
        return isGeneral;
    }

    public void setGeneral(final boolean general) {
        isGeneral = general;
    }

    public String getNameNormalized() {
        return nameNormalized;
    }

    public void setNameNormalized(final String nameNormalized) {
        this.nameNormalized = nameNormalized;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(final boolean shared) {
        isShared = shared;
    }

    public boolean isOrgShared() {
        return isOrgShared;
    }

    public void setOrgShared(final boolean orgShared) {
        isOrgShared = orgShared;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(final boolean member) {
        isMember = member;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(final boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<String> getPreviousNames() {
        return previousNames;
    }

    public void setPreviousNames(final List<String> previousNames) {
        this.previousNames = previousNames;
    }
}
