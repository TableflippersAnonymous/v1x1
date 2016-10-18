package tv.twitchbot.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.db.GlobalConfiguration;

/**
 * Created by naomi on 10/17/2016.
 */
public class DAOGlobalConfiguration {
    private Mapper<GlobalConfiguration> mapper;

    public DAOGlobalConfiguration(MappingManager mappingManager) {
        mapper = mappingManager.mapper(GlobalConfiguration.class);
    }

    public GlobalConfiguration get(Module module) {
        return mapper.get(module.getName());
    }

    public void put(GlobalConfiguration globalConfiguration) {
        mapper.save(globalConfiguration);
    }
}
