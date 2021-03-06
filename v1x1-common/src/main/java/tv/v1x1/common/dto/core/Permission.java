package tv.v1x1.common.dto.core;


import tv.v1x1.common.dto.proto.core.PermissionOuterClass;

/**
 * Represents a permission node to attach to an entity
 * @author Cobi
 */
public class Permission {
    public static Permission fromProto(final PermissionOuterClass.Permission proto) {
        final String node = proto.getNode();
        return new Permission(node);
    }

    private final String node;

    public Permission(final String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public PermissionOuterClass.Permission toProto() {
        return PermissionOuterClass.Permission.newBuilder()
                .setNode(node)
                .build();
    }

    @Override
    public boolean equals(final Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        final Permission that = (Permission)o;

        return node != null ? node.equals(that.node) : that.node == null;

    }

    @Override
    public int hashCode() {
        return node != null ? node.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getNode();
    }
}
