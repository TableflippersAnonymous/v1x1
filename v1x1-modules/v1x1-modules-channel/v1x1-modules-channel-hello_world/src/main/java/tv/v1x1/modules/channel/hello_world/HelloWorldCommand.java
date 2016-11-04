package tv.v1x1.modules.channel.hello_world;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableMap;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Created by Josh on 2016-10-07.
 */
public class HelloWorldCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HelloWorld module;

    public HelloWorldCommand(final HelloWorld module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("hello", "hey", "hi");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("hello.use"));
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "hello.badargs",
                "commander", chatMessage.getSender().getDisplayName(),
                "usage", getUsage());
    }

    @Override
    public void handleNoPermissions(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "hello.noperms",
                "commander", chatMessage.getSender().getDisplayName(),
                "perm", "hello.use");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final String resp = module.language.message(module.toDto(), "hello", ImmutableMap.of("user", chatMessage.getSender().getDisplayName()));
        module.crsc.sendMessage(chatMessage.getChannel(), resp);
    }
}
