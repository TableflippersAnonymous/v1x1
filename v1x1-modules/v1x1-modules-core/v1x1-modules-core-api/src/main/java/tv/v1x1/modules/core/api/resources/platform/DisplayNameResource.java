package tv.v1x1.modules.core.api.resources.platform;

import com.google.inject.Inject;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.state.NoSuchUserException;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.dto.users.User;
import tv.v1x1.modules.core.api.api.DisplayNameRecord;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by naomi on 3/5/2017.
 */
@Path("/api/v1/platform/display-name")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisplayNameResource {
    private final TwitchDisplayNameService twitchDisplayNameService;
    private final DAOGlobalUser daoGlobalUser;

    @Inject
    public DisplayNameResource(final TwitchDisplayNameService twitchDisplayNameService, final DAOManager daoManager) {
        this.twitchDisplayNameService = twitchDisplayNameService;
        this.daoGlobalUser = daoManager.getDaoGlobalUser();
    }

    @Path("/twitch/user/by-username/{username}")
    @GET
    public DisplayNameRecord getTwitchUserByUsername(@PathParam("username") final String username) {
        try {
            return displayNameRecordFromTwitchUser(twitchDisplayNameService.getUserByUsername(username));
        } catch (final NoSuchUserException e) {
            throw new NotFoundException();
        }
    }

    @Path("/twitch/user/by-display-name/{display_name}")
    @GET
    public DisplayNameRecord getTwitchUserByDisplayName(@PathParam("display_name") final String displayName) {
        try {
            return displayNameRecordFromTwitchUser(twitchDisplayNameService.getUserByDisplayName(displayName));
        } catch (final NoSuchUserException e) {
            throw new NotFoundException();
        }
    }

    @Path("/twitch/user/by-id/{id}")
    @GET
    public DisplayNameRecord getTwitchUserById(@PathParam("id") final String id) {
        try {
            return displayNameRecordFromTwitchUser(twitchDisplayNameService.getUserByUserId(id));
        } catch (final NoSuchUserException e) {
            throw new NotFoundException();
        }
    }

    private DisplayNameRecord displayNameRecordFromTwitchUser(final User user) {
        return new DisplayNameRecord(
                Platform.TWITCH,
                String.valueOf(user.getId()),
                user.getName(),
                user.getDisplayName(),
                daoGlobalUser.getOrCreate(Platform.TWITCH, String.valueOf(user.getId()), user.getDisplayName()).getId()
        );
    }
}
