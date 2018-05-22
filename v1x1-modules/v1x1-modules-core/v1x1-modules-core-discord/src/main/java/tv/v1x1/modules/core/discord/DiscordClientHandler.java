package tv.v1x1.modules.core.discord;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.DiscordChannel;
import tv.v1x1.common.dto.core.DiscordUser;
import tv.v1x1.common.dto.db.ChannelGroup;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.messages.events.DiscordChatMessageEvent;
import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.channel.Channel;
import tv.v1x1.common.services.discord.dto.channel.ChannelType;
import tv.v1x1.common.services.discord.dto.guild.CompleteGuild;
import tv.v1x1.common.services.discord.dto.guild.Guild;
import tv.v1x1.common.services.discord.dto.guild.GuildMember;
import tv.v1x1.common.services.discord.dto.guild.UnavailableGuild;
import tv.v1x1.common.services.discord.dto.user.Status;
import tv.v1x1.common.services.discord.dto.user.StatusUpdate;
import tv.v1x1.common.services.discord.dto.user.User;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.state.DiscordDisplayNameService;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;
import tv.v1x1.modules.core.discord.gateway.HeartbeatAckPayload;
import tv.v1x1.modules.core.discord.gateway.HeartbeatPayload;
import tv.v1x1.modules.core.discord.gateway.HelloPayload;
import tv.v1x1.modules.core.discord.gateway.IdentifyPayload;
import tv.v1x1.modules.core.discord.gateway.InvalidSessionPayload;
import tv.v1x1.modules.core.discord.gateway.Payload;
import tv.v1x1.modules.core.discord.gateway.ReconnectPayload;
import tv.v1x1.modules.core.discord.gateway.ResumePayload;
import tv.v1x1.modules.core.discord.gateway.events.ChannelCreateEvent;
import tv.v1x1.modules.core.discord.gateway.events.ChannelDeleteEvent;
import tv.v1x1.modules.core.discord.gateway.events.ChannelPinsUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.ChannelUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildBanAddEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildCreateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildDeleteEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildEmojisUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildIntegrationsUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildMemberAddEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildMemberRemoveEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildMemberUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildMembersChunkEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildRoleCreateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildRoleDeleteEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildRoleUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.GuildUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageCreateEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageDeleteBulkEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageDeleteEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageReactionAddEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageReactionRemoveAllEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageReactionRemoveEvent;
import tv.v1x1.modules.core.discord.gateway.events.MessageUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.PresenceUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.ReadyEvent;
import tv.v1x1.modules.core.discord.gateway.events.ResumedEvent;
import tv.v1x1.modules.core.discord.gateway.events.TypingStartEvent;
import tv.v1x1.modules.core.discord.gateway.events.UserUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.VoiceServerUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.VoiceStateUpdateEvent;
import tv.v1x1.modules.core.discord.gateway.events.WebhooksUpdateEvent;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 4/11/2018.
 */
public class DiscordClientHandler implements MessageHandler.Whole<String> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper mapper = new ObjectMapper(new JsonFactory());

    private final DiscordModule discordModule;
    private final String shardKey;
    private final int shardNumber;
    private final int totalShards;
    private final Session session;
    private final CountDownLatch countDownLatch;

    private volatile boolean gotPing = true;
    private volatile Long lastSeenSequence = null;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public DiscordClientHandler(final DiscordModule discordModule, final String shardKey, final int shardNumber,
                                final int totalShards, final Session session, final CountDownLatch countDownLatch) {
        this.discordModule = discordModule;
        this.shardKey = shardKey;
        this.shardNumber = shardNumber;
        this.totalShards = totalShards;
        this.session = session;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onMessage(final String s) {
        try {
            LOG.debug("[{}] WS Read: {}", shardKey, s);
            final Payload payload = mapper.readValue(s, Payload.class);
            LOG.debug("[{}] Frame read: {}", shardKey, payload);
            if(payload instanceof DispatchPayload)
                handlePayload((DispatchPayload) payload);
            else if(payload instanceof HeartbeatPayload)
                handlePayload((HeartbeatPayload) payload);
            else if(payload instanceof ReconnectPayload)
                handlePayload((ReconnectPayload) payload);
            else if(payload instanceof InvalidSessionPayload)
                handlePayload((InvalidSessionPayload) payload);
            else if(payload instanceof HelloPayload)
                handlePayload((HelloPayload) payload);
            else if(payload instanceof HeartbeatAckPayload)
                handlePayload((HeartbeatAckPayload) payload);
            else
                LOG.warn("[{}] Unknown frame: {} {}", shardKey, payload.getOp(), payload.getClass().getCanonicalName());
        } catch (final IOException e) {
            LOG.error("[{}] Got exception", shardKey, e);
        }
    }

    private void handlePayload(final DispatchPayload payload) {
        lastSeenSequence = payload.getSequenceNumber();
        discordModule.setLastSeen(shardKey, lastSeenSequence);
        if(payload instanceof ReadyEvent)
            handleEvent((ReadyEvent) payload);
        else if(payload instanceof ResumedEvent)
            handleEvent((ResumedEvent) payload);
        else if(payload instanceof ChannelCreateEvent)
            handleEvent((ChannelCreateEvent) payload);
        else if(payload instanceof ChannelUpdateEvent)
            handleEvent((ChannelUpdateEvent) payload);
        else if(payload instanceof ChannelDeleteEvent)
            handleEvent((ChannelDeleteEvent) payload);
        else if(payload instanceof ChannelPinsUpdateEvent)
            handleEvent((ChannelPinsUpdateEvent) payload);
        else if(payload instanceof GuildCreateEvent)
            handleEvent((GuildCreateEvent) payload);
        else if(payload instanceof GuildUpdateEvent)
            handleEvent((GuildUpdateEvent) payload);
        else if(payload instanceof GuildDeleteEvent)
            handleEvent((GuildDeleteEvent) payload);
        else if(payload instanceof GuildBanAddEvent)
            handleEvent((GuildBanAddEvent) payload);
        else if(payload instanceof GuildEmojisUpdateEvent)
            handleEvent((GuildEmojisUpdateEvent) payload);
        else if(payload instanceof GuildIntegrationsUpdateEvent)
            handleEvent((GuildIntegrationsUpdateEvent) payload);
        else if(payload instanceof GuildMemberAddEvent)
            handleEvent((GuildMemberAddEvent) payload);
        else if(payload instanceof GuildMemberRemoveEvent)
            handleEvent((GuildMemberRemoveEvent) payload);
        else if(payload instanceof GuildMemberUpdateEvent)
            handleEvent((GuildMemberUpdateEvent) payload);
        else if(payload instanceof GuildMembersChunkEvent)
            handleEvent((GuildMembersChunkEvent) payload);
        else if(payload instanceof GuildRoleCreateEvent)
            handleEvent((GuildRoleCreateEvent) payload);
        else if(payload instanceof GuildRoleUpdateEvent)
            handleEvent((GuildRoleUpdateEvent) payload);
        else if(payload instanceof GuildRoleDeleteEvent)
            handleEvent((GuildRoleDeleteEvent) payload);
        else if(payload instanceof MessageCreateEvent)
            handleEvent((MessageCreateEvent) payload);
        else if(payload instanceof MessageUpdateEvent)
            handleEvent((MessageUpdateEvent) payload);
        else if(payload instanceof MessageDeleteEvent)
            handleEvent((MessageDeleteEvent) payload);
        else if(payload instanceof MessageDeleteBulkEvent)
            handleEvent((MessageDeleteBulkEvent) payload);
        else if(payload instanceof MessageReactionAddEvent)
            handleEvent((MessageReactionAddEvent) payload);
        else if(payload instanceof MessageReactionRemoveEvent)
            handleEvent((MessageReactionRemoveEvent) payload);
        else if(payload instanceof MessageReactionRemoveAllEvent)
            handleEvent((MessageReactionRemoveAllEvent) payload);
        else if(payload instanceof PresenceUpdateEvent)
            handleEvent((PresenceUpdateEvent) payload);
        else if(payload instanceof TypingStartEvent)
            handleEvent((TypingStartEvent) payload);
        else if(payload instanceof UserUpdateEvent)
            handleEvent((UserUpdateEvent) payload);
        else if(payload instanceof VoiceStateUpdateEvent)
            handleEvent((VoiceStateUpdateEvent) payload);
        else if(payload instanceof VoiceServerUpdateEvent)
            handleEvent((VoiceServerUpdateEvent) payload);
        else if(payload instanceof WebhooksUpdateEvent)
            handleEvent((WebhooksUpdateEvent) payload);
        else
            LOG.warn("[{}] Unknown event: {} {}", shardKey, payload.getEventType(), payload.getClass().getCanonicalName());
    }

    private void handleEvent(final ReadyEvent payload) {
        discordModule.setSessionId(shardKey, payload.getData().getSessionId());
        createUser(payload.getData().getUser());
        LOG.info("[{}] Connected.", shardKey);
    }

    private void handleEvent(final ResumedEvent payload) {
        LOG.info("[{}] Resumed connection.", shardKey);
    }

    private void handleEvent(final ChannelCreateEvent payload) {
        createChannel(payload.getChannel().getGuildId(), payload.getChannel());
    }

    private void handleEvent(final ChannelUpdateEvent payload) {
        updateChannel(payload.getChannel());
    }

    private void handleEvent(final ChannelDeleteEvent payload) {
        deleteChannel(payload.getChannel());
    }

    private void handleEvent(final ChannelPinsUpdateEvent payload) {

    }

    private void handleEvent(final GuildCreateEvent payload) {
        createChannelGroup(payload.getGuild());
    }

    private void handleEvent(final GuildUpdateEvent payload) {
        updateChannelGroup(payload.getGuild());
    }

    private void handleEvent(final GuildDeleteEvent payload) {
        if(!payload.getGuild().isUnavailable())
            deleteChannelGroup(payload.getGuild());
    }

    private void handleEvent(final GuildBanAddEvent payload) {

    }

    private void handleEvent(final GuildEmojisUpdateEvent payload) {

    }

    private void handleEvent(final GuildIntegrationsUpdateEvent payload) {

    }

    private void handleEvent(final GuildMemberAddEvent payload) {
        createUser(payload.getMemberWithGuildId().getUser());
    }

    private void handleEvent(final GuildMemberRemoveEvent payload) {
        removeMember(payload.getData());
    }

    private void handleEvent(final GuildMemberUpdateEvent payload) {
        updateMember(payload.getData());
    }

    private void handleEvent(final GuildMembersChunkEvent payload) {
        createMembers(payload.getChunk().getGuildId(), payload.getChunk().getMembers());
    }

    private void handleEvent(final GuildRoleCreateEvent payload) {

    }

    private void handleEvent(final GuildRoleUpdateEvent payload) {

    }

    private void handleEvent(final GuildRoleDeleteEvent payload) {

    }

    private void handleEvent(final MessageCreateEvent payload) {
        if(discordModule.getInjector().getInstance(Deduplicator.class).seenAndAdd(new tv.v1x1.common.dto.core.UUID(UUID.nameUUIDFromBytes(payload.getMessage().getId().getBytes()))))
            return;
        final DiscordChannel channel = getChannel(payload.getMessage().getChannelId());
        final DiscordUser user = getUser(payload.getMessage().getAuthor());
        final Set<String> roles = new HashSet<>(discordModule.getRoles(channel.getChannelGroup().getId(), channel.getId()));
        roles.add("_DEFAULT_");
        final List<tv.v1x1.common.dto.core.Permission> permissions = discordModule.getPermissions(channel.getChannelGroup().getTenant(),
                user.getGlobalUser(), channel.getChannelGroup().getId(), roles);
        discordModule.sendEvent(new DiscordChatMessageEvent(discordModule.toDto(), new ChatMessage(channel, user,
                payload.getMessage().getContent(), permissions)));
    }

    private void handleEvent(final MessageUpdateEvent payload) {

    }

    private void handleEvent(final MessageDeleteEvent payload) {

    }

    private void handleEvent(final MessageDeleteBulkEvent payload) {

    }

    private void handleEvent(final MessageReactionAddEvent payload) {

    }

    private void handleEvent(final MessageReactionRemoveEvent payload) {

    }

    private void handleEvent(final MessageReactionRemoveAllEvent payload) {

    }

    private void handleEvent(final PresenceUpdateEvent payload) {

    }

    private void handleEvent(final TypingStartEvent payload) {

    }

    private void handleEvent(final UserUpdateEvent payload) {
        updateUser(payload.getUser());
    }

    private void handleEvent(final VoiceStateUpdateEvent payload) {
        discordModule.handleVoiceState(payload.getVoiceState());
    }

    private void handleEvent(final VoiceServerUpdateEvent payload) {

    }

    private void handleEvent(final WebhooksUpdateEvent payload) {

    }

    private void handlePayload(final HeartbeatPayload payload) {
        send(new HeartbeatPayload(lastSeenSequence));
    }

    private void handlePayload(final ReconnectPayload payload) {
        try {
            session.close();
        } catch (final IOException e) {
            LOG.error("[{}] Error while disconnecting", shardKey, e);
        }
    }

    private void handlePayload(final InvalidSessionPayload payload) {
        scheduledExecutorService.schedule(this::sendIdentify, 5, TimeUnit.SECONDS);
    }

    private void handlePayload(final HelloPayload payload) {
        LOG.debug("[{}] Discord trace: {}", shardKey, Joiner.on(", ").join(payload.getHelloData().getTrace()));
        startPinging(payload.getHelloData().getHeartbeatInterval());
        sendResumeOrIdentify();
    }

    private void startPinging(final int interval) {
        gotPing = true;
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if(countDownLatch.getCount() == 0)
                scheduledExecutorService.shutdown();
            if(!gotPing)
                try {
                    session.close();
                } catch (final IOException e) {
                    LOG.error("[{}] Got exception", shardKey, e);
                }
            gotPing = false;
            send(new HeartbeatPayload(lastSeenSequence));
        }, interval, interval, TimeUnit.MILLISECONDS);
    }

    private void sendResumeOrIdentify() {
        final String sessionId = discordModule.getSessionId(shardKey);
        final Long sequenceNumber = discordModule.getLastSeen(shardKey);
        if(sessionId == null || sequenceNumber == null)
            sendIdentify();
        else
            sendResume(sessionId, sequenceNumber);
    }

    private void sendResume(final String sessionId, final Long sequenceNumber) {
        final ResumePayload resumePayload = new ResumePayload(new ResumePayload.ResumeData(
                discordModule.getInjector().getInstance(DiscordApi.class).getOauthToken(),
                sessionId,
                sequenceNumber
        ));
        send(resumePayload);
    }

    private void sendIdentify() {
        final IdentifyPayload identifyPayload = new IdentifyPayload(new IdentifyPayload.IdentifyData(
                discordModule.getInjector().getInstance(DiscordApi.class).getOauthToken(),
                new IdentifyPayload.ConnectionProperties("linux", "v1x1", "v1x1"),
                false,
                250,
                ImmutableList.of(shardNumber, totalShards),
                new StatusUpdate(null, null, Status.ONLINE, false)
        ));
        discordModule.rateLimitIdentify(() -> send(identifyPayload));
    }

    private void handlePayload(final HeartbeatAckPayload payload) {
        gotPing = true;
    }

    private void send(final Payload payload) {
        try {
            final String s = mapper.writeValueAsString(payload);
            LOG.debug("[{}] WS Write: {}", shardKey, s.replace(discordModule.getInjector().getInstance(DiscordApi.class).getOauthToken(), "<oauth token remove>"));
            session.getAsyncRemote().sendText(s);
        } catch (final JsonProcessingException e) {
            LOG.error("[{}] Error processing JSON", shardKey, e);
        }
    }

    private void createUser(final User user) {
        discordModule.getDaoManager().getDaoGlobalUser().getOrCreate(Platform.DISCORD, user.getId(), user.getUsername());
        discordModule.getInjector().getInstance(DiscordDisplayNameService.class).cacheUser(user);
    }

    private void updateUser(final User user) {
        discordModule.getDaoManager().getDaoGlobalUser().addUser(discordModule.getGlobalUser(user.getId(), user.getUsername()).toDB(), Platform.DISCORD, user.getId(), user.getUsername());
        discordModule.getInjector().getInstance(DiscordDisplayNameService.class).cacheUser(user);
    }

    private void createMembers(final String guildId, final Iterable<GuildMember> members) {
        for(final GuildMember user : members) {
            createUser(user.getUser());
            discordModule.setRoles(guildId, user.getUser().getId(), user.getRoleIds());
        }
    }

    private void updateMember(final GuildMemberUpdateEvent.Data data) {
        updateUser(data.getUser());
        discordModule.setRoles(data.getGuildId(), data.getUser().getId(), data.getRoleIds());
    }

    private void removeMember(final GuildMemberRemoveEvent.Data data) {
        discordModule.deleteRoles(data.getGuildId(), data.getUser().getId());
    }

    private void createChannelGroup(final CompleteGuild guild) {
        discordModule.getDaoManager().getDaoTenant().getOrCreate(Platform.DISCORD, guild.getId(), guild.getName());
        createMembers(guild.getId(), guild.getMembers());
        for(final Channel channel : guild.getChannels())
            createChannel(guild.getId(), channel);
    }

    private void updateChannelGroup(final Guild guild) {
        discordModule.getDaoManager().getDaoTenant().addChannelGroup(discordModule.getTenant(guild.getId()).toDB(), Platform.DISCORD, guild.getId(), guild.getName());
    }

    private void deleteChannelGroup(final UnavailableGuild guild) {
        discordModule.getDaoManager().getDaoTenant().removeChannelGroup(discordModule.getTenant(guild.getId()).toDB(), Platform.DISCORD, guild.getId());
    }

    private void createChannel(final String guildId, final Channel channel) {
        discordModule.getInjector().getInstance(DiscordDisplayNameService.class).cacheChannel(channel);
        if(!channel.getType().equals(ChannelType.GUILD_TEXT))
            return;
        discordModule.getDaoManager().getDaoTenant().addChannel(getChannelGroup(guildId), channel.getId(), channel.getName());
        for(final User user : channel.getRecipients())
            createUser(user);
    }

    private void updateChannel(final Channel channel) {
        createChannel(channel.getGuildId(), channel);
    }

    private void deleteChannel(final Channel channel) {
        discordModule.getDaoManager().getDaoTenant().removeChannel(getChannelGroup(channel.getGuildId()), channel.getId());
    }

    private ChannelGroup getChannelGroup(final String guildId) {
        return discordModule.getDaoManager().getDaoTenant().getChannelGroup(Platform.DISCORD, guildId);
    }

    private DiscordChannel getChannel(final String id) {
        final tv.v1x1.common.dto.db.Channel channel = discordModule.getDaoManager().getDaoTenant().getChannel(Platform.DISCORD, id);
        final Tenant tenant = discordModule.getDaoManager().getDaoTenant().getByChannel(channel);
        return (DiscordChannel) tenant.toCore(discordModule.getDaoManager().getDaoTenant()).getChannel(Platform.DISCORD, channel.getChannelGroupId(), channel.getId()).get();
    }

    private DiscordUser getUser(final User user) {
        return (DiscordUser) discordModule.getGlobalUser(user.getId(), user.getUsername()).getUser(Platform.DISCORD, user.getId()).get();
    }
}
