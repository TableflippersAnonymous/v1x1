package tv.v1x1.common.services.state;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.DiscordChannel;
import tv.v1x1.common.dto.core.TwitchChannel;

/**
 * @author Josh
 */
@Singleton
public class DisplayNameService {
    private final TwitchDisplayNameService twitchDisplayNameService;
    private final DiscordDisplayNameService discordDisplayNameService;

    @Inject
    public DisplayNameService(final TwitchDisplayNameService twitchDisplayNameService, final DiscordDisplayNameService discordDisplayNameService) {
        this.twitchDisplayNameService = twitchDisplayNameService;
        this.discordDisplayNameService = discordDisplayNameService;
    }

    public String getIdFromDisplayName(Channel context, String displayName) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUserIdFromDisplayName(displayName);
        if(context instanceof DiscordChannel)
            // TODO: This needs a lot of work to incorporate context.
            return discordDisplayNameService.getUserIdFromDisplayName(displayName);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    public String getDisplayNameFromId(Channel context, String id) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getDisplayNameFromUserId(id);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getDisplayNameFromUserId(id);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    public String getUserFromId(Channel context, String id) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUsernameFromUserId(id);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getUsernameFromUserId(id);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }
}
