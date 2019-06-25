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
public class CounterShowCommand extends Command {
    final private CounterModule module;

    public CounterShowCommand(final CounterModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("show", "get");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("counter.show"));
    }

    @Override
    public void handleNoPermissions(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "generic.noperms",
                "commander", chatMessage.getSender().getMention(),
                "perm", "counter.show");
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
        return "Says the current count of a counter in chat";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String id = args.get(0).toLowerCase();
        final Counter counter = module.getCounter(channel, id);
        final String commander = chatMessage.getSender().getMention();
        if(counter == null) {
            Chat.i18nMessage(module, channel, "badtarget",
                    "commander", commander,
                    "target", id);
            return;
        }
        Chat.i18nMessage(module, channel, "set",
                "commander", commander,
                "target", id,
                "count", counter.getCount());
    }
}
