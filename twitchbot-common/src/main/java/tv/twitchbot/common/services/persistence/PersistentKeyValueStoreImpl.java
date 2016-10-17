package tv.twitchbot.common.services.persistence;

import tv.twitchbot.common.dao.DAOKeyValueEntry;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.db.KeyValueEntry;

/**
 * Created by cobi on 10/17/2016.
 */
public class PersistentKeyValueStoreImpl implements KeyValueStore {
    private DAOKeyValueEntry daoKeyValueEntry;
    private String name;

    public PersistentKeyValueStoreImpl(DAOKeyValueEntry daoKeyValueEntry, String name) {
        this.daoKeyValueEntry = daoKeyValueEntry;
        this.name = name;
    }

    public PersistentKeyValueStoreImpl(DAOKeyValueEntry daoKeyValueEntry, Module module) {
        this(daoKeyValueEntry, "Module|" + module.getName());
    }

    public PersistentKeyValueStoreImpl(DAOKeyValueEntry daoKeyValueEntry) {
        this(daoKeyValueEntry, "__GLOBAL__");
    }

    @Override
    public void put(byte[] key, byte[] value) {
        daoKeyValueEntry.put(new KeyValueEntry(name, key, value));
    }

    @Override
    public byte[] get(byte[] key) {
        return daoKeyValueEntry.get(name, key).getValue();
    }

    @Override
    public void delete(byte[] key) {
        daoKeyValueEntry.delete(new KeyValueEntry(name, key, null));
    }
}
