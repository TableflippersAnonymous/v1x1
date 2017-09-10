package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GroupMembership {
    @JsonProperty
    private Group group;
    @JsonProperty
    private List<GlobalUser> members;

    public GroupMembership() {
    }

    public GroupMembership(final Group group, final List<GlobalUser> members) {
        this.group = group;
        this.members = members;
    }

    public Group getGroup() {
        return group;
    }

    public List<GlobalUser> getMembers() {
        return members;
    }
}
