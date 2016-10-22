package tv.v1x1.modules.channel.link_purger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.i18n.Language;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.rpc.client.ChatRouterServiceClient;
import tv.v1x1.common.util.commands.CommandDelegator;

import java.lang.invoke.MethodHandles;

/**
 * @author Josh
 */
public class LinkPurger extends RegisteredThreadedModule<LinkPurgerSettings, LinkPurgerGlobalConfiguration, LinkPurgerTenantConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static {
        final Module module = new Module("link_purger");
        I18n.registerDefault(module, "purged", "Hey %user%, please ask before posting a link! I've purged all your messages; feel free to keep participating in the chat!");
        I18n.registerDefault(module, "timeout", "Hey %user%, I said ask before posting a link! I've timed you out for now; see you soon.");
        I18n.registerDefault(module, "permit", "Hey %viewer%, you can post one link now!");
    }

    private CommandDelegator delegator;
    /* pkg-private */ Language language;

    public static void main(String[] args) throws Exception {
        new LinkPurger().entryPoint(args);
    }

    @Override
    protected void initialize() {
        super.initialize();
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new PermitCommand(this));
        registerListener(new LinkPurgerListener(this));
        language = getI18n().getLanguage(null);
    }

    @Override
    public String getName() {
        return "link_purger";
    }
}
