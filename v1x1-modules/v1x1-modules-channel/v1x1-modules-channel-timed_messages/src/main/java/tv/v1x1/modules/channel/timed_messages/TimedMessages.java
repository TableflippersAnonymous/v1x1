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
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.modules.channel.timed_messages.commands.TimerCommand;
import tv.v1x1.modules.channel.timed_messages.config.TimedMessagesChannelConfiguration;
import tv.v1x1.modules.channel.timed_messages.config.TimedMessagesGlobalConfiguration;
import tv.v1x1.modules.channel.timed_messages.config.TimedMessagesSettings;
import tv.v1x1.modules.channel.timed_messages.config.TimedMessagesTenantConfiguration;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Josh
 */
    // TODO: Handle (soft) enable/disable of timers for offline strimmers
public class TimedMessages extends RegisteredThreadedModule<TimedMessagesSettings, TimedMessagesGlobalConfiguration, TimedMessagesTenantConfiguration, TimedMessagesChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static {
        Module module = new Module("timed_messages");
        // base/shared
        I18n.registerDefault(module, "invalid.subcommand", "%commander%, I don't know that subcommand. Usage: %usage%");
        I18n.registerDefault(module, "help", "(Hint: You can get a list of commands with !timer help)");
        I18n.registerDefault(module, "invalid.timer", "%commander%, I can't find a timer called \"%id%\".");
        I18n.registerDefault(module, "invalid.interval", "%commander%, that interval doesn't look right. Try a number, greater than ten, in seconds.");
        I18n.registerDefault(module, "toomanyargs", "Whoa settle down there, %commander%. There's too many things there. Usage: %usage%");

        // subcmd success
        I18n.registerDefault(module, "create.success", "%commander%, I've created a timer named \"%id%\". Now add stuff to it!");
        I18n.registerDefault(module, "add.success", "%commander%, I've added your entry (\"%preview%\") to the rotation.");
        I18n.registerDefault(module, "remove.success", "%commander%, I've removed the following entry from the rotation: %preview%");
        I18n.registerDefault(module, "destroy.success", "%commander%, I've destroyed the rotation named \"%id%\". It will no longer message the chat.");
        I18n.registerDefault(module, "enable.success", "%commander%, I've re-enabled the \"%id%\" rotation.");
        I18n.registerDefault(module, "disable.success", "%commander%, I've disabled the \"%id%\" rotation.");
        I18n.registerDefault(module, "list.success", "%commander%, here's the list of timers: %timers%");
        I18n.registerDefault(module, "list.empty", "%commander%, there are no timers set up.");
        I18n.registerDefault(module, "info.noentries", "%commander%, \"%id%\" is set up to message every %" +
                "interval% seconds, but there's no timer entries in the rotation");
        I18n.registerDefault(module, "info.success", "%commander%, \"%id%\" is set up to message every %in" +
                "terval% seconds. It's currently %enabled%. Here's a list of timer entries: %entries%");
        I18n.registerDefault(module, "reschedule.success", "%commander%, \"%id%\" will now message every %interval% seconds.");
        I18n.registerDefault(module, "help.blurb", "Timers send messages to the channel at set intervals. " +
                "Timers have two parts: Rotations and a list messages which you add to a rotation. First, create a rotat" +
                "ion with \"create,\" then add stuff to it using \"add\"");

        // subcmd failure
        I18n.registerDefault(module, "create.alreadyexists", "%commander%, there's already a timer named \"%id%\". Do you wanna to add entries with !timer add?");
        I18n.registerDefault(module, "create.notarget", "%commander%, what's the name of the timer we're creating? Usage: %usage%");
        I18n.registerDefault(module, "add.nomessage", "%commander%, I'd love to add a timer, but what should it say? Usage: %usage%");
        I18n.registerDefault(module, "add.notarget", "%commander%, add to what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "remove.notarget", "%commander%, remove from what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "remove.nomessage", "%commander%, what are we deleting from the timer? Usage: %usage%");
        I18n.registerDefault(module, "remove.nomatch", "%commander%, I can't find a timer entry that starts with \"%preview%\"");
        I18n.registerDefault(module, "remove.toomanymatches", "%commander%, there's %count% things that start with \"%preview%\" in \"%id%\". Can you be more specific?");
        I18n.registerDefault(module, "destroy.notarget", "%commander%, destroy what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "enable.notarget", "%commander%, what timer should I enable?");
        I18n.registerDefault(module, "disable.notarget", "%commander%, what timer should I disable?");
        I18n.registerDefault(module, "reschedule.notarget", "%commander%, what timer should I reschedule? Usage: %usage%");
        I18n.registerDefault(module, "info.notarget", "%commander%, what timer are you curious about? Usage: %usage%");
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
     * @return true if successful, false if timer already exists
     * @throws IllegalArgumentException if interval is too low
     * TODO: This should be taking in timer args instead of a Timer
     */
    public boolean createTimer(final Tenant tenant, final String name, final Timer timer) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        if(timer.getInterval() < 1000)
            throw new IllegalArgumentException("Refusing to create a timer with sub-second interval");
        final boolean success = config.addTimer(name, timer);
        if(success) {
            getTenantConfigProvider().save(tenant, config);
            unpauseTimer(tenant, name);
        }
        return success;
    }

    /**
     * Get all timer names
     * @param tenant
     * @return set of timer names
     */
    public Set<String> listTimers(final Tenant tenant) {
        return getTenantConfiguration(tenant).getTimers().keySet();
    }

    /**
     * Get a single timer
     * @param tenant
     * @param timerName
     * @return a Timer, or null if not found
     */
    public Timer getTimer(final Tenant tenant, final String timerName) {
        return getTenantConfiguration(tenant).getTimers().get(timerName);
    }

    /**
     * set a timer to run on a new interval
     * @param tenant
     * @param timerName
     * @param interval
     * @return true if successful; false if timer not found
     */
    public boolean rescheduleTimer(final Tenant tenant, final String timerName, final long interval) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer timer = config.getTimer(timerName);
        if(timer == null) return false;
        if(interval < 1000)
            throw new IllegalArgumentException("Refusing to create a timer with sub-second interval");
        timer.setInterval(interval);
        getTenantConfigProvider().save(tenant, config);
        if(timer.isEnabled()) {
            pauseTimer(new UUID(timer.getActiveTimer()));
            unpauseTimer(tenant, timerName);
        }
        return true;
    }

    /**
     * enable or disable a timer, triggering a schedule or unschedule task
     * @param tenant
     * @param timerName
     * @param enabled
     * @return true on success; false if timer doesn't exist or is already in state
     */
    public boolean enableTimer(final Tenant tenant, final String timerName, final boolean enabled) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer timer = config.getTimer(timerName);
        if(timer == null) return false;
        if(timer.isEnabled() == enabled) return false;
        timer.setEnabled(enabled);
        getTenantConfigProvider().save(tenant, config);
        if(enabled) {
            unpauseTimer(tenant, timerName);
        } else {
            if(timer.getActiveTimer() == null) throw new IllegalStateException("Timer marked as enabled with no scheduled task");
            final UUID scheduledId = new UUID(timer.getActiveTimer());
            pauseTimer(scheduledId);
        }
        return true;
    }
    /**
     * Remove a timer from DB + scheduler
     * @param tenant
     * @param timerName
     * @return true if destroyed; false if it doesn't exist
     */
    public boolean destroyTimer(final Tenant tenant, final String timerName) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) return false;
        final boolean success = config.delTimer(timerName);
        if(success) {
            final UUID activeTimer = t.getDtoActiveTimer();
            if(activeTimer.getValue() != null)
                pauseTimer(activeTimer);
            getTenantConfigProvider().save(tenant, config);
        }
        return success;
    }

    public boolean addTimerEntry(final Tenant tenant, final String timerName, final String message) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) return false;
        final boolean success = t.getEntries().add(new TimerEntry(message));
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
        final List<TimerEntry> entries = t.getEntries();
        for(; entryIdx < entries.size(); ++entryIdx) {
            final TimerEntry entry = entries.get(entryIdx);
            if(entry.getMessage().startsWith(message))
                ++found;
        }
        return found;
    }

    public TimerEntry removeTimerEntry(final Tenant tenant, final String timerName, final String message) {
        final TimedMessagesTenantConfiguration config = getTenantConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) return null;
        boolean found = false;
        int entryIdx = 0;
        final List<TimerEntry> entries = t.getEntries();
        for(; entryIdx < entries.size(); ++entryIdx) {
            final TimerEntry entry = entries.get(entryIdx);
            if(entry.getMessage().startsWith(message)) {
                found = true;
                break;
            }
        }
        if(found) {
            final TimerEntry entry = entries.remove(entryIdx);
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
    public void unpauseTimer(final Tenant tenant, final String timerName) {
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
    public void pauseTimer(final UUID timerId) {
        LOG.trace("asked to pause timer {}", timerId.getValue());
        //cursors.fastRemove(timerId.toProto().toByteArray());
        cursors.fastPut(timerId.toProto().toByteArray(), Ints.toByteArray(-1), 1, TimeUnit.MILLISECONDS);
        ssc.scheduleRepeating(-1, timerId, "foobar".getBytes());
    }

    /* pkg-private */ int getCursor(final UUID id) {
        final byte[] cursorBytes = cursors.get(id.toProto().toByteArray());
        if(cursorBytes == null) return -1;
        return Ints.fromByteArray(cursorBytes);
    }

    /* pkg-private */ void saveCursor(final UUID timerId, final int cursor) {
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
