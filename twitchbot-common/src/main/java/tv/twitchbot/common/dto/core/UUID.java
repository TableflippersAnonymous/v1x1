package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.UUIDOuterClass;

/**
 * Created by naomi on 10/6/2016.
 */
public class UUID {
    public static UUID fromProto(UUIDOuterClass.UUID uuid) {
        java.util.UUID realUuid = new java.util.UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
        return new UUID(realUuid);
    }

    private java.util.UUID value;

    public UUID(java.util.UUID value) {
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
}
