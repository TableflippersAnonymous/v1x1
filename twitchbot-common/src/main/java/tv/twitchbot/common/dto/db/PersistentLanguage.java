package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.i18n.I18n;
import tv.twitchbot.common.i18n.Language;

import java.util.Map;
import java.util.UUID;

/**
 * @author Josh
 */
@Table(name = "persistent_language")
public class PersistentLanguage extends Language {
    @PartitionKey
    private final UUID id;
    @Column(name = "parent_id")
    private final UUID parentId;
    private final String name;
    private Map<String, String> strings;
    @Transient
    private Language parent;
    public PersistentLanguage(final UUID parentLanguage) {
        id = UUID.randomUUID();
        parentId = parentLanguage;
        name = null;
    }

    public String message(final Module module, final String key, final Map<String, Object> parameters) {
        if(strings.containsKey(getKey(module, key)))
            return format(strings.get(getKey(module, key)), parameters);
        if(parent == null) {
            if(parentId == null) {
                parent = I18n.DEFAULT_LANGUAGE;
            } else {
                /* Ask the DAOLanguage nicely for parentId */
            }
        }
        return parent.message(module, key, parameters);
    }

    @Override
    public void set(final Module module, final String key, final String message) {
        // XXX: Stub
    }
}