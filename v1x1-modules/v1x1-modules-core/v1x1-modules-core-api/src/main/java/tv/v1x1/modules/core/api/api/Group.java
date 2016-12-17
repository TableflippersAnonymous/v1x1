package tv.v1x1.modules.core.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.TenantGroup;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cobi on 12/18/2016.
 */
public class Group {
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("group_id")
    private String groupId;
    @JsonProperty
    private String name;
    @JsonProperty
    private List<String> permissions;

    public Group() {
    }

    public Group(final String tenantId, final String groupId, final String name, final List<String> permissions) {
        this.tenantId = tenantId;
        this.groupId = groupId;
        this.name = name;
        this.permissions = permissions;
    }

    public Group(final TenantGroup tenantGroup) {
        this.tenantId = tenantGroup.getTenantId().toString();
        this.groupId = tenantGroup.getGroupId().toString();
        this.name = tenantGroup.getName();
        this.permissions = tenantGroup.getPermissions().stream().map(Permission::getNode).collect(Collectors.toList());
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
}
