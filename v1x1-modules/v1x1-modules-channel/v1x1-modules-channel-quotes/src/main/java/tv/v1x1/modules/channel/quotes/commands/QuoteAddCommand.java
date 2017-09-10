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
public class QuoteAddCommand extends Command {
    private final QuotesModule module;
    public QuoteAddCommand(final QuotesModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("add");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final String commander = chatMessage.getSender().getDisplayName();
        final Channel channel = chatMessage.getChannel();
        final String game = module.getGame(channel);
        final String quoteText = String.join(" ", args);
        final Quote quote = module.addQuote(channel.getChannelGroup().getTenant(), quoteText, game);
        if(quote != null) {
            Chat.i18nMessage(module, channel, "added",
                    "commander", commander,
                    "id", quote.getId(),
                    "quote", Shorten.genPreview(quoteText));
        } else {
            Chat.i18nMessage(module, channel, "generic.error",
                    "commander", commander,
                    "message", "addQuote() didn't add the quote");
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
                "commander", chatMessage.getSender().getDisplayName(),
                "command", toString(),
                "usage", getUsage());
    }
}
