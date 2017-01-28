package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.db.ThirdPartyCredential;

/**
 * Created by cobi on 11/12/2016.
 */
@Singleton
public class DAOThirdPartyCredential {
    private final Mapper<ThirdPartyCredential> mapper;

    @Inject
    public DAOThirdPartyCredential(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(ThirdPartyCredential.class);
    }

    public void put(final ThirdPartyCredential thirdPartyCredential) {
        mapper.save(thirdPartyCredential);
    }

    public ThirdPartyCredential get(final String name) {
        return mapper.get(name);
    }

    public void delete(final ThirdPartyCredential thirdPartyCredential) {
        mapper.delete(thirdPartyCredential);
    }
}
