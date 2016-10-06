package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.UserOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class Permission {
    public static Permission fromProto(UserOuterClass.User.Permission permission) {
        String node = permission.getNode();
        return new Permission(node);
    }

    private String node;

    public Permission(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        if (node != null ? !node.equals(that.node) : that.node != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return node != null ? node.hashCode() : 0;
    }

    public UserOuterClass.User.Permission toProto() {
        return UserOuterClass.User.Permission.newBuilder()
                .setNode(node)
                .build();
    }
}
