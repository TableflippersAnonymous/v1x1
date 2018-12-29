package tv.v1x1.modules.core.tmi;

import brave.Span;
import brave.Tracer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.PrivateMessage;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.TwitchUser;
import tv.v1x1.common.dto.irc.IrcSource;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.IrcUser;
import tv.v1x1.common.dto.irc.MessageTaggedIrcStanza;
import tv.v1x1.common.dto.irc.commands.ClearChatCommand;
import tv.v1x1.common.dto.irc.commands.GlobalUserStateCommand;
import tv.v1x1.common.dto.irc.commands.HostTargetCommand;
import tv.v1x1.common.dto.irc.commands.JoinCommand;
import tv.v1x1.common.dto.irc.commands.ModeCommand;
import tv.v1x1.common.dto.irc.commands.NoticeCommand;
import tv.v1x1.common.dto.irc.commands.PartCommand;
import tv.v1x1.common.dto.irc.commands.PingCommand;
import tv.v1x1.common.dto.irc.commands.PrivmsgCommand;
import tv.v1x1.common.dto.irc.commands.ReconnectCommand;
import tv.v1x1.common.dto.irc.commands.RoomStateCommand;
import tv.v1x1.common.dto.irc.commands.RplEndOfMotdCommand;
import tv.v1x1.common.dto.irc.commands.RplNameReplyCommand;
import tv.v1x1.common.dto.irc.commands.UserNoticeCommand;
import tv.v1x1.common.dto.irc.commands.UserStateCommand;
import tv.v1x1.common.dto.irc.commands.WhisperCommand;
import tv.v1x1.common.dto.messages.events.TwitchBotChannelStateEvent;
import tv.v1x1.common.dto.messages.events.TwitchBotConnectedEvent;
import tv.v1x1.common.dto.messages.events.TwitchBotGlobalStateEvent;
import tv.v1x1.common.dto.messages.events.TwitchChannelEvent;
import tv.v1x1.common.dto.messages.events.TwitchChannelUsersEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatJoinEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatPartEvent;
import tv.v1x1.common.dto.messages.events.TwitchHostEvent;
import tv.v1x1.common.dto.messages.events.TwitchPingEvent;
import tv.v1x1.common.dto.messages.events.TwitchPrivateMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchRawMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchReconnectEvent;
import tv.v1x1.common.dto.messages.events.TwitchRoomStateEvent;
import tv.v1x1.common.dto.messages.events.TwitchTimeoutEvent;
import tv.v1x1.common.dto.messages.events.TwitchUserEvent;
import tv.v1x1.common.dto.messages.events.TwitchUserModChangeEvent;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.services.twitch.dto.channels.Channel;
import tv.v1x1.common.util.data.CompositeKey;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TmiClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TmiBot tmiBot;
    private final String username;
    private final Channel channel;
    private final Deduplicator deduplicator;
    private final Tracer tracer;
    private final TwitchDisplayNameService twitchDisplayNameService;

    public TmiClientHandler(final TmiBot tmiBot, final String username, final Channel channel,
                            final Deduplicator deduplicator, final Tracer tracer,
                            final TwitchDisplayNameService twitchDisplayNameService) {
        this.tmiBot = tmiBot;
        this.username = username;
        this.channel = channel;
        this.deduplicator = deduplicator;
        this.tracer = tracer;
        this.twitchDisplayNameService = twitchDisplayNameService;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object message) {
        final Span rootSpan = tracer.newTrace()
                .name("TMI recv")
                .start();
        try {
            if (message == null)
                return;
            if (!(message instanceof IrcStanza))
                return;
            final IrcStanza stanza = (IrcStanza) message;
            log("Read: " + stanza.getRawLine());
            if (stanza instanceof PingCommand)
                tmiBot.sendLine("PONG :" + ((PingCommand) stanza).getToken());
            else if (stanza instanceof JoinCommand)
                handleJoin((JoinCommand) stanza);
            else if (stanza instanceof PartCommand)
                handlePart((PartCommand) stanza);
            else if (stanza.getTags().containsKey("id") && deduplicator.seenAndAdd(new tv.v1x1.common.dto.core.UUID(UUID.fromString(stanza.getTags().get("id")))))
                return;
            else if (stanza instanceof WhisperCommand && stanza.getTags().containsKey("thread-id") && stanza.getTags().containsKey("message-id")
                    && deduplicator.seenAndAdd(new tv.v1x1.common.dto.core.UUID(UUID.nameUUIDFromBytes(CompositeKey.makeKey(stanza.getTags().get("thread-id"), stanza.getTags().get("message-id"))))))
                return;
            final Span span = tracer.newChild(rootSpan.context()).name("TMI event stanza").start();
            try {
                event(stanza, span);
            } catch (final NoSuchTargetException e) {
                throw new IllegalStateException(e);
            } finally {
                span.finish();
            }
        } finally {
            rootSpan.finish();
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        tmiBot.login();
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        tmiBot.cleanup();
        if(tmiBot.isRunning())
            tmiBot.connect();
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOG.error("[{}] [{}] Got exception: ", username, channel.getDisplayName(), cause);
        tmiBot.disconnect();
    }

    private void handleJoin(final JoinCommand stanza) {
        if(!isForMyChannel(stanza.getSource(), stanza.getChannel()))
            return;
        tmiBot.registerService();
    }

    private void handlePart(final PartCommand stanza) {
        if(!isForMyChannel(stanza.getSource(), stanza.getChannel()))
            return;
        tmiBot.unregisterService();
    }

    private void event(final IrcStanza stanza, final Span parentSpan) throws NoSuchTargetException {
        tmiBot.cache(stanza, parentSpan);
        tmiBot.event(new TwitchRawMessageEvent(tmiBot.module, tmiBot.bot, stanza), parentSpan);
        if(stanza instanceof ClearChatCommand)
            event((ClearChatCommand) stanza, parentSpan);
        else if(stanza instanceof GlobalUserStateCommand)
            event((GlobalUserStateCommand) stanza, parentSpan);
        else if(stanza instanceof HostTargetCommand)
            event((HostTargetCommand) stanza, parentSpan);
        else if(stanza instanceof JoinCommand)
            event((JoinCommand) stanza, parentSpan);
        else if(stanza instanceof ModeCommand)
            event((ModeCommand) stanza, parentSpan);
        else if(stanza instanceof NoticeCommand)
            event((NoticeCommand) stanza, parentSpan);
        else if(stanza instanceof PartCommand)
            event((PartCommand) stanza, parentSpan);
        else if(stanza instanceof PingCommand)
            event((PingCommand) stanza, parentSpan);
        else if(stanza instanceof PrivmsgCommand)
            event((PrivmsgCommand) stanza, parentSpan);
        else if(stanza instanceof ReconnectCommand)
            event((ReconnectCommand) stanza, parentSpan);
        else if(stanza instanceof RoomStateCommand)
            event((RoomStateCommand) stanza, parentSpan);
        else if(stanza instanceof RplEndOfMotdCommand)
            event((RplEndOfMotdCommand) stanza, parentSpan);
        else if(stanza instanceof RplNameReplyCommand)
            event((RplNameReplyCommand) stanza, parentSpan);
        else if(stanza instanceof UserNoticeCommand)
            event((UserNoticeCommand) stanza, parentSpan);
        else if(stanza instanceof UserStateCommand)
            event((UserStateCommand) stanza, parentSpan);
        else if(stanza instanceof WhisperCommand)
            event((WhisperCommand) stanza, parentSpan);
        else
            throw new IllegalStateException("Unknown IrcStanza: " + stanza.getClass().getCanonicalName());
    }

    private void event(final ClearChatCommand clearChatCommand, final Span parentSpan) throws NoSuchTargetException {
        tmiBot.event(new TwitchTimeoutEvent(tmiBot.module, getChannel(clearChatCommand), getUserByUsername(clearChatCommand.getNickname()), clearChatCommand), parentSpan);
    }

    private void event(final GlobalUserStateCommand globalUserStateCommand, final Span parentSpan) {
        tmiBot.event(new TwitchBotGlobalStateEvent(tmiBot.module, tmiBot.bot, globalUserStateCommand), parentSpan);
    }

    private void event(final HostTargetCommand hostTargetCommand, final Span parentSpan) throws NoSuchTargetException {
        tmiBot.event(new TwitchHostEvent(tmiBot.module, getChannel(hostTargetCommand), hostTargetCommand.getTargetChannel().equals("-") ? null : getChannelByName(hostTargetCommand.getTargetChannel()), hostTargetCommand), parentSpan);
    }

    private void event(final JoinCommand joinCommand, final Span parentSpan) throws NoSuchTargetException {
        tmiBot.event(new TwitchChatJoinEvent(tmiBot.module, getUser(joinCommand), getChannel(joinCommand), joinCommand), parentSpan);
    }

    private void event(final ModeCommand modeCommand, final Span parentSpan) throws NoSuchTargetException {
        for(final String username : modeCommand.getNicknames())
            tmiBot.event(new TwitchUserModChangeEvent(tmiBot.module, getChannel(modeCommand), getUserByUsername(username), modeCommand.getModeString().startsWith("+"), modeCommand), parentSpan);
    }

    private void event(final NoticeCommand noticeCommand, final Span parentSpan) throws NoSuchTargetException {
        tmiBot.event(new TwitchChannelEvent(tmiBot.module, getChannel(noticeCommand), noticeCommand.getMessage(), noticeCommand), parentSpan);
    }

    private void event(final PartCommand partCommand, final Span parentSpan) throws NoSuchTargetException {
        tmiBot.event(new TwitchChatPartEvent(tmiBot.module, getUser(partCommand), getChannel(partCommand), partCommand), parentSpan);
    }

    private void event(final PingCommand pingCommand, final Span parentSpan) {
        tmiBot.event(new TwitchPingEvent(tmiBot.module, pingCommand.getToken(), pingCommand), parentSpan);
    }

    private void event(final PrivmsgCommand privmsgCommand, final Span parentSpan) throws NoSuchTargetException {
        final TwitchChannel channel = getChannel(privmsgCommand);
        final TwitchUser user = getUser(privmsgCommand);
        if((privmsgCommand.getSource() instanceof IrcUser) && ((IrcUser) privmsgCommand.getSource()).getNickname().equals(username))
            return;
        final Set<String> badges = privmsgCommand.getBadges().stream().map(MessageTaggedIrcStanza.Badge::name).collect(Collectors.toSet());
        badges.add("_DEFAULT_");
        final List<Permission> permissions = tmiBot.tmiModule.getPermissions(channel.getChannelGroup().getTenant(), user.getGlobalUser(), channel.getChannelGroup().getId(), badges);
        final ChatMessage chatMessage = new ChatMessage(channel, user, privmsgCommand.getMessage(), permissions);
        tmiBot.event(new TwitchChatMessageEvent(tmiBot.module, chatMessage, privmsgCommand), parentSpan);
    }

    private void event(final ReconnectCommand reconnectCommand, final Span parentSpan) {
        tmiBot.event(new TwitchReconnectEvent(tmiBot.module, tmiBot.bot, reconnectCommand), parentSpan);
    }

    private void event(final RoomStateCommand roomStateCommand, final Span parentSpan) throws NoSuchTargetException {
        tmiBot.event(new TwitchRoomStateEvent(tmiBot.module, getChannel(roomStateCommand), roomStateCommand), parentSpan);
    }

    private void event(final RplEndOfMotdCommand rplEndOfMotdCommand, final Span parentSpan) {
        tmiBot.event(new TwitchBotConnectedEvent(tmiBot.module, tmiBot.bot, rplEndOfMotdCommand), parentSpan);
    }

    private void event(final RplNameReplyCommand rplNameReplyCommand, final Span parentSpan) throws NoSuchTargetException {
        tmiBot.event(new TwitchChannelUsersEvent(tmiBot.module, getChannel(rplNameReplyCommand), rplNameReplyCommand.getMembers().stream().map(member -> {
            try {
                return getUserByUsername(member.getNickname());
            } catch (final NoSuchTargetException e) {
                throw new IllegalStateException(e);
            }
        }).collect(Collectors.toList()), rplNameReplyCommand), parentSpan);
    }

    private void event(final UserNoticeCommand userNoticeCommand, final Span parentSpan) {
        try {
            tmiBot.event(new TwitchUserEvent(tmiBot.module, getChannel(userNoticeCommand), getUserByUsername(userNoticeCommand.getLogin()), userNoticeCommand.getMessage(), userNoticeCommand), parentSpan);
        } catch (final NoSuchTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private void event(final UserStateCommand userStateCommand, final Span parentSpan) throws NoSuchTargetException {
        tmiBot.event(new TwitchBotChannelStateEvent(tmiBot.module, getChannel(userStateCommand), tmiBot.bot, userStateCommand), parentSpan);
    }

    private void event(final WhisperCommand whisperCommand, final Span parentSpan) {
        tmiBot.event(new TwitchPrivateMessageEvent(tmiBot.module, new PrivateMessage(tmiBot.bot, getUser(whisperCommand), whisperCommand.getMessage()), whisperCommand), parentSpan);
    }

    private TwitchUser getUser(final IrcStanza stanza) {
        final IrcSource source = stanza.getSource();

        final String displayName = stanza.getTags().get("display-name");

        try {
            // If we have a user-id, use that.
            if(stanza.getTags().containsKey("user-id"))
                return getUser(stanza.getTags().get("user-id"), displayName);

            // Otherwise, we need to extract a username and look it up in the TwitchDisplayNameService
            if(source instanceof IrcUser)
                return getUser(twitchDisplayNameService.getUserIdFromUsername(((IrcUser) source).getNickname()), displayName);
            else if(stanza.getTags().containsKey("login"))
                return getUser(twitchDisplayNameService.getUserIdFromUsername(stanza.getTags().get("login")), displayName);
        } catch(final NoSuchTargetException e) {
            throw new IllegalArgumentException(e);
        }
        throw new IllegalArgumentException("Not able to find a TwitchUser in stanza: " + stanza.getRawLine());
    }

    private TwitchUser getUserByUsername(final String username) throws NoSuchTargetException {
        return getUser(twitchDisplayNameService.getUserIdFromUsername(username), null);
    }

    private TwitchUser getUser(final String id, final String displayName) throws NoSuchTargetException {
        return new TwitchUser(id, tmiBot.tmiModule.getGlobalUser(id, displayName), displayName == null ? twitchDisplayNameService.getDisplayNameFromUserId(id) : displayName);
    }

    private TwitchChannel getChannel(final IrcStanza stanza) throws NoSuchTargetException {
        // If we have a room-id, use that.
        if(stanza.getTags().containsKey("room-id"))
            return getChannel(stanza.getTags().get("room-id"));

        switch(stanza.getCommand()) {
            case JOIN: return getChannelByName(((JoinCommand) stanza).getChannel());
            case PART: return getChannelByName(((PartCommand) stanza).getChannel());
            case PRIVMSG: return getChannelByName(((PrivmsgCommand) stanza).getChannel());
            case RPL_NAMREPLY: return getChannelByName(((RplNameReplyCommand) stanza).getChannel());
            case MODE: return getChannelByName(((ModeCommand) stanza).getChannel());
            case NOTICE: return getChannelByName(((NoticeCommand) stanza).getChannel());
            case HOSTTARGET: return getChannelByName(((HostTargetCommand) stanza).getChannel());
            case CLEARCHAT: return getChannelByName(((ClearChatCommand) stanza).getChannel());
            case USERSTATE: return getChannelByName(((UserStateCommand) stanza).getChannel());
            case ROOMSTATE: return getChannelByName(((RoomStateCommand) stanza).getChannel());
            case USERNOTICE: return getChannelByName(((UserNoticeCommand) stanza).getChannel());
            default: throw new IllegalArgumentException("Not able to find a TwitchChannel in stanza: " + stanza.getRawLine());
        }
    }

    private TwitchChannel getChannelByName(final String channelName) throws NoSuchTargetException {
        String effectiveChannelName = channelName;
        if(channelName.startsWith("#"))
            effectiveChannelName = channelName.substring(1);
        return getChannel(twitchDisplayNameService.getChannelIdByChannelName(effectiveChannelName));
    }

    private TwitchChannel getChannel(final String id) throws NoSuchTargetException {
        final String displayName = twitchDisplayNameService.getDisplayNameFromChannelId(id);
        return getChannel(id, displayName);
    }

    private TwitchChannel getChannel(final String id, final String displayName) {
        return new TwitchChannel(id + ":main", tmiBot.tmiModule.getChannelGroup(id), displayName);
    }

    private boolean isForMyChannel(final IrcSource source, final String channelName) {
        if(!(source instanceof IrcUser))
            return false;
        final IrcUser user = (IrcUser) source;
        if(!user.getNickname().equals(username))
            return false;
        return channelName.equals("#" + channel.getName());
    }

    private void log(final String message) {
        tmiBot.log(message);
    }
}
