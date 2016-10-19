package tv.twitchbot.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.twitchbot.common.dto.db.PersistentLanguage;

import java.util.UUID;

/**
 * @author Josh
 */
public class DAOLanguage {
    private Mapper<PersistentLanguage> mapper;

    public DAOLanguage(MappingManager mappingManager) {
        mapper = mappingManager.mapper(PersistentLanguage.class);
    }

    public void put(PersistentLanguage language) {
        mapper.save(language);
    }

    public PersistentLanguage get(UUID id) {
        return mapper.get(id);
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }
}
