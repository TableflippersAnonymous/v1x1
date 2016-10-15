package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by naomi on 10/15/2016.
 */
@Table(name = "TenantUserPermissions")
public class TenantUserPermissions {
    private UUID tenantId;
    private String user;

}
