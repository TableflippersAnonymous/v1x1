package tv.v1x1.common.services.discord.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Created by cobi on 9/10/2017.
 */
public enum GameType {
    GAME(0), STREAMING(1), LISTENING(2);

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
        throw new IllegalArgumentException("value=" + value + " not recognized");
    }
}
