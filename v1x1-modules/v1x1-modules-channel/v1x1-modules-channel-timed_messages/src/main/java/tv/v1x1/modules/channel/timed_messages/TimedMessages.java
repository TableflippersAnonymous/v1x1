package tv.v1x1.modules.channel.timed_messages;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.i18n.Language;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.rpc.client.SchedulerServiceClient;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.modules.channel.timed_messages.commands.TimerCommand;

/**
 * @author Josh
 */
public class TimedMessages extends RegisteredThreadedModule<TimedMessagesSettings, TimedMessagesGlobalConfiguration, TimedMessagesTenantConfiguration> {
    static {
        Module module = new Module("timed_messages");
        /*
        !timer create main 10min
        !timer add main Hello, world!
        !timer delete Hello
        !timer disable main
        !timer enable main
        !timer destroy main
         */
        // base/shared
        I18n.registerDefault(module, "invalid.subcommand", "%commander%, I don't know that subcommand. Usage: %usage%");
        I18n.registerDefault(module, "invalid.timer", "%commander%, I can't find a timer called \"%id%.\"");

        // subcmd success
        I18n.registerDefault(module, "timer.created", "%commander%, I've created a timer named \"%id%\". Now add stuff to it!");
        I18n.registerDefault(module, "entry.added", "%commander%, I've added your entry to the rotation. Here's a preview: %preview%");
        I18n.registerDefault(module, "entry.removed", "%commander%, I've removed the following entry from the rotation: %preview%");
        I18n.registerDefault(module, "timer.destroyed", "%commander%, I've destroyed the timer named \"%id%\". It will no longer message the chat.");
        I18n.registerDefault(module, "timer.enabled", "%commander%, I've re-enabled the timer \"%id%\" rotation.");
        I18n.registerDefault(module, "timer.disabled", "%commander%, I've disabled the timer \"%id%\" rotation.");

        // subcmd failure
        I18n.registerDefault(module, "timer.create.alreadyexists", "%commander%, there's already a timer named \"%id%\". Do you wanna to add entries with %cmd%?");
        I18n.registerDefault(module, "timer.create.notarget", "%commander%, what's the name of the timer we're creating? Usage: %usage%");
        I18n.registerDefault(module, "entry.nomatch", "%commander%, I can't find a timer entry that starts with %preview%");
        I18n.registerDefault(module, "entry.add.invalidmessage", "%commander%, I'd love to add a timer, but what should it say? Usage: %usage%");
        I18n.registerDefault(module, "entry.add.notarget", "%commander%, add to what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "entry.remove.notarget", "%commander%, remove from what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "timer.destroy.notarget", "%commander%, destroy what timer? Also, what message? Usage: %usage%");
        I18n.registerDefault(module, "timer.alreadytoggled", "%commander%, the \"%id%\" timer is already %state%");
    }

    private CommandDelegator delegator;
    public Language language;
    private SchedulerServiceClient ssc;

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
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new TimerCommand(this));
        ssc = getServiceClient(SchedulerServiceClient.class);
        language = getI18n().getLanguage(null);
    }
}
