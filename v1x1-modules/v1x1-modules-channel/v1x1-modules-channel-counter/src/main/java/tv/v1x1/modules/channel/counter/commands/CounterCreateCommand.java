package tv.v1x1.modules.channel.counter.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.counter.CounterModule;

import java.util.List;

/**
 * Created by Josh on 2019-06-25
 */
public class CounterCreateCommand extends Command {
    final private CounterModule module;

    public CounterCreateCommand(final CounterModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("create");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("counter.modify"));
    }

    @Override
    public String getUsage() {
        return "<name>";
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Create a new counter to track a number";
    }

    @Override
    public String getHelp() {
        return "If Geek Mode is on, you can add/subtract one with just \"counter++\" and \"counter--\"";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String id = args.get(0).toLowerCase();
        module.addCounter(channel, id, 0, "", "");
        Chat.i18nMessage(module, channel, "created",
                "commander", chatMessage.getSender().getMention(),
                "target", id);
    }
}
