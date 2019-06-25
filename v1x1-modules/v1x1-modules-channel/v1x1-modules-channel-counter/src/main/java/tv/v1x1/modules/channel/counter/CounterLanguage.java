package tv.v1x1.modules.channel.counter;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.i18n.Language;

import java.util.Map;

/**
 * Created by Josh on 2019-06-25
 * This is a hack to use Language.format, which is protected.
 * I should feel bad that I did this instead of actually implement custom languages like I wanted to.
 *
 * Oh well.
 */
public class CounterLanguage extends Language {
    public static String DEFAULT_SET_MESSAGE = "%target% is now %count%";
    @Override
    public String message(final Module module, final String key, final Map<String, Object> parameters) {
        return null;
    }

    @Override
    public void set(final Module module, final String key, final String message) {
    }

    public String format(final String template, final Map<String, Object> parameters) {
        return super.format(template, parameters);
    }
}
