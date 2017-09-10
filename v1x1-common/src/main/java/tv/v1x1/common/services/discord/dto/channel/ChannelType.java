package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Created by naomi on 9/10/2017.
 */
public enum ChannelType {
    GUILD_TEXT(0), DM(1), GUILD_VOICE(2), GROUP_DM(3), GUILD_CATEGORY(4);

    private final int value;

    ChannelType(final int value) {
        this.value = value;
    }

    @JsonValue
    public Integer jsonValue() {
        return this.value;
    }

    @JsonCreator
    public static ChannelType fromJson(final Integer value) {
        for(final ChannelType channelType : values())
            if(Objects.equals(channelType.jsonValue(), value))
                return channelType;
        throw new IllegalArgumentException("value=" + value + " not recognized");
    }
}
