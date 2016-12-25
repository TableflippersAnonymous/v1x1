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

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * @author Josh
 */
public class OpsToolCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected OpsTool opsTool;
    protected CommandDelegator delegator;

    public OpsToolCommand() {

    }

    public OpsToolCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
        this.delegator = new CommandDelegator("");
        delegator.registerCommand(new OpsToolTenantIdCommand(opsTool));
        delegator.registerCommand(new OpsToolUserIdCommand(opsTool));
        delegator.registerCommand(new OpsToolPermissionCommand(opsTool));
        delegator.registerCommand(new OpsToolHelpCommand(opsTool, delegator));
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("opstool", "ot");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("opstool.use"));
    }

    @Override
    public String getDescription() {
        return "tooling to help manage the bot globally";
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(opsTool, chatMessage.getChannel(), "invalid.subcommand",
                "commander", chatMessage.getSender().getDisplayName(),
                "usage", getUsage()
        );
    }

    @Override
    public void handleNoPermissions(final ChatMessage chatMessage, final String command, final List<String> args) {
        if(opsTool.getTenantConfiguration(chatMessage.getChannel().getTenant()).isEnabled())
            Chat.i18nMessage(opsTool, chatMessage.getChannel(), "generic.noperms",
                    "commander", chatMessage.getSender().getDisplayName());
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
                    "commander", chatMessage.getSender().getDisplayName(),
                    "message", ex.getClass().getSimpleName());
            LOG.info("OpsTool broke...", ex);
        }
    }
}
