package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.db.GlobalConfiguration;

/**
 * Created by cobi on 10/17/2016.
 */
@Singleton
public class DAOGlobalConfiguration {
    private final Mapper<GlobalConfiguration> mapper;

    @Inject
    public DAOGlobalConfiguration(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(GlobalConfiguration.class);
    }

    public GlobalConfiguration get(final Module module) {
        return mapper.get(module.getName());
    }

    public void put(final GlobalConfiguration globalConfiguration) {
        mapper.save(globalConfiguration);
    }
}
