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
public class CounterGeekModeCommand extends Command {
    final private CounterModule module;

    public CounterGeekModeCommand(final CounterModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("geek", "geekmode");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("counter.modify"));
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public int getMaxArgs() {
        return 0;
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public String getDescription() {
        return "Toggle geek mode.";
    }

    @Override
    public String getHelp() {
        return "When enabled, you can add/subtract one with just \"counter++\" or \"counter--\" in chat without any !counter commands";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getMention();
        module.setGeekMode(channel, !module.getGeekMode(channel));
        Chat.i18nMessage(module, channel, "geektoggle",
                "commander", commander,
                "status", (module.getGeekMode(channel) ? "on" : "off"));
    }
}
