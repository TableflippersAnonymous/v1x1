package tv.v1x1.common.services.discord.dto.audit;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Created by naomi on 9/16/2017.
 */
public class StringToIntegerConverter extends StdConverter<String, Integer> {
    @Override
    public Integer convert(final String value) {
        return Integer.valueOf(value);
    }
}
