package tv.twitchbot.common.rpc.services;

import tv.twitchbot.common.dto.messages.Request;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.modules.GlobalConfiguration;
import tv.twitchbot.common.modules.Module;
import tv.twitchbot.common.modules.ModuleSettings;
import tv.twitchbot.common.modules.TenantConfiguration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by naomi on 10/19/2016.
 */
public abstract class ThreadedService<T extends Request, U extends Response<T>> extends Service<T, U> {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public ThreadedService(final Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module, final String serviceName, final Class<T> requestClass) {
        super(module, serviceName, requestClass);
    }

    @Override
    protected void handleRequest(final T request) {
        executorService.submit(() -> ThreadedService.super.handleRequest(request));
    }
}
