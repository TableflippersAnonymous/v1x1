package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.db.GlobalConfigurationDefinition;
import tv.v1x1.common.dto.db.UserConfigurationDefinition;

/**
 * Created by naomi on 10/24/2016.
 */
@Singleton
public class DAOConfigurationDefinition {
    private final Mapper<GlobalConfigurationDefinition> globalMapper;
    private final Mapper<UserConfigurationDefinition> userMapper;
    private final ConfigurationDefinitionAccessor accessor;

    @Accessor
    public interface ConfigurationDefinitionAccessor {
        @Query("SELECT * FROM global_configuration_definition")
        Result<GlobalConfigurationDefinition> allGlobal();

        @Query("SELECT * FROM user_configuration_definition")
        Result<UserConfigurationDefinition> allUser();
    }

    @Inject
    public DAOConfigurationDefinition(final MappingManager mappingManager) {
        globalMapper = mappingManager.mapper(GlobalConfigurationDefinition.class);
        userMapper = mappingManager.mapper(UserConfigurationDefinition.class);
        accessor = mappingManager.createAccessor(ConfigurationDefinitionAccessor.class);
    }

    public Iterable<GlobalConfigurationDefinition> getAllGlobal() {
        return accessor.allGlobal();
    }

    public Iterable<UserConfigurationDefinition> getAllUser() {
        return accessor.allUser();
    }

    public GlobalConfigurationDefinition getGlobal(final String module) {
        return globalMapper.get(module);
    }

    public UserConfigurationDefinition getUser(final String module) {
        return userMapper.get(module);
    }

    public void put(final GlobalConfigurationDefinition globalConfigurationDefinition) {
        final GlobalConfigurationDefinition oldGlobalConfigurationDefinition = getGlobal(globalConfigurationDefinition.getName());
        if(oldGlobalConfigurationDefinition == null || oldGlobalConfigurationDefinition.getVersion() <= globalConfigurationDefinition.getVersion())
            globalMapper.save(globalConfigurationDefinition);
    }

    public void put(final UserConfigurationDefinition userConfigurationDefinition) {
        final UserConfigurationDefinition oldUserConfigurationDefinition = getUser(userConfigurationDefinition.getName());
        if(oldUserConfigurationDefinition == null || oldUserConfigurationDefinition.getVersion() <= userConfigurationDefinition.getVersion())
            userMapper.save(userConfigurationDefinition);
    }
}
