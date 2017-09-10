package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by cobi on 9/10/2017.
 */
public enum Status {
    IDLE, DND, ONLINE, OFFLINE, INVISIBLE;

    @JsonCreator
    public static Status fromJson(final String jsonValue) {
        return valueOf(jsonValue.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
