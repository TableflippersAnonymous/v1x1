package tv.v1x1.modules.channel.factoids.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

import java.util.List;
import java.util.UUID;

/**
 * @author Josh
 */
public class DAOFactoid {
    private final Mapper<Factoid> mapper;
    private final FactoidAccessor accessor;

    @Accessor
    public interface FactoidAccessor {
        @Query("SELECT * FROM factoid WHERE tenant_id = ?")
        Result<Factoid> all(final UUID tenantId);
    }

    public DAOFactoid(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(Factoid.class);
        accessor = mappingManager.createAccessor(FactoidAccessor.class);
    }

    /**
     * get the factoid for tenant
     * @param tenantId
     * @param id
     * @return
     */
    public Factoid getById(final UUID tenantId, final String id) {
        return mapper.get(tenantId, id.toLowerCase());
    }

    /**
     * get the factoid for a tenant, traversing aliases if required
     * @param tenantId
     * @param id
     * @return
     */
    public Factoid chaseDownById(final UUID tenantId, final String id) {
        final Factoid factoid = getById(tenantId, id);
        if(factoid == null) return null;
        if(factoid.isAlias()) return chaseDownById(tenantId, factoid.getData());
        return factoid;
    }

    /**
     * Save/overwrite a factoid
     * @param fact
     */
    public void add(final Factoid fact) {
        mapper.save(fact);
    }

    public Factoid del(final UUID tenantId, final String id) {
        final Factoid fact = getById(tenantId, id);
        if(fact == null) return null;
        mapper.delete(fact);
        return fact;
    }

    public List<Factoid> all(final UUID tenantId) {
        return accessor.all(tenantId).all();
    }
}
