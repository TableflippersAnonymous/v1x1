package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Created by cobi on 9/16/2017.
 */
public class IntegerToStringConverter extends StdConverter<Integer, String> {
    @Override
    public String convert(final Integer value) {
        return String.valueOf(value);
    }
}
