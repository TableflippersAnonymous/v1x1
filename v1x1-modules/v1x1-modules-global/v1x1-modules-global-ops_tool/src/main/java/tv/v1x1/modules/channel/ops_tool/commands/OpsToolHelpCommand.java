package tv.v1x1.modules.channel.ops_tool.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.commands.GenericHelpMessage;
import tv.v1x1.modules.channel.ops_tool.OpsTool;

import java.util.List;

/**
 * @author Josh
 */
public class OpsToolHelpCommand extends Command {
    final private OpsTool opsTool;
    final private CommandDelegator masterDelegator;

    public OpsToolHelpCommand(final OpsTool opsTool, final CommandDelegator delegator) {
        this.opsTool = opsTool;
        this.masterDelegator = delegator;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("help");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public String getDescription() {
        return "this";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        opsTool.respond(chatMessage.getChannel(), GenericHelpMessage.helpMessage(masterDelegator, (args.size() > 0 ? args.get(0) : ""), "!ot "));
    }
}
