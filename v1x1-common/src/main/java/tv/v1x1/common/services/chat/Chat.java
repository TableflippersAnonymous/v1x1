package tv.v1x1.common.services.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.DiscordChannel;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.i18n.Language;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.rpc.client.ChatRouterServiceClient;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchUserException;
import tv.v1x1.common.util.text.Splitter;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Josh
 */
public class Chat {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * Convenience method to hide the semantics behind sending messages to channels
     * @author Josh
     * @param module DTO module sending the message
     * @param channel channel to send a message to
     * @param text the message to send
     */
    public static void message(final Module<?, ?> module, final Channel channel, final String text) {
        int maxLength = 2000;
        boolean escapeCommand = false;
        if(channel instanceof TwitchChannel) {
            if(text.startsWith(".") || text.startsWith("/")) {
                escapeCommand = true;
                maxLength = 498;
            } else {
                maxLength = 500;
            }
        }
        else if(channel instanceof DiscordChannel)
            maxLength = 2000;
        else
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());

        for(final String line : Splitter.split(maxLength, "...", (escapeCommand ? ". " : "") + text)) {
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, line);
        }
    }

    /**
     * Convenience method to hide all the semantics behind localizing messages and sending them to channels
     * @param module
     * @param channel
     * @param key
     * @param parameters
     */
    public static void i18nMessage(final Module<?, ?> module, final Channel channel, final String key, final Object... parameters) {
        if(parameters.length % 2 != 0) throw new IllegalArgumentException("Passed a non-even amount of arguments for i18n params");
        final Map<String, Object> castParams = new HashMap<>();
        for(int i = 0; i < parameters.length; ++i) {
            if(!(parameters[i] instanceof String)) throw new IllegalArgumentException("Passed a non-String key for i18n params");
            castParams.put((String)parameters[i], parameters[++i]);
        }
        final Language language = I18n.DEFAULT_LANGUAGE; // TODO: Detect the language automatically
        Chat.message(module, channel, language.message(module.toDto(), key, castParams));
    }

    /**
     * Clear a users messages from the channel
     * @author Josh
     * @param module
     * @param channel
     * @param user
     * @param amount if a platform supports it, the number of messages to purge
     * @param reason reason for punishment, if supported
     */
    public static void purge(final Module<?, ?> module, final Channel channel, final User user, final int amount, final String reason) {
        if(channel instanceof TwitchChannel) {
            final DisplayNameService dns = module.getDisplayNameService();
            String username;
            try {
                username = dns.getUserFromId(channel, user.getId());
            } catch(NoSuchUserException e) {
                LOG.error("Error resolving Twitch ID to username", e);
                username = user.getDisplayName(); // Try anyway, since most of the time this will work
            }
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/timeout %s 1 %s", username, reason));
        } else if(channel instanceof DiscordChannel) {
            throw new IllegalArgumentException("DiscordChannel purges not supported yet");
        }  else {
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
        }
    }

    /**
     * Quiet a user on a channel
     * @author Josh
     * @param module
     * @param channel
     * @param user
     * @param length duration (in seconds) to quiet a user for, if supported
     * @param reason reason for punishment, if supported
     * @throws ChatException
     */
    public static void timeout(final Module<?, ?> module, final Channel channel, final User user, final Integer length, final String reason) throws ChatException {
        if(channel instanceof TwitchChannel) {
            final DisplayNameService dns = module.getDisplayNameService();
            String username;
            try {
                username = dns.getUserFromId(channel, user.getId());
            } catch(NoSuchUserException e) {
                LOG.error("Error resolving Twitch ID to username", e);
                username = user.getDisplayName(); // Try anyway, since most of the time this will work
            }
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/timeout %s %d %s", username, length, reason));
        } else if(channel instanceof DiscordChannel) {
            throw new ChatException("DiscordChannel doesn't support timeout()");
        }  else {
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
        }
    }

    /**
     * Undo the action from {@link Chat#timeout(Module, Channel, User, Integer, String)}
     * @author Josh
     * @param module
     * @param channel
     * @param user
     * @throws ChatException
     */
    public static void untimeout(final Module<?, ?> module, final Channel channel, final User user) throws ChatException {
        if(channel instanceof TwitchChannel) {
            final DisplayNameService dns = module.getDisplayNameService();
            String username;
            try {
                username = dns.getUserFromId(channel, user.getId());
            } catch(NoSuchUserException e) {
                LOG.error("Error resolving Twitch ID to username", e);
                username = user.getDisplayName(); // Try anyway, since most of the time this will work
            }
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/untimeout %s", username));
        } else if(channel instanceof DiscordChannel) {
            throw new ChatException("DiscordChannel doesn't support timeout()");
        } else {
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
        }
    }

    /**
     * Temporarily remove a user from a channel
     * @author Josh
     * @param module
     * @param channel
     * @param user
     * @param reason reason for punishment, if supported
     * @throws ChatException
     */
    public static void kick(final Module<?, ?> module, final Channel channel, final User user, final String reason) throws ChatException {
        if(channel instanceof TwitchChannel) {
            throw new ChatException("TwitchChannel doesn't support kick()");
        } else if(channel instanceof DiscordChannel) {
            throw new IllegalArgumentException("DiscordChannel kicks not supported yet");
        } else {
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
        }
    }

    /**
     * Indefinitely remove a user from a channel
     * @author
     * @param module
     * @param channel
     * @param user
     * @param length time (in seconds) to ban for; 0 for indefinite (if supported)
     * @param reason reason for punishment, if supported
     */
    public static void ban(final Module<?, ?> module, final Channel channel, final User user, final Integer length, final String reason) {
        if(channel instanceof TwitchChannel) {
            final DisplayNameService dns = module.getDisplayNameService();
            String username;
            try {
                username = dns.getUserFromId(channel, user.getId());
            } catch(NoSuchUserException e) {
                LOG.error("Error resolving Twitch ID to username", e);
                username = user.getDisplayName(); // Try anyway, since most of the time this will work
            }
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/ban %s %s", username, reason));
        } else if(channel instanceof DiscordChannel) {
            throw new IllegalArgumentException("DiscordChannel bans not supported yet");
        } else {
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
        }
    }

    /**
     * Convenience method to take the minimal punishment on a user supported by a platform
     * @param module DTO module
     * @param channel channel to punish on
     * @param user user to punish
     * @param length length of time to punish for, in seconds, if applicable
     * @param reason reason for punishment, if supported
     */
    public static void punish(final Module<?, ?> module, final Channel channel, final User user, final Integer length, final String reason) {
        try {
            if(channel instanceof TwitchChannel) Chat.timeout(module, channel, user, length, reason);
            if(channel instanceof DiscordChannel) Chat.timeout(module, channel, user, length, reason);
            //if(channel instanceof IrcChannel) Chat.kick()...; // Example, but we don't have IRC channels yet
        } catch(ChatException e) {
            // We've done the impossible
        }
    }
}
