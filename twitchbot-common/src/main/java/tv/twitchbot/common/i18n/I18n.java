package tv.twitchbot.common.i18n;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.db.PersistentLanguage;
import tv.twitchbot.common.services.persistence.DAOManager;

import java.util.UUID;

/**
 * @author Josh
 */
public class I18n {
    public static final Language DEFAULT_LANGUAGE = new StaticLanguage();
    public static void registerDefault(Module module, String key, String message) {
        DEFAULT_LANGUAGE.set(module, key, message);
    }
    private DAOManager daoManager;

    public I18n(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    public static String getKey(Module module, String key) {
        return module.getName() + "." + key;
    }

    public Language getLanguage(UUID id) {
        if(id == null)
            return DEFAULT_LANGUAGE;
        PersistentLanguage language = daoManager.getDaoLanguage().get(id);
        return (language != null ? language : DEFAULT_LANGUAGE);
    }

    public Language createLanguage(UUID parentLanguage) {
        Language language = new PersistentLanguage(parentLanguage);
        /* Ask DAOLanguage nicely */
        return null;
    }
}