package tv.twitchbot.common.dto.core;


import tv.twitchbot.common.dto.proto.core.PermissionOuterClass;

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
}
