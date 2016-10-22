package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.proto.core.UUIDOuterClass;

/**
 * Represents a {@link java.util.UUID}.
 * Don't know what else you expected.
 * @author Naomi
 */
public class UUID {
    public static UUID fromProto(final UUIDOuterClass.UUID uuid) {
        final java.util.UUID realUuid = new java.util.UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
        return new UUID(realUuid);
    }

    private final java.util.UUID value;

    public UUID(final java.util.UUID value) {
        this.value = value;
    }

    public java.util.UUID getValue() {
        return value;
    }

    public UUIDOuterClass.UUID toProto() {
        return UUIDOuterClass.UUID.newBuilder()
                .setMostSignificantBits(value.getMostSignificantBits())
                .setLeastSignificantBits(value.getLeastSignificantBits())
                .build();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UUID uuid = (UUID) o;

        return value != null ? value.equals(uuid.value) : uuid.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
