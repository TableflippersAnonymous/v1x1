package tv.v1x1.modules.channel.quotes.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.quotes.QuotesModule;
import tv.v1x1.modules.channel.quotes.quote.Quote;

import java.util.List;

/**
 * @author Josh
 */
public class QuoteGetCommand extends Command {
    private final QuotesModule module;
    public QuoteGetCommand(final QuotesModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("get");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getDisplayName();
        final Quote quote;
        if(args.size() > 0) {
            try {
                final int quoteId = Integer.parseInt(args.get(0));
                quote = module.getQuoteById(channel.getChannelGroup().getTenant(), quoteId);
                if(quote == null) {
                    Chat.i18nMessage(module, channel, "invalid.quote",
                            "commander", commander,
                            "id", args.get(0));
                    return;
                }
            } catch(NumberFormatException ex) {
                Chat.i18nMessage(module, channel, "invalid.id",
                        "commander", commander);
                return;
            }
        } else {
            quote = module.getRandomQuote(channel.getChannelGroup().getTenant());
            if(quote == null) {
                Chat.i18nMessage(module, channel, "noquotes",
                        "commander", commander
                );
                return;
            }
        }

        if(quote.getDate() != null && quote.getGame() != null) {
            Chat.i18nMessage(module, channel, "respond.standard",
                    "id", quote.getId(),
                    "quote", quote.getQuote(),
                    "game", quote.getGame(),
                    "date", quote.getDate());
        } else if(quote.getDate() == null) {
            Chat.i18nMessage(module, channel, "respond.gameonly",
                    "id", quote.getId(),
                    "quote", quote.getQuote(),
                    "game", quote.getGame());
        } else if (quote.getGame() == null) {
            Chat.i18nMessage(module, channel, "respond.dateonly",
                    "id", quote.getId(),
                    "quote", quote.getQuote(),
                    "date", quote.getDate());
        } else {
            Chat.i18nMessage(module, channel, "respond.nometadata",
                    "id", quote.getId(),
                    "quote", quote.getQuote());
        }
    }

    @Override
    public String getUsage() {
        return "[id]";
    }

    @Override
    public String getDescription() {
        return "get an old quote";
    }

    @Override
    public String getHelp() {
        return "If you don't provide a certain quote ID, a random quote will be given. !quote <id> and !quote with no args are aliases for this command";
    }
}
