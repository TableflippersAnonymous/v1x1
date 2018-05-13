package tv.v1x1.common.services.state;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.DiscordChannel;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.db.Platform;

/**
 * @author Josh
 */
@Singleton
public class DisplayNameService {
    private final TwitchDisplayNameService twitchDisplayNameService;
    private final DiscordDisplayNameService discordDisplayNameService;

    @Inject
    public DisplayNameService(final TwitchDisplayNameService twitchDisplayNameService,
                              final DiscordDisplayNameService discordDisplayNameService) {
        this.twitchDisplayNameService = twitchDisplayNameService;
        this.discordDisplayNameService = discordDisplayNameService;
    }

    public String getIdFromDisplayName(Channel context, String displayName) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUserIdFromDisplayName(displayName);
        if(context instanceof DiscordChannel)
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

    public String getDisplayNameFromUsername(Channel context, final String username) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getDisplayNameFromUsername(username);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getDisplayNameFromUsername(username);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    public String getUsernameFromDisplayName(final Channel context, final String displayName) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUsernameFromDisplayName(displayName);
        if(context instanceof DiscordChannel)
            throw new UnsupportedOperationException("Display names are not unique on " + context.getPlatform());
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    public String getIdFromUsername(final Channel context, final String username) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUserIdFromUsername(username);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getUserIdFromUsername(username);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    public String getIdFromDisplayName(Platform platform, String displayName) throws NoSuchUserException {
        return getIdFromDisplayName(Channel.emptyFromPlatform(platform), displayName);
    }

    public String getDisplayNameFromId(Platform platform, String id) throws NoSuchUserException {
        return getDisplayNameFromId(Channel.emptyFromPlatform(platform), id);
    }

    public String getUserFromId(Platform platform, String id) throws NoSuchUserException {
        return getUserFromId(Channel.emptyFromPlatform(platform), id);
    }

    public String getDisplayNameFromUsername(final Platform platform, final String username) throws NoSuchUserException {
        return getDisplayNameFromUsername(Channel.emptyFromPlatform(platform), username);
    }

    public String getUsernameFromDisplayName(final Platform platform, final String displayName) throws NoSuchUserException {
        return getUsernameFromDisplayName(Channel.emptyFromPlatform(platform), displayName);
    }

    public String getIdFromUsername(final Platform platform, final String username) throws NoSuchUserException {
        return getIdFromUsername(Channel.emptyFromPlatform(platform), username);
    }
}
