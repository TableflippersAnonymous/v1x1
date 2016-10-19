package tv.twitchbot.modules.channel.i18n_test;

import tv.twitchbot.common.i18n.Language;
import tv.twitchbot.common.modules.RegisteredThreadedModule;
import tv.twitchbot.common.rpc.client.ChatRouterServiceClient;
import tv.twitchbot.common.util.commands.CommandDelegator;

/**
 * @author Josh
 */
public class I18nTest extends RegisteredThreadedModule<I18nTestSettings, I18nTestGlobalConfiguration, I18nTestTenantConfiguration> {
    ChatRouterServiceClient crsc;
    CommandDelegator delegator;
    Language language;

    public static void main(String[] args) throws Exception {
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
