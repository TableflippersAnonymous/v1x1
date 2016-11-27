package tv.v1x1.modules.channel.quotes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.modules.channel.quotes.config.QuotesChannelConfiguration;
import tv.v1x1.modules.channel.quotes.config.QuotesGlobalConfiguration;
import tv.v1x1.modules.channel.quotes.config.QuotesSettings;
import tv.v1x1.modules.channel.quotes.config.QuotesTenantConfiguration;

import java.lang.invoke.MethodHandles;

/**
 * @author Josh
 */
public class Quotes extends RegisteredThreadedModule<QuotesSettings, QuotesGlobalConfiguration, QuotesTenantConfiguration, QuotesChannelConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static {
        Module module = new Module("quotes");
        I18n.registerDefault(module, "added", "%commander%, added quote! \"%preview%\" [%date%]");
        // removed
        // edited
        // quote
    }

    @Override
    public String getName() {
        return "quotes";
    }

    @Override
    protected void initialize() {
        super.initialize();
    }
}
