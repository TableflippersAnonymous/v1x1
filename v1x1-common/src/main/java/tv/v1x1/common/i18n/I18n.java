package tv.v1x1.common.i18n;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.db.PersistentLanguage;
import tv.v1x1.common.services.persistence.DAOManager;

import java.util.UUID;

/**
 * @author Josh
 */
public class I18n {
    public static final Language DEFAULT_LANGUAGE = new StaticLanguage();
    public static void registerDefault(final Module module, final String key, final String message) {
        DEFAULT_LANGUAGE.set(module, key, message);
    }
    private final DAOManager daoManager;

    public I18n(final DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    public static String getKey(final Module module, final String key) {
        return module.getName() + "." + key;
    }

    public Language getLanguage(final UUID id) {
        if(id == null)
            return DEFAULT_LANGUAGE;
        final PersistentLanguage language = daoManager.getDaoLanguage().get(id);
        return (language != null ? language : DEFAULT_LANGUAGE);
    }

    public Language createLanguage(final UUID parentLanguage) {
        final Language language = new PersistentLanguage(parentLanguage);
        /* Ask DAOLanguage nicely */
        return null;
    }
}