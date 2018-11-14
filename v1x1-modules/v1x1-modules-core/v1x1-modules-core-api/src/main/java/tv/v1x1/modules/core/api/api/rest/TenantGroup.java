package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TenantGroup {
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("group_id")
    private String groupId;
    @JsonProperty
    private String name;
    @JsonProperty
    private List<String> permissions;
    @JsonProperty
    private List<GlobalUser> users;

    public TenantGroup() {
    }

    public TenantGroup(final String tenantId, final String groupId, final String name, final List<String> permissions,
                       final List<GlobalUser> users) {
        this.tenantId = tenantId;
        this.groupId = groupId;
        this.name = name;
        this.permissions = permissions;
        this.users = users;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(final List<String> permissions) {
        this.permissions = permissions;
    }

    public List<GlobalUser> getUsers() {
        return users;
    }

    public void setUsers(final List<GlobalUser> users) {
        this.users = users;
    }
}
