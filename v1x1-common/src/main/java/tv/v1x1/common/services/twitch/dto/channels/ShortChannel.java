package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.db.Platform;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortChannel {
    @JsonProperty("_id")
    private long id;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty
    private String name;

    public ShortChannel() {
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Channel toCore(final DAOTenant daoTenant) {
        final tv.v1x1.common.dto.db.Tenant dbTenant = daoTenant.getByChannel(Platform.TWITCH, String.valueOf(getId()) + ":main");
        if(dbTenant == null)
            throw new IllegalArgumentException("Have a ShortChannel that's not part of a tenant");
        final Tenant tenant = dbTenant.toCore(daoTenant);
        return tenant.getChannel(Platform.TWITCH, String.valueOf(getId()), String.valueOf(getId()) + ":main").orElse(null);
    }
}
