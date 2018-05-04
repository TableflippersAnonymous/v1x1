package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ParseType {
    FULL("full"), NONE("none");

    private final String stringVersion;

    ParseType(final String stringVersion) {
        this.stringVersion = stringVersion;
    }

    @JsonValue
    public String getStringVersion() {
        return stringVersion;
    }

    @JsonCreator
    public static ParseType getByString(final String stringVersion) {
        for(final ParseType parseType : ParseType.values())
            if(parseType.getStringVersion().equals(stringVersion))
                return parseType;
        throw new IllegalArgumentException("Unknown ParseType: " + stringVersion);
    }
}
