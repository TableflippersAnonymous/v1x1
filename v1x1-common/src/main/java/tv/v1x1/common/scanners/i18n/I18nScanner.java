package tv.v1x1.common.scanners.i18n;

import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.scanners.ClassScanner;

public class I18nScanner extends ClassScanner {
    public I18nScanner() {
    }

    public static void scanClass(final Module<?, ?> module) {
        final I18nDefaults i18nDefaults = getAnnotation(module.getClass(), I18nDefaults.class);
        if(i18nDefaults == null)
            return;
        for(final I18nDefault i18nDefault : i18nDefaults.value())
            I18n.registerDefault(module.toDto(), i18nDefault.key(), i18nDefault.message());
    }
}
