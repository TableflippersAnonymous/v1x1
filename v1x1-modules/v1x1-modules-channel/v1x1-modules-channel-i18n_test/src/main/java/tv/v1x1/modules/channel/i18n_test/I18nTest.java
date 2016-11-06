package tv.v1x1.modules.channel.i18n_test;

import tv.v1x1.common.i18n.Language;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.rpc.client.ChatRouterServiceClient;
import tv.v1x1.common.util.commands.CommandDelegator;

/**
 * @author Josh
 */
public class I18nTest extends RegisteredThreadedModule<I18nTestSettings, I18nTestGlobalConfiguration, I18nTestTenantConfiguration, I18nTestChannelConfiguration> {
    ChatRouterServiceClient crsc;
    CommandDelegator delegator;
    Language language;

    public static void main(final String[] args) throws Exception {
        new I18nTest().entryPoint(args);
    }

    @Override
    public String getName() {
        return "i18n_test";
    }

    @Override
    protected void initialize() {
        super.initialize();
        crsc = getServiceClient(ChatRouterServiceClient.class);
        delegator = new CommandDelegator("!");
        language = getI18n().getLanguage(null);
        delegator.registerCommand(new I18nTestCommand(this));
        registerListener(new I18nTestListener(this));
    }
}
