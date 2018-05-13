package tv.v1x1.modules.channel.quotes.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.quotes.QuotesModule;

import java.util.List;

/**
 * @author Josh
 */
public class QuoteDelCommand extends Command {
    private final QuotesModule module;
    public QuoteDelCommand(final QuotesModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("del", "delete", "remove");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final String commander = chatMessage.getSender().getMention();
        final Channel channel = chatMessage.getChannel();
        final int quoteId;
        try {
            quoteId = Integer.parseInt(args.get(0));
            if(quoteId < 1) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            Chat.i18nMessage(module, channel, "invalid.id",
                    "commander", commander,
                    "command", command,
                    "usage", getUsage());
            return;
        }
        if(module.delQuote(channel.getChannelGroup().getTenant(), quoteId)) {
           Chat.i18nMessage(module, channel, "removed",
                   "commander", commander,
                   "id", quoteId);
        } else {
            Chat.i18nMessage(module, channel, "invalid.quote",
                    "commander", commander,
                    "id", quoteId);
        }
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("quote.edit"));
    }

    @Override
    public String getUsage() {
        return "<id>";
    }

    @Override
    public String getDescription() {
        return "deletes a quote from the database";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "generic.invalid.args",
                "commander", chatMessage.getSender().getMention(),
                "command", toString(),
                "usage", getUsage());
    }
}
