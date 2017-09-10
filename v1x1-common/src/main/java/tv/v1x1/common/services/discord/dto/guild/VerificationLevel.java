package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Created by cobi on 9/10/2017.
 */
public enum VerificationLevel {
    NONE(0), LOW(1), MEDIUM(2), HIGH(3), VERY_HIGH(4);

    private final int value;

    VerificationLevel(final int value) {
        this.value = value;
    }

    @JsonValue
    public Integer jsonValue() {
        return this.value;
    }

    @JsonCreator
    public static VerificationLevel fromJson(final Integer value) {
        for(final VerificationLevel verificationLevel : values())
            if(Objects.equals(verificationLevel.jsonValue(), value))
                return verificationLevel;
        throw new IllegalArgumentException("value=" + value + " not recognized");
    }
}
