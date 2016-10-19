package tv.twitchbot.common.i18n;

import tv.twitchbot.common.dto.core.Module;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Josh
 */
public class StaticLanguage extends Language {
    private Map<String, String> strings;

    public StaticLanguage() {
        strings = new HashMap<>();
    }

    @Override
    public String message(Module module, String key, Map<String, Object> parameters) {
        return format(strings.get(getKey(module, key)), parameters);
    }

    @Override
    public void set(Module module, String key, String message) {
        strings.put(getKey(module, key), message);
    }
}
