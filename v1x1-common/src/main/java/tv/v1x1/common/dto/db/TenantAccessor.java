package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

import java.util.UUID;

/**
 * Created by naomi on 10/15/2016.
 */
@Accessor
public interface TenantAccessor {
    @Query("select * from Tenant where id = ?")
    Tenant getById(UUID id);
}
