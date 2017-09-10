package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Created by cobi on 9/10/2017.
 */
public enum MFALevel {
    NONE(0), ELEVATED(1);

    private final int value;

    MFALevel(final int value) {
        this.value = value;
    }

    @JsonValue
    public Integer jsonValue() {
        return this.value;
    }

    @JsonCreator
    public static MFALevel fromJson(final Integer value) {
        for(final MFALevel mfaLevel : values())
            if(Objects.equals(mfaLevel.jsonValue(), value))
                return mfaLevel;
        throw new IllegalArgumentException("value=" + value + " not recognized");
    }
}
