package tv.v1x1.modules.channel.timed_messages;


import com.google.common.primitives.Ints;
import org.redisson.api.RMapCache;
import org.redisson.client.codec.ByteArrayCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
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
public class TimedMessages extends RegisteredThreadedModule<TimedMessagesSettings, TimedMessagesGlobalConfiguration, TimedMessagesTenantConfiguration> {
    public static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static {
        Module module = new Module("timed_messages");
        /*
        TODO: Add these messages
        timer.create.toomanyargs
        entry.add.badtarget - commander, id
         */
        // base/shared
        I18n.registerDefault(module, "invalid.subcommand", "%commander%, I don't know that subcommand. Usage: %usage%");
        I18n.registerDefault(module, "invalid.timer", "%commander%, I can't find a timer called \"%id%.\"");

        // subcmd success
        I18n.registerDefault(module, "timer.create.success", "%commander%, I've created a timer named \"%id%\". Now add stuff to it!");
        I18n.registerDefault(module, "entry.add.success", "%commander%, I've added your entry to the rotation. Here's a preview: %preview%");
        I18n.registerDefault(module, "entry.remove.success", "%commander%, I've removed the following entry from the rotation: %preview%");
        I18n.registerDefault(module, "timer.destroy.success", "%commander%, I've destroyed the timer named \"%id%\". It will no longer message the chat.");
        I18n.registerDefault(module, "timer.enable.success", "%commander%, I've re-enabled the timer \"%id%\" rotation.");
        I18n.registerDefault(module, "timer.disable.success", "%commander%, I've disabled the timer \"%id%\" rotation.");

        // subcmd failure
        I18n.registerDefault(module, "timer.create.alreadyexists", "%commander%, there's already a timer named \"%id%\". Do you wanna to add entries with %cmd%?");
        I18n.registerDefault(module, "timer.create.notarget", "%commander%, what's the name of the timer we're creating? Usage: %usage%");
        I18n.registerDefault(module, "timer.create.badinterval", "%commander%, that interval doesn't look right. Try a number, which will be the number of minutes between each message.");
        I18n.registerDefault(module, "entry.nomatch", "%commander%, I can't find a timer entry that starts with %preview%");
        I18n.registerDefault(module, "entry.add.invalidmessage", "%commander%, I'd love to add a timer, but what should it say? Usage: %usage%");
        I18n.registerDefault(module, "entry.add.notarget", "%commander%, add to what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "entry.remove.notarget", "%commander%, remove from what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "timer.destroy.notarget", "%commander%, destroy what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "timer.alreadytoggled", "%commander%, the \"%id%\" timer is already %state%");
    }

    private CommandDelegator delegator;
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
            disableTimer(tenant, t.getActiveTimer());
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
        final Timer t = getTenantConfiguration(tenant).getTimer(timerName);
        if(t == null) {
            LOG.warn("Asked to kick off a non-existing timer.");
            return;
        }
        final UUID uuid = new UUID(java.util.UUID.randomUUID());
        cursors.putAsync(uuid.toProto().toByteArray(), Ints.toByteArray(0), t.getInterval() + (t.getInterval() / 2), TimeUnit.MILLISECONDS);
        final byte[] payload = CompositeKey.makeKey(new byte[][]{timerName.getBytes(), "tenant".getBytes(), tenant.toProto().toByteArray()});
        ssc.scheduleRepeating(t.getInterval(), uuid, payload);
    }

    /**
     * Remove a timer from the scheduler but leaves it in the DB
     * @param tenant
     * @param timerId
     */
    public void disableTimer(final Tenant tenant, final UUID timerId) {
        // TODO: Remove activeTimer from Timer
        cursors.removeAsync(timerId.toProto().toByteArray());
        ssc.scheduleRepeating(-1, timerId, null);
    }

    /* pkg-private */ int getCursor(final UUID id) {
        final byte[] cursorBytes = cursors.get(id.toProto().toByteArray());
        if(cursorBytes == null) return -1;
        return Ints.fromByteArray(cursorBytes);
    }

    public void saveCursor(final UUID timerId, final int cursor) {
        cursors.putAsync(timerId.toProto().toByteArray(), Ints.toByteArray(cursor));
    }
}
