package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * Created by naomi on 9/10/2017.
 */
public enum GameType {
    UNKNOWN(-1), GAME(0), STREAMING(1), LISTENING(2), WATCHING(3);

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final int value;

    GameType(final int value) {
        this.value = value;
    }

    @JsonValue
    public Integer jsonValue() {
        return this.value;
    }

    @JsonCreator
    public static GameType fromJson(final Integer value) {
        for(final GameType gameType : values())
            if(Objects.equals(gameType.jsonValue(), value))
                return gameType;
        LOG.error("Unknown GameType: ", new IllegalArgumentException("value=" + value + " not recognized"));
        return UNKNOWN;
    }
}
