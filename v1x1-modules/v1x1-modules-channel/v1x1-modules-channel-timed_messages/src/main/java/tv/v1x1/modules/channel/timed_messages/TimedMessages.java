package tv.v1x1.modules.channel.timed_messages;

import com.google.common.primitives.Ints;
import org.redisson.api.RMapCache;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.scanners.i18n.I18nDefault;
import tv.v1x1.common.scanners.i18n.I18nDefaults;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.data.CompositeKey;
import tv.v1x1.modules.channel.timed_messages.commands.TimerCommand;
import tv.v1x1.modules.channel.timed_messages.config.TimedMessagesGlobalConfiguration;
import tv.v1x1.modules.channel.timed_messages.config.TimedMessagesUserConfiguration;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Josh
 */
@Permissions({
        @RegisteredPermission(
                node = "timer.modify",
                displayName = "Manage Timers",
                description = "This allows you to use the !timers command",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS}
        )
})
@I18nDefaults({
        // base/shared
        @I18nDefault(
                key = "invalid.subcommand",
                message = "%commander%, I don't know that subcommand. Usage: %usage%",
                displayName = "Invalid Command",
                description = "Sent when an invalid command is used with !timers"
        ),
        @I18nDefault(
                key = "help",
                message = "(Hint: You can get a list of commands with !timer help)",
                displayName = "Help Hint",
                description = "Sent with !timers help"
        ),
        @I18nDefault(
                key = "invalid.timer",
                message = "%commander%, I can't find a timer called \"%id%\".",
                displayName = "Invalid Timer",
                description = "Sent when a non-existent timer is requested"
        ),
        @I18nDefault(
                key = "invalid.interval",
                message = "%commander%, that interval doesn't look right. Try a number, greater than ten, in seconds.",
                displayName = "Invalid Interval",
                description = "Sent when an invalid interval is specified"
        ),
        @I18nDefault(
                key = "toomanyargs",
                message = "Whoa settle down there, %commander%. There's too many things there. Usage: %usage%",
                displayName = "Too Many Parameters",
                description = "Sent when too many parameters are given"
        ),
        // subcmd success
        @I18nDefault(
                key = "create.success",
                message = "%commander%, I've created a timer named \"%id%\". Now add stuff to it!",
                displayName = "Created",
                description = "Sent when a timer is created"
        ),
        @I18nDefault(
                key = "add.success",
                message = "%commander%, I've added your entry (\"%preview%\") to the rotation.",
                displayName = "Added",
                description = "Sent when an entry is added to a timer"
        ),
        @I18nDefault(
                key = "remove.success",
                message = "%commander%, I've removed the following entry from the rotation: %preview%",
                displayName = "Removed",
                description = "Sent when an entry is removed from a timer"
        ),
        @I18nDefault(
                key = "destroy.success",
                message = "%commander%, I've destroyed the rotation named \"%id%\". It will no longer message the chat.",
                displayName = "Destroyed",
                description = "Sent when a timer is destroyed"
        ),
        @I18nDefault(
                key = "enable.success",
                message = "%commander%, I've re-enabled the \"%id%\" rotation.",
                displayName = "Enabled",
                description = "Sent when a timer is enabled"
        ),
        @I18nDefault(
                key = "disable.success",
                message = "%commander%, I've disabled the \"%id%\" rotation.",
                displayName = "Disabled",
                description = "Sent when a timer is disabled"
        ),
        @I18nDefault(
                key = "list.success",
                message = "%commander%, here's the list of timers: %timers%",
                displayName = "List",
                description = "Format for listing timers"
        ),
        @I18nDefault(
                key = "list.empty",
                message = "%commander%, there are no timers set up.",
                displayName = "List (Empty)",
                description = "Sent when no timers exist"
        ),
        @I18nDefault(
                key = "info.noentries",
                message = "%commander%, \"%id%\" is set up to message every %interval% seconds, but there's no timer " +
                        "entries in the rotation",
                displayName = "Info (No Entries)",
                description = "Format for describing a timer with no entries"
        ),
        @I18nDefault(
                key = "info.success",
                message = "%commander%, \"%id%\" is set up to message every %interval% seconds. It's currently %enabled%. " +
                        "Here's a list of timer entries: %entries%",
                displayName = "Info",
                description = "Format for describing a timer with entries"
        ),
        @I18nDefault(
                key = "reschedule.success",
                message = "%commander%, \"%id%\" will now message every %interval% seconds.",
                displayName = "Rescheduled",
                description = "Sent when a timer is rescheduled"
        ),
        @I18nDefault(
                key = "help.blurb",
                message = "Timers send messages to the channel at set intervals. Timers have two parts: Rotations and a " +
                        "list messages which you add to a rotation. First, create a rotation with \"create,\" then add " +
                        "stuff to it using \"add\"",
                displayName = "Help Blurb",
                description = "Sent with !timers help"
        ),
        // subcmd failure
        @I18nDefault(
                key = "create.alreadyexists",
                message = "%commander%, there's already a timer named \"%id%\". Do you wanna to add entries with !timer add?",
                displayName = "Create Failure (Already Exists)",
                description = "Sent when trying to create a timer that already exists"
        ),
        @I18nDefault(
                key = "create.notarget",
                message = "%commander%, what's the name of the timer we're creating? Usage: %usage%",
                displayName = "Create Failure (No Target)",
                description = "Sent when trying to create a timer without a target"
        ),
        @I18nDefault(
                key = "add.nomessage",
                message = "%commander%, I'd love to add a timer, but what should it say? Usage: %usage%",
                displayName = "Create Failure (No Message)",
                description = "Sent when trying to add a message to a timer without a message"
        ),
        @I18nDefault(
                key = "add.notarget",
                message = "%commander%, add to what timer? Also, what message? Usage: %usage%",
                displayName = "Add Failure (No Target)",
                description = "Sent when trying to add a message to a timer without a timer"
        ),
        @I18nDefault(
                key = "remove.notarget",
                message = "%commander%, remove from what timer? Also, what message? Usage: %usage%",
                displayName = "Remove Failure (No Target)",
                description = "Sent when trying to remove a message from a timer without a timer"
        ),
        @I18nDefault(
                key = "remove.nomessage",
                message = "%commander%, what are we deleting from the timer? Usage: %usage%",
                displayName = "Remove Failure (No Message)",
                description = "Sent when trying to remove a message from a timer without a message"
        ),
        @I18nDefault(
                key = "remove.nomatch",
                message = "%commander%, I can't find a timer entry that starts with \"%preview%\"",
                displayName = "Remove Failure (No Match)",
                description = "Sent when trying to remove a message from a timer when there is no matching message"
        ),
        @I18nDefault(
                key = "remove.toomanymatches",
                message = "%commander%, there's %count% things that start with \"%preview%\" in \"%id%\". Can you be " +
                        "more specific?",
                displayName = "Remove Failure (Multiple Matches)",
                description = "Sent when trying to remove a message from a timer when there are multiple matching messages"
        ),
        @I18nDefault(
                key = "destroy.notarget",
                message = "%commander%, destroy what timer? Also, what message? Usage: %usage%",
                displayName = "Destroy Failure (No Target)",
                description = "Sent when trying to destroy a timer without a timer"
        ),
        @I18nDefault(
                key = "enable.notarget",
                message = "%commander%, what timer should I enable?",
                displayName = "Enable Failure (No Target)",
                description = "Sent when trying to enable a timer without a timer"
        ),
        @I18nDefault(
                key = "disable.notarget",
                message = "%commander%, what timer should I disable?",
                displayName = "Disable Failure (No Target)",
                description = "Sent when trying to disable a timer without a timer"
        ),
        @I18nDefault(
                key = "reschedule.notarget",
                message = "%commander%, what timer should I reschedule? Usage: %usage%",
                displayName = "Reschedule Failure (No Target)",
                description = "Sent when trying to reschedule a timer without a timer"
        ),
        @I18nDefault(
                key = "info.notarget",
                message = "%commander%, what timer are you curious about? Usage: %usage%",
                displayName = "Info Failure (No Target)",
                description = "Sent when trying to get info on a timer without a timer"
        ),
        @I18nDefault(
                key = "alreadytoggled",
                message = "%commander%, the \"%id%\" timer is already %state%",
                displayName = "Enable/Disable Failure (Not Changed)",
                description = "Sent when trying to enable or disable a timer when the timer was already enabled or disabled"
        )
})
// TODO: Handle (soft) enable/disable of timers for offline strimmers
public class TimedMessages extends RegisteredThreadedModule<TimedMessagesGlobalConfiguration, TimedMessagesUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
        final TimedMessagesUserConfiguration config = getConfiguration(tenant);
        if(timer.getInterval() < 1000)
            throw new IllegalArgumentException("Refusing to create a timer with sub-second interval");
        final boolean success = config.addTimer(name, timer);
        if(success) {
            getUserConfigProvider().save(tenant, config);
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
        return getConfiguration(tenant).getTimers().keySet();
    }

    /**
     * Get a single timer
     * @param tenant
     * @param timerName
     * @return a Timer, or null if not found
     */
    public Timer getTimer(final Tenant tenant, final String timerName) {
        return getConfiguration(tenant).getTimers().get(timerName);
    }

    /**
     * set a timer to run on a new interval
     * @param tenant
     * @param timerName
     * @param interval
     * @return true if successful; false if timer not found
     */
    public boolean rescheduleTimer(final Tenant tenant, final String timerName, final long interval) {
        final TimedMessagesUserConfiguration config = getConfiguration(tenant);
        final Timer timer = config.getTimer(timerName);
        if(timer == null) return false;
        if(interval < 1000)
            throw new IllegalArgumentException("Refusing to create a timer with sub-second interval");
        timer.setInterval(interval);
        getUserConfigProvider().save(tenant, config);
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
        final TimedMessagesUserConfiguration config = getConfiguration(tenant);
        final Timer timer = config.getTimer(timerName);
        if(timer == null) return false;
        if(timer.isEnabled() == enabled) return false;
        timer.setEnabled(enabled);
        getUserConfigProvider().save(tenant, config);
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
        final TimedMessagesUserConfiguration config = getConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) return false;
        final boolean success = config.delTimer(timerName);
        if(success) {
            final UUID activeTimer = t.getDtoActiveTimer();
            if(activeTimer.getValue() != null)
                pauseTimer(activeTimer);
            getUserConfigProvider().save(tenant, config);
        }
        return success;
    }

    public boolean addTimerEntry(final Tenant tenant, final String timerName, final String message) {
        final TimedMessagesUserConfiguration config = getConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) return false;
        final boolean success = t.getEntries().add(new TimerEntry(message));
        if(success)
            getUserConfigProvider().save(tenant, config);
        return success;
    }

    public int countMatchingTimerEntries(final Tenant tenant, final String timerName, final String message) {
        final TimedMessagesUserConfiguration config = getConfiguration(tenant);
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
        final TimedMessagesUserConfiguration config = getConfiguration(tenant);
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
            getUserConfigProvider().save(tenant, config);
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
        final TimedMessagesUserConfiguration config = getConfiguration(tenant);
        final Timer t = config.getTimer(timerName);
        if(t == null) {
            LOG.warn("Asked to kick off a non-existing timer.");
            return;
        }
        final UUID uuid = new UUID(java.util.UUID.nameUUIDFromBytes(CompositeKey.makeKey("timed_messages", tenant.toString(), timerName)));
        cursors.fastPut(uuid.toProto().toByteArray(), Ints.toByteArray(0)); // , t.getInterval() + (t.getInterval() / 2), TimeUnit.MILLISECONDS
        final byte[] payload = CompositeKey.makeKey(new byte[][]{timerName.getBytes(), "tenant".getBytes(), tenant.toProto().toByteArray()});
        ssc.scheduleRepeating(t.getInterval(), uuid, payload);
        if(!uuid.getValue().equals(t.getActiveTimer())) {
            t.setActiveTimer(uuid.getValue());
            getUserConfigProvider().save(tenant, config);
        }
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
        return getConfiguration(channel.getChannelGroup().getTenant()).isEnabled();
    }

    public boolean isStreaming(final Channel channel) {
        if(!channel.getChannelGroup().getPlatform().equals(Platform.TWITCH))
            throw new IllegalArgumentException("Requested platform doesn't support streaming: " + channel.getChannelGroup().getPlatform().name());
        return (getTwitchApi().getStreams().getStream(channel.getId()).getStream() != null);

    }
}
