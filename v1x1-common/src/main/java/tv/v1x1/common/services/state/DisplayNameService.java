package tv.v1x1.common.services.state;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.DiscordChannel;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.util.validation.DiscordValidator;

import java.lang.invoke.MethodHandles;

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
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


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
     * @throws NoSuchTargetException
     */
    public String getIdFromDisplayName(Channel context, String displayName) throws NoSuchTargetException {
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
     * @throws NoSuchTargetException
     */
    public String getDisplayNameFromId(Channel context, String id) throws NoSuchTargetException {
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
     * @throws NoSuchTargetException
     */
    public String getUsernameFromId(Channel context, String id) throws NoSuchTargetException {
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
     * @throws NoSuchTargetException
     */
    public String getDisplayNameFromUsername(Channel context, final String username) throws NoSuchTargetException {
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
     * @throws NoSuchTargetException
     */
    public String getUsernameFromDisplayName(final Channel context, final String displayName) throws NoSuchTargetException {
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
     * @throws NoSuchTargetException
     */
    public String getIdFromUsername(final Channel context, final String username) throws NoSuchTargetException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getUserIdFromUsername(username);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getUserIdFromUsername(username);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    /**
     * Get a channel's display name from its Platform-specific ID
     * Reliability: High
     * @param context
     * @param id
     * @return
     * @throws NoSuchTargetException
     */
    public String getChannelDisplayNameFromId(final Channel context, final String id) throws NoSuchTargetException {
        if(context instanceof TwitchChannel)
            return twitchDisplayNameService.getDisplayNameFromChannelId(id);
        if(context instanceof DiscordChannel)
            return discordDisplayNameService.getDisplayNameFromChannelId(id);
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    /**
     * Get a {@link User} based on their Platform-specific mention name
     * Reliability: High
     * @param context Platform & channel to search on
     * @param mention search params
     * @return User the user that was mentioned
     * @throws NoSuchTargetException
     */
    public String getUserIdFromMention(final Channel context, String mention) throws NoSuchTargetException {
        if(context instanceof TwitchChannel) {
            if(mention.startsWith("@"))
                mention = mention.substring(1, mention.length());
            return String.valueOf(twitchDisplayNameService.getUserByDisplayName(mention).getId());
        }
        if(context instanceof DiscordChannel) {
            // https://discordapp.com/developers/docs/reference#message-formatting
            // Discord user mentions come in two flavors -- <@UID> and <@!UID>
            if(mention.length() > 3) {
                mention = mention.substring(2, mention.length() - 1); // Strip leading <@ and trailing >
                if(mention.startsWith("!"))
                    mention = mention.substring(1, mention.length()); // strip leading !
                LOG.trace("getUserIdFromMention(): looking for " + mention);
                if(DiscordValidator.isSnowflake(mention))
                    return discordDisplayNameService.getUserByUserId(mention).getId();
            }
            throw new NoSuchTargetException("Invalid snowflake");
        }
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    public String getChannelIdFromMention(final Channel context, String mention) throws NoSuchTargetException {
        if(context instanceof TwitchChannel) {
            if(mention.startsWith("#"))
                mention = mention.substring(1, mention.length());
            return String.valueOf(twitchDisplayNameService.getChannelByDisplayName(mention).getId());
        }
        if(context instanceof DiscordChannel) {
            // https://discordapp.com/developers/docs/reference#message-formatting
            // Discord Channel mentions look like this: <#CID>
            if(mention.length() > 3) {
                mention = mention.substring(2, mention.length() - 1);
                LOG.trace("getChannelIdFromMention(): looking for " + mention);
                if(DiscordValidator.isSnowflake(mention))
                    return discordDisplayNameService.getChannelByChannelId(mention).getId();
            }
            throw new NoSuchTargetException("Invalid snowflake");
        }
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    public String getChannelGroupDisplayNameFromId(final Platform platform, final String channelGroupId) throws NoSuchTargetException {
        switch(platform) {
            case TWITCH:
                return getDisplayNameFromId(platform, channelGroupId);
            case DISCORD:
                return discordDisplayNameService.getGuildNameByGuildId(channelGroupId);
            default:
                throw new IllegalArgumentException("Don't know how to handle " + platform.stylize() + " display names yet");
        }
    }

    /**
     * @see DisplayNameService#getIdFromDisplayName(Channel, String)
     */
    public String getIdFromDisplayName(Platform platform, String displayName) throws NoSuchTargetException {
        return getIdFromDisplayName(Channel.emptyFromPlatform(platform), displayName);
    }

    /**
     * @see DisplayNameService#getDisplayNameFromId(Channel, String)
     */
    public String getDisplayNameFromId(Platform platform, String id) throws NoSuchTargetException {
        return getDisplayNameFromId(Channel.emptyFromPlatform(platform), id);
    }

    /**
     * @see DisplayNameService#getUsernameFromId(Channel, String)
     */
    public String getUsernameFromId(Platform platform, String id) throws NoSuchTargetException {
        return getUsernameFromId(Channel.emptyFromPlatform(platform), id);
    }

    /**
     * @see DisplayNameService#getDisplayNameFromUsername(Channel, String)
     */
    public String getDisplayNameFromUsername(final Platform platform, final String username) throws NoSuchTargetException {
        return getDisplayNameFromUsername(Channel.emptyFromPlatform(platform), username);
    }

    /**
     * @see DisplayNameService#getUsernameFromDisplayName(Channel, String)
     */
    public String getUsernameFromDisplayName(final Platform platform, final String displayName) throws NoSuchTargetException {
        return getUsernameFromDisplayName(Channel.emptyFromPlatform(platform), displayName);
    }

    /**
     * @see DisplayNameService#getIdFromUsername(Channel, String)
     */
    public String getIdFromUsername(final Platform platform, final String username) throws NoSuchTargetException {
        return getIdFromUsername(Channel.emptyFromPlatform(platform), username);
    }

    /**
     * @see DisplayNameService#getChannelDisplayNameFromId(Channel, String)
     */
    public String getChannelDisplayNameFromId(Platform platform, String id) throws NoSuchTargetException {
        return getChannelDisplayNameFromId(Channel.emptyFromPlatform(platform), id);
    }
}
