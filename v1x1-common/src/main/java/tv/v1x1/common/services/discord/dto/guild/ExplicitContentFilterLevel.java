package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Created by naomi on 9/10/2017.
 */
public enum ExplicitContentFilterLevel {
    DISABLED(0), MEMBERS_WITHOUT_ROLES(1), ALL_MEMBERS(2);

    private final int value;

    ExplicitContentFilterLevel(final int value) {
        this.value = value;
    }

    @JsonValue
    public Integer jsonValue() {
        return this.value;
    }

    @JsonCreator
    public static ExplicitContentFilterLevel fromJson(final Integer value) {
        for(final ExplicitContentFilterLevel explicitContentFilterLevel : values())
            if(Objects.equals(explicitContentFilterLevel.jsonValue(), value))
                return explicitContentFilterLevel;
        throw new IllegalArgumentException("value=" + value + " not recognized");
    }
}
