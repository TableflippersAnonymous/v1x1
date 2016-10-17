package tv.twitchbot.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.twitchbot.common.dto.db.KeyValueEntry;

/**
 * Created by naomi on 10/17/2016.
 */
public class DAOKeyValueEntry {
    private Mapper<KeyValueEntry> mapper;

    public DAOKeyValueEntry(MappingManager mappingManager) {
        mapper = mappingManager.mapper(KeyValueEntry.class);
    }

    public void put(KeyValueEntry keyValueEntry) {
        mapper.save(keyValueEntry);
    }

    public KeyValueEntry get(String name, byte[] key) {
        return mapper.get(name, key);
    }

    public void delete(KeyValueEntry keyValueEntry) {
        mapper.delete(keyValueEntry);
    }
}
