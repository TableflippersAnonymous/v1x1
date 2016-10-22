package tv.twitchbot.common.services.chat;

import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.core.DiscordChannel;
import tv.twitchbot.common.dto.core.TwitchChannel;
import tv.twitchbot.common.dto.core.User;
import tv.twitchbot.common.modules.Module;
import tv.twitchbot.common.rpc.client.ChatRouterServiceClient;

/**
 * @author Josh
 */
public class Chat {
    public static void message(final Module<?, ?, ?> module, final Channel channel, final String text) {
        if(channel instanceof TwitchChannel) module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, text);
        if(channel instanceof DiscordChannel) throw new IllegalArgumentException("Discord messages not yet supported");
        throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
    }

    public static void purge(final Module<?, ?, ?> module, final Channel channel, final User user, final String reason) {
        if(channel instanceof TwitchChannel) module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/timeout %s 1 %s", user.getId(), reason));
        if(channel instanceof DiscordChannel) throw new IllegalArgumentException("DiscordChannel purges not supported yet");
        throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
    }

    public static void timeout(final Module<?, ?, ?> module, final Channel channel, final User user, final Integer length, final String reason) throws ChatException {
        if(channel instanceof TwitchChannel) module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/timeout %s %d %s", user.getId(), length, reason));
        if(channel instanceof DiscordChannel) throw new ChatException("DiscordChannel doesn't support timeout()");
        throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
    }

    public static void untimeout(final Module<?, ?, ?> module, final Channel channel, final User user) throws ChatException {
        if(channel instanceof TwitchChannel) module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/untimeout %s", user.getId()));
        if(channel instanceof DiscordChannel) throw new ChatException("DiscordChannel doesn't support timeout()");
        throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
    }

    public static void kick(final Module<?, ?, ?> module, final Channel channel, final User user, final String reason) throws ChatException {
        if(channel instanceof TwitchChannel) throw new ChatException("TwitchChannel doesn't support kick()");
        if(channel instanceof DiscordChannel) throw new IllegalArgumentException("DiscordChannel kicks not supported yet");
        throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
    }

    public static void ban(final Module<?, ?, ?> module, final Channel channel, final User user, final Integer length, final String reason) {
        if(channel instanceof TwitchChannel) module.getServiceClient(ChatRouterServiceClient.class).sendMessage(channel, String.format("/ban %s %s", user.getId(), reason));
        if(channel instanceof DiscordChannel) throw new IllegalArgumentException("DiscordChannel bans not supported yet");
        throw new IllegalArgumentException("Unknown Channel type: " + channel.getClass());
    }
}
