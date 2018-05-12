package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import com.google.inject.Inject;
import tv.v1x1.common.dto.db.I18nDefinition;

public class DAOI18nDefinition {
    private final I18nDefinitionAccessor accessor;
    private final Mapper<I18nDefinition> mapper;

    @Accessor
    public interface I18nDefinitionAccessor {
        @Query("SELECT * FROM i18n_module_definition")
        Result<I18nDefinition> all();
    }

    @Inject
    public DAOI18nDefinition(final MappingManager mappingManager) {
        accessor = mappingManager.createAccessor(I18nDefinitionAccessor.class);
        mapper = mappingManager.mapper(I18nDefinition.class);
    }

    public Iterable<I18nDefinition> getAll() {
        return accessor.all();
    }

    public I18nDefinition get(final String module) {
        return mapper.get(module);
    }

    public void put(final I18nDefinition i18nDefinition) {
        final I18nDefinition oldI18nDefinition = get(i18nDefinition.getName());
        if(oldI18nDefinition == null || oldI18nDefinition.getVersion() <= i18nDefinition.getVersion())
            mapper.save(i18nDefinition);
    }
}
