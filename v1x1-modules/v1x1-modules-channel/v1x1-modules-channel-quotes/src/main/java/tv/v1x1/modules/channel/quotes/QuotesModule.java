package tv.v1x1.modules.channel.quotes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.modules.channel.quotes.commands.QuoteCommand;
import tv.v1x1.modules.channel.quotes.config.QuotesChannelConfiguration;
import tv.v1x1.modules.channel.quotes.config.QuotesGlobalConfiguration;
import tv.v1x1.modules.channel.quotes.config.QuotesSettings;
import tv.v1x1.modules.channel.quotes.config.QuotesTenantConfiguration;
import tv.v1x1.modules.channel.quotes.quote.DAOQuote;
import tv.v1x1.modules.channel.quotes.quote.Quote;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Josh
 */
public class QuotesModule extends RegisteredThreadedModule<QuotesSettings, QuotesGlobalConfiguration, QuotesTenantConfiguration, QuotesChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static {
        Module module = new Module("quotes");
        I18n.registerDefault(module, "invalid.quote", "%commander%, quote #%id% doesn't exist!");
        I18n.registerDefault(module, "invalid.id", "%commander%, that doesn't look like a valid quote number.");
        I18n.registerDefault(module, "invalid.subcommand", "%commander%, I don't know that command. Need some !quote help?");
        I18n.registerDefault(module, "added", "%commander%, quote #%id% added! \"%quote%\"");
        I18n.registerDefault(module, "removed", "%commander%, quote #%id% removed.");
        I18n.registerDefault(module, "noquotes", "%commander%, there don't seem to be any quotes.");
        I18n.registerDefault(module, "respond.standard", "Quote #%id%: %quote% [%game%] [%date%]");
        I18n.registerDefault(module, "respond.dateonly", "Quote: #%id%: %quote% [%date%]");
        I18n.registerDefault(module, "respond.gameonly", "Quote: #%id%: %quote% [%game%]");
        I18n.registerDefault(module, "modified", "%commander%, quote #%id% was modified to say: %quote%");
        I18n.registerDefault(module, "help.blurb", "The quote command can be used alone to get a random qu" +
                "ote, with a number to get a specific one, or with the add/del/edit commands to modify them.");
    }

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
        if(getChannelConfiguration(channel).isOverridden()) {
            return getChannelConfiguration(channel).isEnabled();
        } else {
            return getTenantConfiguration(channel.getTenant()).isEnabled();
        }
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
