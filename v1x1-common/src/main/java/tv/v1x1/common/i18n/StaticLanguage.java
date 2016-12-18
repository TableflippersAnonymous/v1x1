package tv.v1x1.common.i18n;

import tv.v1x1.common.dto.core.Module;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Josh
 */
public class StaticLanguage extends Language {
    private final Map<String, String> strings;

    public StaticLanguage() {
        strings = new HashMap<>();
    }

    @Override
    public String message(final Module module, final String key, final Map<String, Object> parameters) {
        String template = strings.get(getKey(module, key));
        if(template == null) template = strings.get("_GLOBAL_".concat(key));
        return format((template == null ? getKey(module, key) : template), parameters);
    }

    @Override
    public void set(final Module module, final String key, final String message) {
        strings.put(getKey(module, key), message);
    }
}
