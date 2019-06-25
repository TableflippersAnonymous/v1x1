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
public class CounterIncMsgCommand extends Command {
    final private CounterModule module;

    public CounterIncMsgCommand(final CounterModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("incmsg");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("counter.modify"));
    }

    @Override
    public String getUsage() {
        return "<name> <msg>";
    }

    @Override
    public int getMaxArgs() {
        return -1;
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public String getDescription() {
        return "Set the message when someone increments the counter";
    }

    @Override
    public String getHelp() {
        return "If geek mode is enabled, you can add one with just \"counter++\"";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String id = args.remove(0);
        final Counter counter = module.getCounter(channel, id);
        final String commander = chatMessage.getSender().getMention();
        if(counter == null) {
            Chat.i18nMessage(module, channel, "badtarget",
                    "commander", commander,
                    "target", id);
            return;
        }
        counter.setIncMessage(String.join(" ", args));
        module.addCounter(channel, id, counter);
        Chat.i18nMessage(module, channel, "msgset",
                "commander", commander,
                "target", id);
    }
}
