package tv.v1x1.modules.channel.counter.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.counter.CounterModule;
import tv.v1x1.modules.channel.counter.config.Counter;

import java.util.List;

/**
 * Created by Josh on 2019-06-25
 */
public class CounterSetCommand extends Command {
    final private CounterModule module;

    public CounterSetCommand(final CounterModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("set");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("counter.modify"));
    }

    @Override
    public String getUsage() {
        return "<name> <count>";
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public String getDescription() {
        return "Set a counter to a specific number";
    }

    @Override
    public String getHelp() {
        return "If geek mode is enabled, you can add/subtract one with just \"counter++\" or \"counter--\"";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String id = args.get(0).toLowerCase();
        final Counter counter = module.getCounter(channel, id);
        final String commander = chatMessage.getSender().getMention();
        if(counter == null) {
            Chat.i18nMessage(module, channel, "counter.badtarget",
                    "commander", commander,
                    "target", id);
            return;
        }
        try {
            counter.setCount(Integer.parseInt(args.get(1)));
            module.addCounter(channel, id, counter);
        } catch(NumberFormatException ex) {
            Chat.i18nMessage(module, channel, "badargs",
                    "commander", commander,
                    "usage", getUsage());
            return;
        }
        Chat.i18nMessage(module, channel, "set",
                "commander", commander,
                "target", id,
                "count", counter.getCount());
    }
}
