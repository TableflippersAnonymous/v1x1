package tv.v1x1.common.services.state;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.DiscordChannel;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.db.Platform;

/**
 * Wrappers around all platform-specific DisplayNameServices
 * Definition of reliability:
 * High: All supported platforms will return a good result, regardless of caching status
 * Medium: Platforms may not support this translation, but is OK if the cache is warmed
 * Low: Platforms may not fully support this translation or allow duplicates
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

    /**
     * Get a platform-specifc User ID from a user's display name and channel context
     * Reliability: Low
     * @param context Platform & channel to search on
     * @param displayName search param
     * @return User ID on {@link Platform}
     * @throws NoSuchUserException
     */
    public String getIdFromDisplayName(Channel context, String displayName) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUserIdFromDisplayName(displayName);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getUserIdFromDisplayName(displayName);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    /**
     * Get a channel-specific display name from a platform-specifc User ID and channel context
     * Reliability: High
     * @param context Platform & channel to search on
     * @param id search params
     * @return Display name on {@link Platform} and {@link Channel}
     * @throws NoSuchUserException
     */
    public String getDisplayNameFromId(Channel context, String id) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getDisplayNameFromUserId(id);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getDisplayNameFromUserId(id);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    /**
     * Get a user's username from a platform-specific User ID and channel context
     * Reliability: High
     * @param context Platform & channel to search on
     * @param id search params
     * @return Username on {@link Platform}
     * @throws NoSuchUserException
     */
    public String getUsernameFromId(Channel context, String id) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUsernameFromUserId(id);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getUsernameFromUserId(id);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    /**
     * Get a user's display name from their user name and channel context
     * Reliability: High
     * @param context Platform & channel to search on
     * @param username search params
     * @return Display name on {@link Platform} and {@link Channel}
     * @throws NoSuchUserException
     */
    public String getDisplayNameFromUsername(Channel context, final String username) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getDisplayNameFromUsername(username);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getDisplayNameFromUsername(username);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    /**
     * Get a user's Platform-specific username from their display name
     * Reliability: Low
     * @param context Platform & channel to search on
     * @param displayName search params
     * @return Username for {@link Platform}
     * @throws NoSuchUserException
     */
    public String getUsernameFromDisplayName(final Channel context, final String displayName) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUsernameFromDisplayName(displayName);
        if(context instanceof DiscordChannel)
            throw new UnsupportedOperationException("Display names are not unique on " + context.getPlatform());
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    /**
     * Get a user's Platform-specific ID from their username
     * Reliability: Medium
     * @param context Platform & channel to search on
     * @param username search params
     * @return User ID for {@link Platform}
     * @throws NoSuchUserException
     */
    public String getIdFromUsername(final Channel context, final String username) throws NoSuchUserException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUserIdFromUsername(username);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getUserIdFromUsername(username);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    /**
     * @see DisplayNameService#getIdFromDisplayName(Channel, String)
     */
    public String getIdFromDisplayName(Platform platform, String displayName) throws NoSuchUserException {
        return getIdFromDisplayName(Channel.emptyFromPlatform(platform), displayName);
    }

    /**
     * @see DisplayNameService#getDisplayNameFromId(Channel, String)
     */
    public String getDisplayNameFromId(Platform platform, String id) throws NoSuchUserException {
        return getDisplayNameFromId(Channel.emptyFromPlatform(platform), id);
    }

    /**
     * @see DisplayNameService#getUsernameFromId(Channel, String)
     */
    public String getUsernameFromId(Platform platform, String id) throws NoSuchUserException {
        return getUsernameFromId(Channel.emptyFromPlatform(platform), id);
    }

    /**
     * @see DisplayNameService#getDisplayNameFromUsername(Channel, String)
     */
    public String getDisplayNameFromUsername(final Platform platform, final String username) throws NoSuchUserException {
        return getDisplayNameFromUsername(Channel.emptyFromPlatform(platform), username);
    }

    /**
     * @see DisplayNameService#getUsernameFromDisplayName(Channel, String)
     */
    public String getUsernameFromDisplayName(final Platform platform, final String displayName) throws NoSuchUserException {
        return getUsernameFromDisplayName(Channel.emptyFromPlatform(platform), displayName);
    }

    /**
     * @see DisplayNameService#getIdFromUsername(Channel, String)
     */
    public String getIdFromUsername(final Platform platform, final String username) throws NoSuchUserException {
        return getIdFromUsername(Channel.emptyFromPlatform(platform), username);
    }
}
