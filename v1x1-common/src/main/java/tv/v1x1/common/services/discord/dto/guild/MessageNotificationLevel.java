package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Created by naomi on 9/10/2017.
 */
public enum MessageNotificationLevel {
    ALL_MESSAGES(0), ONLY_MENTIONS(1);

    private final int value;

    MessageNotificationLevel(final int value) {
        this.value = value;
    }

    @JsonValue
    public Integer jsonValue() {
        return this.value;
    }

    @JsonCreator
    public static MessageNotificationLevel fromJson(final Integer value) {
        for(final MessageNotificationLevel messageNotificationLevel : values())
            if(Objects.equals(messageNotificationLevel.jsonValue(), value))
                return messageNotificationLevel;
        throw new IllegalArgumentException("value=" + value + " not recognized");
    }
}
