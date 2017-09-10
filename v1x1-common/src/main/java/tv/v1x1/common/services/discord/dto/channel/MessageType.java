package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Created by naomi on 9/10/2017.
 */
public enum MessageType {
    DEFAULT(0), RECIPIENT_ADD(1), RECIPIENT_REMOVE(2), CALL(3), CHANNEL_NAME_CHANGE(4), CHANNEL_ICON_CHANGE(5),
    CHANNEL_PINNED_MESSAGE(6), GUILD_MEMBER_JOIN(7);

    private final int value;

    MessageType(final int value) {
        this.value = value;
    }

    @JsonValue
    public Integer jsonValue() {
        return this.value;
    }

    @JsonCreator
    public static MessageType fromJson(final Integer value) {
        for(final MessageType messageType : values())
            if(Objects.equals(messageType.jsonValue(), value))
                return messageType;
        throw new IllegalArgumentException("value=" + value + " not recognized");
    }
}
