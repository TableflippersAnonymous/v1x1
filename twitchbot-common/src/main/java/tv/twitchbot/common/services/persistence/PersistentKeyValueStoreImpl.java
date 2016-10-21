package tv.twitchbot.common.services.persistence;

import tv.twitchbot.common.dao.DAOKeyValueEntry;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.db.KeyValueEntry;

/**
 * Created by naomi on 10/17/2016.
 */
public class PersistentKeyValueStoreImpl implements KeyValueStore {
    private final DAOKeyValueEntry daoKeyValueEntry;
    private final String name;

    public PersistentKeyValueStoreImpl(final DAOKeyValueEntry daoKeyValueEntry, final String name) {
        this.daoKeyValueEntry = daoKeyValueEntry;
        this.name = name;
    }

    public PersistentKeyValueStoreImpl(final DAOKeyValueEntry daoKeyValueEntry, final Module module) {
        this(daoKeyValueEntry, "Module|" + module.getName());
    }

    public PersistentKeyValueStoreImpl(final DAOKeyValueEntry daoKeyValueEntry) {
        this(daoKeyValueEntry, "__GLOBAL__");
    }

    @Override
    public void put(final byte[] key, final byte[] value) {
        daoKeyValueEntry.put(new KeyValueEntry(name, key, value));
    }

    @Override
    public byte[] get(final byte[] key) {
        return daoKeyValueEntry.get(name, key).getValue();
    }

    @Override
    public void delete(final byte[] key) {
        daoKeyValueEntry.delete(new KeyValueEntry(name, key, null));
    }
}
