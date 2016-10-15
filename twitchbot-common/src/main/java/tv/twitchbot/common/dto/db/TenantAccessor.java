package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

import java.util.UUID;

/**
 * Created by cobi on 10/15/2016.
 */
@Accessor
public interface TenantAccessor {
    @Query("select * from Tenant where id = ?")
    Tenant getById(UUID id);
}
