package tv.v1x1.modules.channel.wasm;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import org.apache.commons.codec.digest.DigestUtils;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.*;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.DiscordVoiceStateEvent;
import tv.v1x1.common.dto.messages.events.SchedulerNotifyEvent;
import tv.v1x1.common.services.discord.dto.voice.VoiceState;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.modules.channel.wasm.api.HttpResponseEvent;
import tv.v1x1.modules.channel.wasm.api.SyscallWebAssemblyModuleDef;
import tv.v1x1.modules.channel.wasm.api.V1x1WebAssemblyModuleDef;
import tv.v1x1.modules.channel.wasm.config.ModuleUserConfiguration;
import tv.v1x1.modules.channel.wasm.config.WebAssemblyUserConfiguration;
import tv.v1x1.modules.channel.wasm.vm.decoder.ModuleDef;
import tv.v1x1.modules.channel.wasm.vm.runtime.TrapException;
import tv.v1x1.modules.channel.wasm.vm.runtime.WebAssemblyVirtualMachine;
import tv.v1x1.modules.channel.wasm.vm.store.LinkingException;
import tv.v1x1.modules.channel.wasm.vm.types.I32;
import tv.v1x1.modules.channel.wasm.vm.types.I64;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExecutionEnvironment {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int MAX_INSTRUCTIONS = 262144;
    private static final int TRAP_HOLD_TIME = 60000;

    private static final Map<Platform, Integer> platformMap = new ImmutableMap.Builder<Platform, Integer>()
            .put(Platform.TWITCH, 1)
            .put(Platform.DISCORD, 2)
            .put(Platform.SLACK, 3)
            .put(Platform.MIXER, 4)
            .put(Platform.YOUTUBE, 5)
            .put(Platform.CURSE, 6)
            .put(Platform.API, 7)
            .build();

    private static final int UINT8_T_SIZE = 1;
    private static final int INT32_T_SIZE = 4;
    private static final int UINT64_T_SIZE = 8;
    private static final int PTR_SIZE = INT32_T_SIZE;
    private static final int EVENT_TYPE_SIZE = 1;
    private static final int PLATFORM_SIZE = 1;
    private static final int UUID_SIZE = 2 * UINT64_T_SIZE;
    private static final int BUFFER_SIZE = INT32_T_SIZE + PTR_SIZE;
    private static final int TENANT_SIZE = UUID_SIZE + BUFFER_SIZE;
    private static final int CHANNEL_GROUP_SIZE = BUFFER_SIZE + BUFFER_SIZE + PLATFORM_SIZE + TENANT_SIZE;
    private static final int CHANNEL_SIZE = BUFFER_SIZE + BUFFER_SIZE + CHANNEL_GROUP_SIZE;
    private static final int GLOBAL_USER_SIZE = UUID_SIZE;
    private static final int USER_SIZE = BUFFER_SIZE + BUFFER_SIZE + PLATFORM_SIZE + GLOBAL_USER_SIZE;
    private static final int PERMISSION_SIZE = BUFFER_SIZE;
    private static final int PERMISSION_SET_SIZE = INT32_T_SIZE + PTR_SIZE;
    private static final int MESSAGE_SIZE = CHANNEL_SIZE + USER_SIZE + BUFFER_SIZE + PERMISSION_SET_SIZE;
    private static final int EVENT_MESSAGE_SIZE = MESSAGE_SIZE;
    private static final int EVENT_SCHEDULER_NOTIFY_SIZE = BUFFER_SIZE;
    private static final int DISCORD_VOICE_STATE_SIZE = 4 * BUFFER_SIZE + 5 * UINT8_T_SIZE;
    private static final int EVENT_DISCORD_VOICE_STATE_SIZE = 2 * DISCORD_VOICE_STATE_SIZE;
    private static final int EVENT_HTTP_RESPONSE_SIZE = 2 * INT32_T_SIZE + PTR_SIZE + 2 * BUFFER_SIZE;
    private static final int EVENT_SIZE = EVENT_TYPE_SIZE + Ints.max(EVENT_MESSAGE_SIZE, EVENT_SCHEDULER_NOTIFY_SIZE, EVENT_DISCORD_VOICE_STATE_SIZE, EVENT_HTTP_RESPONSE_SIZE);
    private static final int TENANT_SPEC_SIZE = TENANT_SIZE + INT32_T_SIZE + PTR_SIZE;
    private static final int CHANNEL_GROUP_SPEC_SIZE = CHANNEL_GROUP_SIZE + INT32_T_SIZE + PTR_SIZE;
    private static final int HEADER_SIZE = 2 * BUFFER_SIZE;

    private final WebAssembly module;
    private final Tenant tenant;
    private final WebAssemblyUserConfiguration configuration;
    private final RRateLimiter kvstoreLimiter;
    private final RRateLimiter chatLimiter;
    private final RRateLimiter logLimiter;
    private final RRateLimiter httpLimiter;
    private final RRateLimiter displayNameLimiter;
    private final RRateLimiter schedulerLimiter;
    private WebAssemblyVirtualMachine virtualMachine;
    private Event currentEvent;
    private boolean trapped;
    private long lastTrapped;

    public static class CacheKey {
        private final Tenant tenant;
        private final WebAssemblyUserConfiguration configuration;

        public CacheKey(final Tenant tenant, final WebAssemblyUserConfiguration configuration) {
            this.tenant = tenant;
            this.configuration = configuration;
        }

        public Tenant getTenant() {
            return tenant;
        }

        public WebAssemblyUserConfiguration getConfiguration() {
            return configuration;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final CacheKey cacheKey = (CacheKey) o;
            return Objects.equal(tenant, cacheKey.tenant) &&
                    Objects.equal(configuration, cacheKey.configuration);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(tenant, configuration);
        }
    }

    public ExecutionEnvironment(final WebAssembly module, final Tenant tenant, final WebAssemblyUserConfiguration configuration) {
        this.module = module;
        this.tenant = tenant;
        this.configuration = configuration;
        final String rateLimitPrefix = "Ratelimiter|Module|" + module.getName() + "|" + tenant.getId();
        this.kvstoreLimiter = module.getRedisson().getRateLimiter(rateLimitPrefix + "|kvstore");
        this.kvstoreLimiter.trySetRate(RateType.OVERALL, 300, 1, RateIntervalUnit.MINUTES);
        this.chatLimiter = module.getRedisson().getRateLimiter(rateLimitPrefix + "|chat");
        this.chatLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.MINUTES);
        this.logLimiter = module.getRedisson().getRateLimiter(rateLimitPrefix + "|log");
        this.logLimiter.trySetRate(RateType.OVERALL, 1000, 1, RateIntervalUnit.HOURS);
        this.httpLimiter = module.getRedisson().getRateLimiter(rateLimitPrefix + "|http");
        this.httpLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.MINUTES);
        this.displayNameLimiter = module.getRedisson().getRateLimiter(rateLimitPrefix + "|displayName");
        this.displayNameLimiter.trySetRate(RateType.OVERALL, 50, 1, RateIntervalUnit.MINUTES);
        this.schedulerLimiter = module.getRedisson().getRateLimiter(rateLimitPrefix + "|scheduler");
        this.schedulerLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.MINUTES);
        reset();
    }

    public static ExecutionEnvironment build(final WebAssembly module, final Tenant tenant, final WebAssemblyUserConfiguration configuration) {
        return new ExecutionEnvironment(module, tenant, configuration);
    }

    public synchronized void handleEvent(final Event event) {
        if(isTrapped())
            return;
        final Event previousEvent = currentEvent;
        this.currentEvent = event;
        try {
            virtualMachine.callAllExports("event_handler", MAX_INSTRUCTIONS);
        } catch(final TrapException e) {
            handleTrapped(e);
        }
        this.currentEvent = previousEvent;
    }

    public byte[] getCurrentEvent(final int baseAddress) {
        if(currentEvent == null)
            return null;
        if(currentEvent instanceof ChatMessageEvent)
            return encode((ChatMessageEvent) currentEvent, baseAddress);
        if(currentEvent instanceof SchedulerNotifyEvent)
            return encode((SchedulerNotifyEvent) currentEvent, baseAddress);
        if(currentEvent instanceof DiscordVoiceStateEvent)
            return encode((DiscordVoiceStateEvent) currentEvent, baseAddress);
        if(currentEvent instanceof HttpResponseEvent)
            return encode((HttpResponseEvent) currentEvent, baseAddress);
        return null;
    }

    public byte[] getCurrentEncodedTenant(final int baseAddress) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(TENANT_SPEC_SIZE);
            final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
            dynamicAllocations.write(writeTenant(byteArrayOutputStream, tenant, baseAddress + TENANT_SPEC_SIZE + dynamicAllocations.size()));
            dynamicAllocations.write(writeChannelGroups(byteArrayOutputStream, tenant.getChannelGroups(), baseAddress + TENANT_SPEC_SIZE + dynamicAllocations.size()));
            byteArrayOutputStream.write(dynamicAllocations.toByteArray());
            return byteArrayOutputStream.toByteArray();
        } catch(final IOException e) {
            return null;
        }
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Optional<User> getUser(final Platform platform, final String userId) {
        final tv.v1x1.common.dto.db.GlobalUser user = module.getDaoManager().getDaoGlobalUser().getByUser(platform, userId);
        if(user == null)
            return Optional.empty();
        return user.toCore().getUser(platform, userId);
    }

    public WebAssembly getModule() {
        return module;
    }

    public String getConfigurationHash() {
        return hash(Joiner.on("\0").join(configuration.getModules().entrySet().stream().map(entry -> entry.getKey() + "\0" + entry.getValue().getData()).collect(Collectors.toList())));
    }

    public RRateLimiter getKvstoreLimiter() {
        return kvstoreLimiter;
    }

    public RRateLimiter getChatLimiter() {
        return chatLimiter;
    }

    public RRateLimiter getLogLimiter() {
        return logLimiter;
    }

    public RRateLimiter getHttpLimiter() {
        return httpLimiter;
    }

    public RRateLimiter getDisplayNameLimiter() {
        return displayNameLimiter;
    }

    public RRateLimiter getSchedulerLimiter() {
        return schedulerLimiter;
    }

    private String hash(final String string) {
        return DigestUtils.sha512Hex(string);
    }

    private byte[] encode(final ChatMessageEvent currentEvent, final int baseAddress) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(EVENT_SIZE);
            final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
            byteArrayOutputStream.write(0);
            dynamicAllocations.write(writeChannel(byteArrayOutputStream, currentEvent.getChatMessage().getChannel(), baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            dynamicAllocations.write(writeUser(byteArrayOutputStream, currentEvent.getChatMessage().getSender(), baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            dynamicAllocations.write(writeBuffer(byteArrayOutputStream, currentEvent.getChatMessage().getText().getBytes(), baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            dynamicAllocations.write(writePermissionSet(byteArrayOutputStream, currentEvent.getChatMessage().getPermissions(), baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            byteArrayOutputStream.write(new byte[EVENT_SIZE - EVENT_TYPE_SIZE - EVENT_MESSAGE_SIZE]);
            byteArrayOutputStream.write(dynamicAllocations.toByteArray());
            return byteArrayOutputStream.toByteArray();
        } catch(final IOException e) {
            return null;
        }
    }

    private byte[] encode(final SchedulerNotifyEvent currentEvent, final int baseAddress) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(EVENT_SIZE);
            final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
            byteArrayOutputStream.write(1);
            dynamicAllocations.write(writeBuffer(byteArrayOutputStream, CompositeKey.getKeys(currentEvent.getPayload())[1], baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            byteArrayOutputStream.write(new byte[EVENT_SIZE - EVENT_TYPE_SIZE - EVENT_SCHEDULER_NOTIFY_SIZE]);
            byteArrayOutputStream.write(dynamicAllocations.toByteArray());
            return byteArrayOutputStream.toByteArray();
        } catch(final IOException e) {
            return null;
        }
    }

    private byte[] encode(final DiscordVoiceStateEvent currentEvent, final int baseAddress) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(EVENT_SIZE);
            final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
            byteArrayOutputStream.write(2);
            dynamicAllocations.write(writeDiscordVoiceState(byteArrayOutputStream, currentEvent.getOldVoiceState(), baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            dynamicAllocations.write(writeDiscordVoiceState(byteArrayOutputStream, currentEvent.getNewVoiceState(), baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            byteArrayOutputStream.write(new byte[EVENT_SIZE - EVENT_TYPE_SIZE - EVENT_DISCORD_VOICE_STATE_SIZE]);
            byteArrayOutputStream.write(dynamicAllocations.toByteArray());
            return byteArrayOutputStream.toByteArray();
        } catch(final IOException e) {
            return null;
        }
    }

    private byte[] encode(final HttpResponseEvent currentEvent, final int baseAddress) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(EVENT_SIZE);
            final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
            byteArrayOutputStream.write(3);
            byteArrayOutputStream.write(new I32(currentEvent.getResponseCode()).bytes());
            dynamicAllocations.write(writeHeaders(byteArrayOutputStream, currentEvent.getHeaders(), baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            dynamicAllocations.write(writeBuffer(byteArrayOutputStream, currentEvent.getBody(), baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            dynamicAllocations.write(writeBuffer(byteArrayOutputStream, currentEvent.getEventPayload(), baseAddress + EVENT_SIZE + dynamicAllocations.size()));
            byteArrayOutputStream.write(new byte[EVENT_SIZE - EVENT_TYPE_SIZE - EVENT_HTTP_RESPONSE_SIZE]);
            byteArrayOutputStream.write(dynamicAllocations.toByteArray());
            return byteArrayOutputStream.toByteArray();
        } catch(final IOException e) {
            return null;
        }
    }

    private byte[] writeChannel(final ByteArrayOutputStream byteArrayOutputStream, final Channel channel, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, channel.getId().getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, channel.getDisplayName().getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writeChannelGroup(byteArrayOutputStream, channel.getChannelGroup(), baseAddress + dynamicAllocations.size()));
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeChannelGroup(final ByteArrayOutputStream byteArrayOutputStream, final ChannelGroup channelGroup, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, channelGroup.getId().getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, channelGroup.getDisplayName().getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writePlatform(byteArrayOutputStream, channelGroup.getPlatform()));
        dynamicAllocations.write(writeTenant(byteArrayOutputStream, channelGroup.getTenant(), baseAddress + dynamicAllocations.size()));
        return dynamicAllocations.toByteArray();
    }

    private byte[] writePlatform(final ByteArrayOutputStream byteArrayOutputStream, final Platform platform) throws IOException {
        byteArrayOutputStream.write(platformMap.get(platform));
        return new byte[0];
    }

    private byte[] writeTenant(final ByteArrayOutputStream byteArrayOutputStream, final Tenant tenant, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        dynamicAllocations.write(writeUuid(byteArrayOutputStream, tenant.getId()));
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, tenant.getDisplayName().getBytes(), baseAddress + dynamicAllocations.size()));
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeUuid(final ByteArrayOutputStream byteArrayOutputStream, final UUID uuid) throws IOException {
        byteArrayOutputStream.write(new I64(uuid.getValue().getMostSignificantBits()).bytes());
        byteArrayOutputStream.write(new I64(uuid.getValue().getLeastSignificantBits()).bytes());
        return new byte[0];
    }

    private byte[] writeUser(final ByteArrayOutputStream byteArrayOutputStream, final User user, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, user.getId().getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, user.getDisplayName().getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writePlatform(byteArrayOutputStream, user.getPlatform()));
        dynamicAllocations.write(writeGlobalUser(byteArrayOutputStream, user.getGlobalUser(), baseAddress + dynamicAllocations.size()));
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeGlobalUser(final ByteArrayOutputStream byteArrayOutputStream, final GlobalUser globalUser, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        dynamicAllocations.write(writeUuid(byteArrayOutputStream, globalUser.getId()));
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeHeaders(final ByteArrayOutputStream byteArrayOutputStream, final MultivaluedMap<String, Object> headers, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        int count = 0;
        for(final Map.Entry<String, List<Object>> entry : headers.entrySet())
            count += entry.getValue().size();
        byteArrayOutputStream.write(new I32(count).bytes());
        byteArrayOutputStream.write(new I32(baseAddress).bytes());
        final ByteArrayOutputStream subsequentDynamicAllocations = new ByteArrayOutputStream();
        for(final Map.Entry<String, List<Object>> entry : headers.entrySet())
            for(final Object object : entry.getValue())
                subsequentDynamicAllocations.write(writeHeader(dynamicAllocations, entry.getKey(), object.toString(), baseAddress + (count + 1) * HEADER_SIZE + subsequentDynamicAllocations.size()));
        dynamicAllocations.write(new byte[HEADER_SIZE]);
        dynamicAllocations.write(subsequentDynamicAllocations.toByteArray());
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeHeader(final ByteArrayOutputStream byteArrayOutputStream, final String name, final String value, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, name.getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, value.getBytes(), baseAddress + dynamicAllocations.size()));
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeBuffer(final ByteArrayOutputStream byteArrayOutputStream, byte[] bytes, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        if(bytes == null)
            bytes = new byte[0];
        byteArrayOutputStream.write(new I32(bytes.length).bytes());
        byteArrayOutputStream.write(new I32(baseAddress).bytes());
        dynamicAllocations.write(bytes);
        dynamicAllocations.write(0);
        return dynamicAllocations.toByteArray();
    }

    private byte[] writePermissionSet(final ByteArrayOutputStream byteArrayOutputStream, final List<Permission> permissions, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        byteArrayOutputStream.write(new I32(permissions.size()).bytes());
        byteArrayOutputStream.write(new I32(baseAddress).bytes());
        final ByteArrayOutputStream subsequentDynamicAllocations = new ByteArrayOutputStream();
        for(final Permission permission : permissions)
            subsequentDynamicAllocations.write(writePermission(dynamicAllocations, permission, baseAddress + (permissions.size() + 1) * PERMISSION_SIZE + subsequentDynamicAllocations.size()));
        dynamicAllocations.write(new byte[PERMISSION_SIZE]);
        dynamicAllocations.write(subsequentDynamicAllocations.toByteArray());
        return dynamicAllocations.toByteArray();
    }

    private byte[] writePermission(final ByteArrayOutputStream byteArrayOutputStream, final Permission permission, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, permission.getNode().getBytes(), baseAddress + dynamicAllocations.size()));
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeDiscordVoiceState(final ByteArrayOutputStream byteArrayOutputStream, final VoiceState voiceState, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, (voiceState == null || voiceState.getGuildId() == null) ? null : voiceState.getGuildId().getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, (voiceState == null || voiceState.getChannelId() == null) ? null : voiceState.getChannelId().getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, (voiceState == null || voiceState.getUserId() == null) ? null : voiceState.getUserId().getBytes(), baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writeBuffer(byteArrayOutputStream, (voiceState == null || voiceState.getSessionId() == null) ? null : voiceState.getSessionId().getBytes(), baseAddress + dynamicAllocations.size()));
        byteArrayOutputStream.write(voiceState == null ? -1 : voiceState.isDeaf() ? 1 : 0);
        byteArrayOutputStream.write(voiceState == null ? -1 : voiceState.isMute() ? 1 : 0);
        byteArrayOutputStream.write(voiceState == null ? -1 : voiceState.isSelfDeaf() ? 1 : 0);
        byteArrayOutputStream.write(voiceState == null ? -1 : voiceState.isSelfMute() ? 1 : 0);
        byteArrayOutputStream.write(voiceState == null ? -1 : voiceState.isSuppress() ? 1 : 0);
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeChannelGroups(final ByteArrayOutputStream byteArrayOutputStream, final List<ChannelGroup> channelGroups, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        byteArrayOutputStream.write(new I32(channelGroups.size()).bytes());
        byteArrayOutputStream.write(new I32(baseAddress).bytes());
        final ByteArrayOutputStream subsequentDynamicAllocations = new ByteArrayOutputStream();
        for(final ChannelGroup channelGroup : channelGroups)
            subsequentDynamicAllocations.write(writeChannelGroupSpec(dynamicAllocations, channelGroup, baseAddress + (channelGroups.size() + 1) * CHANNEL_GROUP_SPEC_SIZE + subsequentDynamicAllocations.size()));
        dynamicAllocations.write(new byte[CHANNEL_GROUP_SPEC_SIZE]);
        dynamicAllocations.write(subsequentDynamicAllocations.toByteArray());
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeChannelGroupSpec(final ByteArrayOutputStream byteArrayOutputStream, final ChannelGroup channelGroup, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        dynamicAllocations.write(writeChannelGroup(byteArrayOutputStream, channelGroup, baseAddress + dynamicAllocations.size()));
        dynamicAllocations.write(writeChannels(byteArrayOutputStream, channelGroup.getChannels(), baseAddress + dynamicAllocations.size()));
        return dynamicAllocations.toByteArray();
    }

    private byte[] writeChannels(final ByteArrayOutputStream byteArrayOutputStream, final List<Channel> channels, final int baseAddress) throws IOException {
        final ByteArrayOutputStream dynamicAllocations = new ByteArrayOutputStream();
        byteArrayOutputStream.write(new I32(channels.size()).bytes());
        byteArrayOutputStream.write(new I32(baseAddress).bytes());
        final ByteArrayOutputStream subsequentDynamicAllocations = new ByteArrayOutputStream();
        for(final Channel channel : channels)
            subsequentDynamicAllocations.write(writeChannel(dynamicAllocations, channel, baseAddress + (channels.size() + 1) * CHANNEL_SIZE + subsequentDynamicAllocations.size()));
        dynamicAllocations.write(new byte[CHANNEL_SIZE]);
        dynamicAllocations.write(subsequentDynamicAllocations.toByteArray());
        return dynamicAllocations.toByteArray();
    }

    private void handleTrapped(final TrapException e) {
        this.trapped = true;
        this.lastTrapped = System.currentTimeMillis();
        LOG.warn("VM Trapped for {}", tenant, e);
        LOG.info("VM State: {}", virtualMachine.dumpString());
    }

    private boolean isTrapped() {
        if(trapped && lastTrapped < System.currentTimeMillis() - TRAP_HOLD_TIME)
            reset();
        return trapped;
    }

    private void reset() {
        this.currentEvent = null;
        this.trapped = false;
        virtualMachine = WebAssemblyVirtualMachine.build();
        final ModuleDef syscallDef = new SyscallWebAssemblyModuleDef(this);
        final ModuleDef moduleDef = new V1x1WebAssemblyModuleDef(this);
        try {
            virtualMachine.loadModules(syscallDef, moduleDef);
            for(final Map.Entry<String, ModuleUserConfiguration> entry : configuration.getModules().entrySet())
                virtualMachine.loadModules(ModuleDef.fromString(entry.getKey(), entry.getValue().getData()));
        } catch (final ValidationException | LinkingException | TrapException | IOException e) {
            LOG.warn("Exception caught trying to load WebAssembly modules for {}", tenant, e);
        }
    }
}
