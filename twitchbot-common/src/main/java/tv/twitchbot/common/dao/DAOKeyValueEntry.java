package tv.twitchbot.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.twitchbot.common.dto.db.KeyValueEntry;

/**
 * Created by naomi on 10/17/2016.
 */
public class DAOKeyValueEntry {
    private final Mapper<KeyValueEntry> mapper;

    public DAOKeyValueEntry(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(KeyValueEntry.class);
    }

    public void put(final KeyValueEntry keyValueEntry) {
        mapper.save(keyValueEntry);
    }

    public KeyValueEntry get(final String name, final byte[] key) {
        return mapper.get(name, key);
    }

    public void delete(final KeyValueEntry keyValueEntry) {
        mapper.delete(keyValueEntry);
    }
}
