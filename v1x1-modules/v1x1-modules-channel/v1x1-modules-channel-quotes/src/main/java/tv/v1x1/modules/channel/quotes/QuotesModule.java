package tv.v1x1.modules.channel.quotes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.scanners.i18n.I18nDefault;
import tv.v1x1.common.scanners.i18n.I18nDefaults;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.modules.channel.quotes.commands.QuoteCommand;
import tv.v1x1.modules.channel.quotes.config.QuotesGlobalConfiguration;
import tv.v1x1.modules.channel.quotes.config.QuotesUserConfiguration;
import tv.v1x1.modules.channel.quotes.quote.DAOQuote;
import tv.v1x1.modules.channel.quotes.quote.Quote;

import javax.ws.rs.WebApplicationException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Random;

/**
 * @author Josh
 */
@Permissions(version = 1, value = {
        @RegisteredPermission(
                node = "quote.use",
                displayName = "Use Quotes",
                description = "This allows you to use and display quotes",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS, DefaultGroup.EVERYONE}
        ),
        @RegisteredPermission(
                node = "quote.edit",
                displayName = "Edit Quotes",
                description = "This allows you to add, edit, and remove quotes",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS}
        )
})
@I18nDefaults(version = 1, value = {
        @I18nDefault(
                key = "invalid.quote",
                message = "%commander%, quote #%id% doesn't exist!",
                displayName = "Invalid Quote",
                description = "Sent when a quote does not exist"
        ),
        @I18nDefault(
                key = "invalid.id",
                message = "%commander%, that doesn't look like a valid quote number.",
                displayName = "Invalid ID",
                description = "Sent when an ID is not a number"
        ),
        @I18nDefault(
                key = "invalid.subcommand",
                message = "%commander%, I don't know that command. Need some !quote help?",
                displayName = "Invalid Command",
                description = "Sent when an invalid command is used with !quote"
        ),
        @I18nDefault(
                key = "added",
                message = "%commander%, quote #%id% added! \"%quote%\"",
                displayName = "Added",
                description = "Sent when a quote is successfully added"
        ),
        @I18nDefault(
                key = "removed",
                message = "%commander%, quote #%id% removed.",
                displayName = "Removed",
                description = "Sent when a quote is successfully removed"
        ),
        @I18nDefault(
                key = "noquotes",
                message = "%commander%, there don't seem to be any quotes.",
                displayName = "No Quotes",
                description = "Sent when there are no quotes"
        ),
        @I18nDefault(
                key = "respond.standard",
                message = "Quote #%id%: %quote% [%game%] [%date%]",
                displayName = "Response (Complete)",
                description = "Format for a quote with both game and date data"
        ),
        @I18nDefault(
                key = "respond.dateonly",
                message = "Quote: #%id%: %quote% [%date%]",
                displayName = "Response (Date)",
                description = "Format for a quote with only date data"
        ),
        @I18nDefault(
                key = "respond.gameonly",
                message = "Quote: #%id%: %quote% [%game%]",
                displayName = "Response (Game)",
                description = "Format for a quote with only game data"
        ),
        @I18nDefault(
                key = "modified",
                message = "%commander%, quote #%id% was modified to say: %quote%",
                displayName = "Modified",
                description = "Sent when a quote is modified"
        ),
        @I18nDefault(
                key = "help.blurb",
                message = "The quote command can be used alone to get a random quote, with a number to get a specific" +
                        " one, or with the add/del/edit commands to modify them.",
                displayName = "Help Blurb",
                description = "Sent with !quote help"
        )
})
public class QuotesModule extends RegisteredThreadedModule<QuotesGlobalConfiguration, QuotesUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    CommandDelegator delegator;
    private DAOQuote daoQuote;
    private Random random;

    public static void main(final String[] args) throws Exception {
        new QuotesModule().entryPoint(args);
    }

    @Override
    public String getName() {
        return "quotes";
    }

    @Override
    protected void initialize() {
        super.initialize();
        random = new Random();
        daoQuote = new DAOQuote(getMappingManager());
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new QuoteCommand(this));
        registerListener(new QuotesListener(this));
    }

    public Quote getQuoteById(final Tenant tenant, final int id) {
        return daoQuote.getByid(tenant.getId().getValue(), id);
    }

    public List<Quote> getAllQuotes(final Tenant tenant) {
        return daoQuote.all(tenant.getId().getValue());
    }

    public Quote addQuote(final Tenant tenant, final String quote, final String game) {
        final Quote lastQuote = daoQuote.getLastQuote(tenant.getId().getValue());
        int id = 1;
        if(lastQuote != null) {
            if(lastQuote.isDeleted())
                id = lastQuote.getId();
            else
                id = lastQuote.getId()+1;
        }
        return addQuote(new Quote(tenant.getId(), quote, game, id));
    }

    public Quote addQuote(final Quote quote) {
        daoQuote.save(quote);
        return quote;
    }

    public boolean delQuote(final Tenant tenant, final int id) {
        final Quote quote = getQuoteById(tenant, id);
        if(quote == null) return false;
        if(quote.isDeleted()) return false;
        quote.delete();
        daoQuote.save(quote);
        return true;
    }

    public Quote editQuote(final Tenant tenant, final int id, final String newText) {
        final Quote quote = daoQuote.getByid(tenant.getId().getValue(), id);
        if(quote == null) return null;
        quote.setQuote(newText);
        daoQuote.save(quote);
        return quote;
    }

    public Quote getRandomQuote(final Tenant tenant) {
        final Quote lastQuote = daoQuote.getLastQuote(tenant.getId().getValue());
        if(lastQuote == null) return null;
        final int lastId = lastQuote.getId();
        final int randomId = random.nextInt(lastId);
        for(int retId = randomId + 1; randomId != retId; ) {
            final Quote retQuote = daoQuote.getByid(tenant.getId().getValue(), retId);
            if(retQuote == null || retQuote.isDeleted()) {
                if(++retId > lastId)
                    retId = 1;
            } else {
                return retQuote;
            }
        }
        return null;
    }

    public boolean isEnabled(final Channel channel) {
        return getConfiguration(channel).isEnabled();
    }

    public String getGame(final Channel channel) {
        final String targetId = channel.getId().toLowerCase();
        final tv.v1x1.common.services.twitch.dto.channels.Channel videoChannel;
        try {
            videoChannel = getTwitchApi().getChannels().getChannel(targetId);
        } catch(WebApplicationException ex) {
            LOG.warn("Couldn't find the game for channel we're in", ex);
            return null;
        }
        return videoChannel.getGame();
    }

}
