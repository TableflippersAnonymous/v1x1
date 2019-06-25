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
public class CounterDestroyCommand extends Command {
    final private CounterModule module;

    public CounterDestroyCommand(final CounterModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("destroy", "remove");
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
        return "Remove an existing counter";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String id = args.get(0).toLowerCase();
        final boolean deleted = module.delCounter(channel, id);
        final String commander = chatMessage.getSender().getMention();
        if(deleted)
            Chat.i18nMessage(module, channel, "destroyed",
                "commander", commander,
                "target", id);
        else
            Chat.i18nMessage(module, channel, "badtarget",
                    "commander", commander,
                    "target", id);
    }
}
