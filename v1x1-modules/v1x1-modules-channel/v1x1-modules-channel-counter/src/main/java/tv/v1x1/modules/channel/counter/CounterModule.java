package tv.v1x1.modules.channel.counter;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelGroup;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.scanners.i18n.I18nDefault;
import tv.v1x1.common.scanners.i18n.I18nDefaults;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
import tv.v1x1.modules.channel.counter.config.Counter;
import tv.v1x1.modules.channel.counter.config.CounterGlobalConfiguration;
import tv.v1x1.modules.channel.counter.config.CounterUserConfiguration;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Josh on 2019-06-24.
 */
@Permissions(version = 2, value = {
        @RegisteredPermission(
                node = "counter.modify",
                displayName = "Admin counters",
                description = "Allows you to create, remove, and set counters",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS}
        ),
        @RegisteredPermission(
                node = "counter.incall",
                displayName = "Add to a counter",
                description = "Allows you to add one to a counter",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS, DefaultGroup.SUBS}
        ),
        @RegisteredPermission(
                node = "counter.decall",
                displayName = "Subtract from a counter",
                description = "Allows you to subtract one from a counter",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS, DefaultGroup.SUBS}
        ),
        @RegisteredPermission(
                node = "counter.inc.{custompermission}",
                displayName = "Add to a specific counter",
                description = "Allows you to add to a specific factoid"
        ),
        @RegisteredPermission(
                node = "counter.dec.{custompermission}",
                displayName = "Subtract from a specific counter",
                description = "Allows you to subtract from a specific factoid"
        ),
        @RegisteredPermission(
                node = "counter.show",
                displayName = "Get a counter count",
                description = "Send the current counter count to chat",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.EVERYONE}
        )
})
@I18nDefaults(version = 1, value = {
        @I18nDefault(
                key = "help.blurb",
                message = "Counters will keep track of a single number. You can change them too!",
                displayName = "Counter blurb",
                description = "Sent when you ask for help about counters"
        ),
        @I18nDefault(
                key = "badtarget",
                message = "%commander%, I don't have a counter named %target%",
                displayName = "Bad target message",
                description = "Sent when you try to modify a counter that doesn't exist"
        ),
        @I18nDefault(
                key = "badargs",
                message = "%commander%, that command is missing something. %usage%",
                displayName = "Bad target message",
                description = "Sent when you try to modify a counter that doesn't exist"
        ),
        @I18nDefault(
                key = "created",
                message = "%commander%, now keeping track of %target%",
                displayName = "Create counter",
                description = "Sent when you create a new timer"
        ),
        @I18nDefault(
                key = "destroyed",
                message = "%commander%, %target% is no longer being tracked.",
                displayName = "Destroy counter",
                description = "Sent when you destroy a timer"
        ),
        @I18nDefault(
                key = "set",
                message = "%commander%, %target% is now at %count%",
                displayName = "Set counter",
                description = "Sent when you set a counter to an exact value"
        ),
        @I18nDefault(
                key = "msgset",
                message = "%commander%, gotchya! %target% changes will say that",
                displayName = "Set message",
                description = "Sent when you set an increment/decrement message"
        ),
        @I18nDefault(
                key = "list",
                message = "%commander%, I am keeping track of the following: %list%",
                displayName = "Counter list",
                description = "Sent when you request all counters in chat"
        ),
        @I18nDefault(
                key = "geektoggle",
                message = "%commander%, Geek Mode is now %status%",
                displayName = "Geek Mode toggle",
                description = "Sent when you turn off counter's geek mode"
        ),
})
public class CounterModule extends RegisteredThreadedModule<CounterGlobalConfiguration, CounterUserConfiguration> {

    public static void main(final String[] args) {
        new CounterModule().entryPoint(args);
    }

    @Override
    public String getName() {
        return "counter";
    }

    @Override
    protected void initialize() {
        super.initialize();
        registerListener(new CounterListener(this));
    }

    public boolean getGeekMode(final Channel channel) {
        return getConfiguration(channel).isGeekMode();
    }

    public void setGeekMode(final Channel channel, final boolean enabled) {
        final CounterUserConfiguration config = getConfiguration(channel);
        config.setGeekMode(enabled);
        getUserConfigProvider().save(channel.getTenant(), config);
    }

    public Counter getCounter(final Channel channel, final String id) {
        return getConfiguration(channel).getCounter(id);
    }

    public Map<String, Counter> getCounters(final Channel channel) {
        return getConfiguration(channel).getCounters();
    }

    public void addCounter(final Channel channel, final String id, final int initialCount, final String incMessage,
                           final String decMessage) {
        addCounter(channel, id, new Counter(initialCount, incMessage, decMessage));
    }

    public void addCounter(final Channel channel, final String id, final Counter counter) {
        final CounterUserConfiguration config = getConfiguration(channel);
        config.addCounter(id, counter);
        getUserConfigProvider().save(channel.getTenant(), config);
    }

    public boolean delCounter(final Channel channel, final String id) {
        final CounterUserConfiguration config = getConfiguration(channel);
        final boolean isDeleted = config.delCounter(id);
        if(isDeleted) getUserConfigProvider().save(channel.getTenant(), config);
        return isDeleted;
    }

    /* pkg-private */ void fixConfig(final Tenant tenant) {
        final CounterUserConfiguration config = fixConfig(getConfiguration(tenant));
    }

    /* pkg-private */ void fixConfig(final ChannelGroup channelGroup) {
        final CounterUserConfiguration config = fixConfig(getConfiguration(channelGroup));

    }

    /* pkg-private */ void fixConfig(final Channel channel) {
        final CounterUserConfiguration config = fixConfig(getConfiguration(channel));

    }

    private CounterUserConfiguration fixConfig(final CounterUserConfiguration config) {
        final ArrayList<String> dirty = new ArrayList<>();
        for(final Map.Entry<String, Counter> counter : config.getCounters().entrySet()) {
            if(counter.getKey().equals(counter.getKey().toLowerCase())) continue;
            dirty.add(counter.getKey());
        }
        for(final String counterName : dirty) {
            final Counter counter = config.getCounter(counterName);
            config.addCounter(counterName.toLowerCase(), counter);
            config.delCounter(counterName);
        }
        return config;
    }
}
