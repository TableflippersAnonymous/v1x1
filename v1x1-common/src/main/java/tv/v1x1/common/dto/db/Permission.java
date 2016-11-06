package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.UDT;

/**
 * Created by naomi on 11/5/2016.
 */
@UDT(name = "permission")
public class Permission {
    private String node;

    public Permission() {
    }

    public Permission(final String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public tv.v1x1.common.dto.core.Permission toCore() {
        return new tv.v1x1.common.dto.core.Permission(node);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Permission that = (Permission) o;

        return node != null ? node.equals(that.node) : that.node == null;

    }

    @Override
    public int hashCode() {
        return node != null ? node.hashCode() : 0;
    }
}
