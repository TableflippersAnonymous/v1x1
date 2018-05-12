package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import com.google.inject.Inject;
import tv.v1x1.common.dto.db.PermissionDefinition;

public class DAOPermissionDefinition {
    private final PermissionDefinitionAccessor accessor;
    private final Mapper<PermissionDefinition> mapper;

    @Accessor
    public interface PermissionDefinitionAccessor {
        @Query("SELECT * FROM i18n_module_definition")
        Result<PermissionDefinition> all();
    }

    @Inject
    public DAOPermissionDefinition(final MappingManager mappingManager) {
        accessor = mappingManager.createAccessor(PermissionDefinitionAccessor.class);
        mapper = mappingManager.mapper(PermissionDefinition.class);
    }

    public Iterable<PermissionDefinition> getAll() {
        return accessor.all();
    }

    public PermissionDefinition get(final String module) {
        return mapper.get(module);
    }

    public void put(final PermissionDefinition permissionDefinition) {
        final PermissionDefinition oldPermissionDefinition = get(permissionDefinition.getName());
        if(oldPermissionDefinition == null || oldPermissionDefinition.getVersion() <= permissionDefinition.getVersion())
            mapper.save(permissionDefinition);
    }
}
