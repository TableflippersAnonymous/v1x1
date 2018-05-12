package tv.v1x1.common.scanners.i18n;

import tv.v1x1.common.dao.DAOI18nDefinition;
import tv.v1x1.common.dto.db.I18nDefinition;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.scanners.ClassScanner;

import java.util.Arrays;
import java.util.stream.Collectors;

public class I18nScanner extends ClassScanner {
    public static void scanClass(final Module<?, ?> module) {
        final I18nDefaults i18nDefaults = getAnnotation(module.getClass(), I18nDefaults.class);
        if(i18nDefaults == null)
            return;
        for(final I18nDefault i18nDefault : i18nDefaults.value())
            I18n.registerDefault(module.toDto(), i18nDefault.key(), i18nDefault.message());
        module.getInjector().getInstance(DAOI18nDefinition.class).put(
                new I18nDefinition(module.getName(), i18nDefaults.version(),
                        Arrays.stream(i18nDefaults.value()).map(
                                i18nDefault -> new I18nDefinition.I18nEntry(
                                        i18nDefault.key(), i18nDefault.message(),
                                        i18nDefault.displayName(), i18nDefault.description()
                                )
                        ).collect(Collectors.toList())
                )
        );
    }
}
