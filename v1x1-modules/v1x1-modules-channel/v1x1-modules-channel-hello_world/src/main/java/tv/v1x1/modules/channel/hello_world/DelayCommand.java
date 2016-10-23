package tv.v1x1.modules.channel.hello_world;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.data.CompositeKey;

import java.util.List;
import java.util.UUID;

/**
 * Created by naomi on 10/23/2016.
 */
public class DelayCommand extends Command {
    private final HelloWorld module;

    public DelayCommand(final HelloWorld module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("delay");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("hello.delay"));
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final long delay = Long.valueOf(args.get(0));
        final String message = Joiner.on(" ").join(args.subList(1, args.size()));
        module.ssc.scheduleWithDelay(delay, new tv.v1x1.common.dto.core.UUID(UUID.randomUUID()), CompositeKey.makeKey(new byte[][] {chatMessage.getChannel().toProto().toByteArray(), message.getBytes()}));
    }
}
