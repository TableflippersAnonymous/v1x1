package tv.v1x1.modules.channel.quotes.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.commands.ParsedCommand;
import tv.v1x1.common.util.validation.PrimitiveValidator;
import tv.v1x1.modules.channel.quotes.QuotesModule;

import java.util.List;

/**
 * @author Josh
 */
public class QuoteCommand extends Command {
    private final QuotesModule module;
    private final CommandDelegator delegator;
    public QuoteCommand(final QuotesModule quoteModule) {
        this.module = quoteModule;
        delegator = new CommandDelegator();
        delegator.registerCommand(new QuoteAddCommand(quoteModule));
        delegator.registerCommand(new QuoteEditCommand(quoteModule));
        delegator.registerCommand(new QuoteGetCommand(quoteModule));
        delegator.registerCommand(new QuoteDelCommand(quoteModule));
        delegator.registerCommand(new QuoteHelpCommand(quoteModule, delegator));
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("quote");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("quote.use"));
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, List<String> args) {
        final String subCmd;
        if(args.size() < 1) {
            subCmd = "get";
        } else if(PrimitiveValidator.isInteger(args.get(0))) {
            subCmd = "get";
        } else {
            subCmd = args.remove(0).toLowerCase();
        }
        if(!delegator.handleParsedCommand(chatMessage, new ParsedCommand(subCmd, args)))
            handleArgMismatch(chatMessage, command, args);
    }

    @Override
    public String getUsage() {
        return "[id|subcommand <args>]";
    }

    @Override
    public String getDescription() {
        return "view or modify quotes";
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "invalid.subcommand",
                "commander", chatMessage.getSender().getMention());
    }
}
