package tv.v1x1.common.rpc.services;

import brave.propagation.CurrentTraceContext;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.modules.Module;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cobi on 10/19/2016.
 */
public abstract class ThreadedService<T extends Request, U extends Response<T>> extends Service<T, U> {
    private final ExecutorService executorService = getModule().getInjector().getInstance(CurrentTraceContext.class)
            .executorService(Executors.newCachedThreadPool());

    public ThreadedService(final Module<?, ?> module, final String serviceName, final Class<T> requestClass) {
        super(module, serviceName, requestClass);
    }

    @Override
    protected void handleRequest(final T request) {
        executorService.submit(() -> ThreadedService.super.handleRequest(request));
    }
}
