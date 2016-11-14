package tv.v1x1.modules.channel.timed_messages;


import com.google.common.primitives.Ints;
import org.redisson.api.RMapCache;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.streams.StreamResponse;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.modules.channel.timed_messages.commands.TimerCommand;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Josh
 */ // TODO: Handle (hard) disabled but saved timers
    // TODO: Handle (soft) enable/disable of timers for offline strimmers
public class TimedMessages extends RegisteredThreadedModule<TimedMessagesSettings, TimedMessagesGlobalConfiguration, TimedMessagesTenantConfiguration, TimedMessagesChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static {
        Module module = new Module("timed_messages");
        // base/shared
        I18n.registerDefault(module, "invalid.subcommand", "%commander%, I don't know that subcommand. Usage: %usage%");
        I18n.registerDefault(module, "invalid.timer", "%commander%, I can't find a timer called \"%id%\".");
        I18n.registerDefault(module, "toomanyargs", "Whoa settle down there, %commander%. There's too many things there. Usage: %usage%");

        // subcmd success
        I18n.registerDefault(module, "create.success", "%commander%, I've created a timer named \"%id%\". Now add stuff to it!");
        I18n.registerDefault(module, "add.success", "%commander%, I've added your entry (\"%preview%\") to the rotation.");
        I18n.registerDefault(module, "delete.success", "%commander%, I've deleted the following entry from the rotation: %preview%");
        I18n.registerDefault(module, "destroy.success", "%commander%, I've destroyed the timer named \"%id%\". It will no longer message the chat.");
        I18n.registerDefault(module, "enable.success", "%commander%, I've re-enabled the timer \"%id%\" rotation.");
        I18n.registerDefault(module, "disable.success", "%commander%, I've disabled the timer \"%id%\" rotation.");

        // subcmd failure
        I18n.registerDefault(module, "create.alreadyexists", "%commander%, there's already a timer named \"%id%\". Do you wanna to add entries with %cmd%?");
        I18n.registerDefault(module, "create.notarget", "%commander%, what's the name of the timer we're creating? Usage: %usage%");
        I18n.registerDefault(module, "create.badinterval", "%commander%, that interval doesn't look right. Try a number, which will be the number of seconds between each message.");
        I18n.registerDefault(module, "add.nomessage", "%commander%, I'd love to add a timer, but what should it say? Usage: %usage%");
        I18n.registerDefault(module, "add.notarget", "%commander%, add to what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "delete.notarget", "%commander%, delete from what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "delete.nomessage", "%commander%, what are we deleting from the timer? Usage: %usage%");
        I18n.registerDefault(module, "delete.nomatch", "%commander%, I can't find a timer entry that starts with \"%preview%\"");
        I18n.registerDefault(module, "delete.toomanymatches", "%commander%, there's %count% things that start with \"%preview%\" in \"%id%\". Can you be more specific?");
        I18n.registerDefault(module, "destroy.notarget", "%commander%, destroy what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "alreadytoggled", "%commander%, the \"%id%\" timer is already %state%");
    }

    CommandDelegator delegator;
    private SchedulerServiceClient ssc;
    private RMapCache<byte[], byte[]> cursors; // uuid -> int

    @Override
    public String getName() {
        return "timed_messages";
    }
    public static void main(final String[] args) throws Exception {
        new TimedMessages().entryPoint(args);
    }

    @Override
    protected void initialize() {
        super.initialize();
        cursors = getRedisson().getMapCache("Modules|Channel|TimedMessages|Cursors", ByteArrayCodec.INSTANCE);
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new TimerCommand(this));
        registerListener(new TimedMessagesListener(this));
        ssc = getServiceClient(SchedulerServiceClient.class);
    }

    /**
     * Create a timer, add it to DB + scheduler
     * @param tenant
     * @param name
     * @param timer
     * @return
     */
    public boolean createTimer(final Tenant tenant, final String name, final Timer timer) {
        TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        boolean success = config.addTimer(name, timer);
        if(success) {
            getTenantConfigProvider().save(tenant, config);
            enableTimer(tenant, name);
        }
        return success;
    }

    /**
     * Remove a timer from DB + scheduler
     * @param tenant
     * @param timerName
     * @return
     */
    public boolean destroyTimer(final Tenant tenant, final String timerName) {
        TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer t = getTenantConfiguration(tenant).getTimer(timerName);
        if(t == null) return false;
        boolean success = getTenantConfiguration(tenant).delTimer(timerName);
        if(success) {
            final UUID activeTimer = t.getDtoActiveTimer();
            if(activeTimer.getValue() != null)
                disableTimer(activeTimer);
            getTenantConfigProvider().save(tenant, config);
        }
        return success;
    }

    public boolean addTimerEntry(final Tenant tenant, final String timerName, final String message) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) return false;
        boolean success = t.getEntries().add(new TimerEntry(message));
        if(success)
            getTenantConfigProvider().save(tenant, config);
        return success;
    }

    public int countMatchingTimerEntries(final Tenant tenant, final String timerName, final String message) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) return -1;
        int found = 0;
        int entryIdx = 0;
        List<TimerEntry> entries = t.getEntries();
        for(; entryIdx < entries.size(); ++entryIdx) {
            TimerEntry entry = entries.get(entryIdx);
            if(entry.getMessage().startsWith(message))
                ++found;
        }
        return found;
    }

    public TimerEntry deleteTimerEntry(final Tenant tenant, final String timerName, final String message) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) return null;
        boolean found = false;
        int entryIdx = 0;
        List<TimerEntry> entries = t.getEntries();
        for(; entryIdx < entries.size(); ++entryIdx) {
            TimerEntry entry = entries.get(entryIdx);
            if(entry.getMessage().startsWith(message)) {
                found = true;
                break;
            }
        }
        if(found) {
            TimerEntry entry = entries.remove(entryIdx);
            getTenantConfigProvider().save(tenant, config);
            return entry;
        }
        return null;
    }


    /**
     * Find a timer in the DB and add it to the scheduler
     * @param tenant
     * @param timerName
     */
    public void enableTimer(final Tenant tenant, final String timerName) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) {
            LOG.warn("Asked to kick off a non-existing timer.");
            return;
        }
        final UUID uuid = new UUID(java.util.UUID.randomUUID());
        t.setActiveTimer(uuid.getValue());
        cursors.fastPut(uuid.toProto().toByteArray(), Ints.toByteArray(0)); // , t.getInterval() + (t.getInterval() / 2), TimeUnit.MILLISECONDS
        final byte[] payload = CompositeKey.makeKey(new byte[][]{timerName.getBytes(), "tenant".getBytes(), tenant.toProto().toByteArray()});
        ssc.scheduleRepeating(t.getInterval(), uuid, payload);
        getTenantConfigProvider().save(tenant, config);
    }

    /**
     * Remove a timer from the scheduler but leaves it in the DB
     * @param timerId
     */
    public void disableTimer(final UUID timerId) {
        LOG.trace("asked to remove timer {}", timerId.getValue());
        //cursors.fastRemove(timerId.toProto().toByteArray());
        cursors.fastPut(timerId.toProto().toByteArray(), Ints.toByteArray(-1), 1, TimeUnit.MILLISECONDS);
        ssc.scheduleRepeating(-1, timerId, "foobar".getBytes());
    }

    /* pkg-private */ int getCursor(final UUID id) {
        final byte[] cursorBytes = cursors.get(id.toProto().toByteArray());
        if(cursorBytes == null) return -1;
        return Ints.fromByteArray(cursorBytes);
    }

    public void saveCursor(final UUID timerId, final int cursor) {
        cursors.fastPut(timerId.toProto().toByteArray(), Ints.toByteArray(cursor));
    }

    public boolean isEnabled(final Channel channel) {
        return getTenantConfiguration(channel.getTenant()).isEnabled();
    }

    public boolean isStreaming(final Channel channel) {
        if(!channel.getPlatform().equals(Platform.TWITCH))
            throw new IllegalArgumentException("Requested platform doesn't support streaming: " + channel.getPlatform().name());
        return (getTwitchApi().getStreams().getStream(channel.getId().substring(1, channel.getId().length())).getStream() != null);

    }
}
