package tv.v1x1.common.services.state;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.TwitchChannel;

/**
 * @author Josh
 */
@Singleton
public class DisplayNameService {
    private final TwitchDisplayNameService twitchDisplayNameService;

    @Inject
    public DisplayNameService(final TwitchDisplayNameService twitchDisplayNameService) {
        this.twitchDisplayNameService = twitchDisplayNameService;
    }

    public String getIdFromDisplayName(Channel context, String displayName) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUserIdFromDisplayName(displayName);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    public String getDisplayNameFromId(Channel context, String id) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getDisplayNameFromUserId(id);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }
}
