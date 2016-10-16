package tv.twitchbot.common.dto.core;


import tv.twitchbot.common.dto.proto.core.PermissionOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class Permission {
    public static Permission fromProto(PermissionOuterClass.Permission proto) {
        String node = proto.getNode();
        return new Permission(node);
    }

    private String node;

    public Permission(String node) {
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
