package tv.twitchbot.modules.channel.hello_world;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.twitchbot.common.dto.core.ChatMessage;
import tv.twitchbot.common.dto.core.Permission;
import tv.twitchbot.common.util.commands.Command;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Created by Josh on 2016-10-07.
 */
public class HelloWorldCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private HelloWorld module;

    public HelloWorldCommand(HelloWorld module) {
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
    public void run(ChatMessage chatMessage, String command, List<String> args) {
        LOG.debug("Got hello command");
        module.crsc.sendMessage(chatMessage.getChannel(), "Hello World");
    }
}
