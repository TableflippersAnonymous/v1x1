package tv.v1x1.common.util.commands;

import java.util.List;

/**
 * @author Josh
 */
// TODO: I18n this
public class GenericHelpMessage {
    public static String helpMessage(final CommandDelegator delegator, final String commandStr, final String prefixOverride) {
        final StringBuilder sb = new StringBuilder();
        final String prefix = (prefixOverride != null ? prefixOverride : delegator.getPrefix());
        if(commandStr.isEmpty()) {
            if(!prefix.isEmpty()) {
                sb.append("All commands start with ");
                sb.append(prefix);
                sb.append(" | ");
            }
            boolean first = true;
            for(Command command : delegator.getRegisteredCommands()) {
                if(first) first = false;
                else sb.append(" | ");
                sb.append(String.join("/", command.getCommands()));
                sb.append(" - ");
                sb.append(command.getDescription());
            }
        } else {
            Command command = null;
            for(Command checkedCommand : delegator.getRegisteredCommands()) {
                if(checkedCommand.getCommands().contains(commandStr)) {
                    command = checkedCommand;
                    break;
                }
            }
            if(command == null) {
                sb.append("I'm sorry, but I don't know about \"");
                sb.append(commandStr);
                sb.append('"');
            } else {
                final List<String> aliases = command.getCommands();
                sb.append(aliases.get(0));
                sb.append(" | ");
                if(aliases.size() > 1) {
                    sb.append("AKA: ");
                    sb.append(String.join("/", aliases.subList(1, aliases.size())));
                    sb.append(" | ");
                }
                sb.append("Usage: ");
                sb.append(prefix);
                sb.append(command.getCommands().get(0));
                sb.append(' ');
                sb.append(command.getUsage());
                sb.append(" | Desc: ");
                sb.append(command.getDescription());
                if(command.getHelp() != null) {
                    sb.append(" | ");
                    sb.append("Blurb: ");
                    sb.append(command.getHelp());
                }
            }
        }
        return sb.toString();
    }
}
