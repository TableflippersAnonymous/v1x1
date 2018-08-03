package tv.v1x1.common.services.twitch.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dto.db.Platform;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortUser {
    @JsonProperty("_id")
    private long id;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty
    private String name;

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

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public tv.v1x1.common.dto.core.User toCore(final DAOGlobalUser daoGlobalUser) {
        // Like 93% sure this chain won't have a null
        return daoGlobalUser.getOrCreate(Platform.TWITCH,
                String.valueOf(getId()),
                getDisplayName()).toCore().getUser(Platform.TWITCH, String.valueOf(getId())).orElse(null);
    }
}
