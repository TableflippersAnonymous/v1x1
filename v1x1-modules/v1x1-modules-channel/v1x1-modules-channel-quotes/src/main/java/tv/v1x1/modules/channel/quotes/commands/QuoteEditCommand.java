package tv.v1x1.modules.channel.quotes.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.text.Shorten;
import tv.v1x1.modules.channel.quotes.QuotesModule;
import tv.v1x1.modules.channel.quotes.quote.Quote;

import java.util.List;

/**
 * @author Josh
 */
public class QuoteEditCommand extends Command {
    private final QuotesModule module;
    public QuoteEditCommand(final QuotesModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("edit", "modify");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final String commander = chatMessage.getSender().getMention();
        final Channel channel = chatMessage.getChannel();
        final String quoteIdText = args.remove(0);
        final String quoteText = String.join(" ", args);
        final int quoteId;
        try {
            quoteId = Integer.parseInt(quoteIdText);
            if(quoteId < 1) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            Chat.i18nMessage(module, channel, "invalid.id",
                    "commander", commander,
                    "command", command,
                    "usage", getUsage());
            return;
        }
        final Quote quote = module.getQuoteById(channel.getChannelGroup().getTenant(), quoteId);
        if(quote == null) {
            Chat.i18nMessage(module, channel, "invalid.quote",
                    "commander", commander,
                    "id", quoteId);
            return;
        }
        final Quote newQuote = module.editQuote(channel.getChannelGroup().getTenant(), quoteId, quoteText);
        if(newQuote == null) {
            Chat.i18nMessage(module, channel, "generic.error",
                    "commander", commander,
                    "message", "editQuote() returned null");
        } else {
            Chat.i18nMessage(module, channel, "modified",
                    "commander", commander,
                    "id", quote.getId(),
                    "quote", Shorten.genPreview(newQuote.getQuote()));
        }
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("quote.edit"));
    }

    @Override
    public String getUsage() {
        return "<quote>";
    }

    @Override
    public String getDescription() {
        return "adds a quote to the database";
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
