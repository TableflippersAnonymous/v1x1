package tv.v1x1.modules.channel.ops_tool.commands;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.commands.ParsedCommand;
import tv.v1x1.modules.channel.ops_tool.OpsTool;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.GetLinkCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.GroupCreateCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.GroupDestroyCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.GroupInfoCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.GroupListCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.LinkCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.PermAddCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.PermHelpCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.PermListCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.PermRemoveCommand;
import tv.v1x1.modules.channel.ops_tool.commands.permissions.UnlinkCommand;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * @author Josh
 */
public class OpsToolPermissionCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    final private OpsTool opsTool;
    final private CommandDelegator delegator;

    public OpsToolPermissionCommand(final OpsTool opsTool) {
        // x add perm
        // x remove perm
        // x create group
        // x list groups
        // x info on group
        // x destroy group
        // x link group to platform group
        // x list links
        this.opsTool = opsTool;
        delegator = new CommandDelegator();
        delegator.registerCommand(new GroupCreateCommand(opsTool));
        delegator.registerCommand(new GroupInfoCommand(opsTool));
        delegator.registerCommand(new GroupListCommand(opsTool));
        delegator.registerCommand(new GroupDestroyCommand(opsTool));
        delegator.registerCommand(new PermAddCommand(opsTool));
        delegator.registerCommand(new PermListCommand(opsTool));
        delegator.registerCommand(new PermRemoveCommand(opsTool));
        delegator.registerCommand(new LinkCommand(opsTool));
        delegator.registerCommand(new UnlinkCommand(opsTool));
        delegator.registerCommand(new GetLinkCommand(opsTool));
        delegator.registerCommand(new PermHelpCommand(opsTool, delegator));
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("perm", "p", "perms");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        opsTool.respond(chatMessage.getChannel(), "Invalid subcommand/arguments");
    }

    @Override
    public String getDescription() {
        return "modify groups and permissions";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        String subCmd = args.remove(0).toLowerCase();
        try {
            if(!delegator.handleParsedCommand(chatMessage, new ParsedCommand(subCmd, args)))
                handleArgMismatch(chatMessage, command, args);
        } catch(Exception ex) {
            Chat.i18nMessage(opsTool, chatMessage.getChannel(), "generic.error",
                    "commander", chatMessage.getSender().getMention(),
                    "message", ex.getClass().getSimpleName());
            LOG.info("OpsTool broke...", ex);
        }
    }
}
