package tv.v1x1.common.services.chat;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.DiscordChannel;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.i18n.Language;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.rpc.client.ChatRouterServiceClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Josh
 */
public class Chat {
    /**
     * Convenience method to hide the semantics behind sending messages to channels
     * @author Josh
     * @param module DTO module sending the message
     * @param channel channel to send a message to
     * @param text the message to send
     */
    public static void message(final Module<?, ?, ?> module, final Channel channel, final String text) {
        if(channel instanceof TwitchChannel)
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, text);
        else if(channel instanceof DiscordChannel)
            throw new IllegalArgumentException("Discord messages not yet supported");
        else
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
    }

    /**
     * Convenience method to hide all the semantics behind localizing messages and sending them to channels
     * @param module
     * @param channel
     * @param key
     * @param parameters
     */
    public static void i18nMessage(final Module<?, ?, ?> module, final Channel channel, final Language language, final String key, final Object... parameters) {
        if(parameters.length % 2 != 0) throw new IllegalArgumentException("Passed a non-even amount of arguments for i18n params");
        final Map<String, Object> castParams = new HashMap<String, Object>();
        for(int i = 0; i < parameters.length; ++i) {
            if(!(parameters[i] instanceof String)) throw new IllegalArgumentException("Passed a non-String key for i18n params");
            castParams.put((String)parameters[i], parameters[++i]);
        }
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
    public static void purge(final Module<?, ?, ?> module, final Channel channel, final User user, final int amount, final String reason) {
        if(channel instanceof TwitchChannel)
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/timeout %s 1 %s", user.getId(), reason));
        else if(channel instanceof DiscordChannel)
            throw new IllegalArgumentException("DiscordChannel purges not supported yet");
        else
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
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
    public static void timeout(final Module<?, ?, ?> module, final Channel channel, final User user, final Integer length, final String reason) throws ChatException {
        if(channel instanceof TwitchChannel)
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/timeout %s %d %s", user.getId(), length, reason));
        else if(channel instanceof DiscordChannel)
            throw new ChatException("DiscordChannel doesn't support timeout()");
        else
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
    }

    /**
     * Undo the action from {@link Chat#timeout(Module, Channel, User, Integer, String)}
     * @author Josh
     * @param module
     * @param channel
     * @param user
     * @throws ChatException
     */
    public static void untimeout(final Module<?, ?, ?> module, final Channel channel, final User user) throws ChatException {
        if(channel instanceof TwitchChannel)
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/untimeout %s", user.getId()));
        else if(channel instanceof DiscordChannel)
            throw new ChatException("DiscordChannel doesn't support timeout()");
        else
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
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
    public static void kick(final Module<?, ?, ?> module, final Channel channel, final User user, final String reason) throws ChatException {
        if(channel instanceof TwitchChannel)
            throw new ChatException("TwitchChannel doesn't support kick()");
        else if(channel instanceof DiscordChannel)
            throw new IllegalArgumentException("DiscordChannel kicks not supported yet");
        else
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
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
    public static void ban(final Module<?, ?, ?> module, final Channel channel, final User user, final Integer length, final String reason) {
        if(channel instanceof TwitchChannel)
            module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/ban %s %s", user.getId(), reason));
        else if(channel instanceof DiscordChannel)
            throw new IllegalArgumentException("DiscordChannel bans not supported yet");
        else
            throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
    }

    /**
     * Convenience method to take the minimal punishment on a user supported by a platform
     * @param module DTO module
     * @param channel channel to punish on
     * @param user user to punish
     * @param length length of time to punish for, in seconds, if applicable
     * @param reason reason for punishment, if supported
     */
    public static void punish(final Module<?, ?, ?> module, final Channel channel, final User user, final Integer length, final String reason) {
        try {
            if(channel instanceof TwitchChannel) Chat.timeout(module, channel, user, length, reason);
            if(channel instanceof DiscordChannel) Chat.timeout(module, channel, user, length, reason);
            //if(channel instanceof IrcChannel) Chat.kick()...; // Example, but we don't have IRC channels yet
        } catch(ChatException e) {
            // We've done the impossible
        }
    }
}
