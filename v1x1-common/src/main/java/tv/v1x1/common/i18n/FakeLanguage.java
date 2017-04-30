package tv.v1x1.common.i18n;

import tv.v1x1.common.dto.core.Module;

import java.util.Map;

/**
 * @author Josh
 * A Language that uses the key as a template string
 * That way you can use the I18n formatter for user-defined strings
 */
public class FakeLanguage extends Language {
    @Override
    public String message(final Module module, final String key, final Map<String, Object> parameters) {
        return format(key, parameters);
    }

    @Override
    public void set(final Module module, final String key, final String message) {

    }
}
