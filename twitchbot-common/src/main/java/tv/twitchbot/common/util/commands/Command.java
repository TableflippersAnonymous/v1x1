package tv.twitchbot.common.util.commands;

import tv.twitchbot.common.dto.core.ChatMessage;
import tv.twitchbot.common.dto.core.Permission;

import java.util.List;

/**
 * Created by Josh on 2016-10-06.
 */
public abstract class Command {
    /**
     * List of aliases this command will respond to
     * @return
     */
    public abstract List<String> getCommands();

    /**
     * List of permissions required for this command to be used
     * @return
     */
    public abstract List<Permission> getAllowedPermissions();

    /**
     * Callback to run command should it match all criteria
     * @param chatMessage raw chat message object that triggered this command
     * @param command String alias that triggered this command
     * @param args List<String> args for this command invocation
     */
    public abstract void run(ChatMessage chatMessage, String command, List<String> args);

    /**
     * Syntax of the command, for help pages
     * @return
     */
    public String getUsage() { return getCommands().get(0) + " [args]"; }

    /**
     * Description of the command, for help pages
     * @return
     */
    public String getDescription() { return "-"; }

    /**
     * Required minimum args for the command to be fired. 0 means it expects no args
     * @return
     */
    public int getMinArgs() { return 0; }

    /**
     * Maximum amount of args before the command isn't fired
     * Special value of -1 means it accepts infinite args
     * @return
     */
    public int getMaxArgs() { return -1; }
}
