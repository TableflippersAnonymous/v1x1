package tv.v1x1.common.rpc.client;

import brave.Span;
import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.dto.messages.responses.ExceptionResponse;
import tv.v1x1.common.modules.Module;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * If you want to tell another module to do something, you extend this
 */
public abstract class ServiceClient<T extends Request, U extends Response<T>> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Module<?, ?> module;
    private final String queueName;
    private final Map<tv.v1x1.common.dto.core.UUID, ServiceFuture<U>> futureMap = new ConcurrentHashMap<>();
    private final Map<tv.v1x1.common.dto.core.UUID, Span> spanMap = new ConcurrentHashMap<>();
    private final Class<U> responseClass;

    public ServiceClient(final Module<?, ?> module, final Class<U> responseClass) {
        this.module = module;
        this.queueName = module.getInstanceQueueName();
        this.responseClass = responseClass;
    }

    protected Future<U> send(final T request) {
        final ServiceFuture<U> future = new ServiceFuture<>();
        futureMap.put(request.getMessageId(), future);
        module.expectResponseTo(request.getMessageId(), this);
        final Tracer tracer = module.getInjector().getInstance(Tracer.class);
        final Span currentSpan = tracer.currentSpan();
        if(currentSpan != null) {
            final Span span = tracer.newChild(currentSpan.context())
                    .name(getClass().getCanonicalName())
                    .kind(Span.Kind.CLIENT);
            spanMap.put(request.getMessageId(), span);
            span.start();
        }
        module.send("Service|" + getServiceName(), request);
        return future;
    }

    public void shutdown() {
        futureMap.keySet()
                .forEach(module::clearResponseTo);
    }

    protected String getQueueName() {
        return queueName;
    }

    protected tv.v1x1.common.dto.core.Module getModule() {
        return module.toDto();
    }

    protected abstract String getServiceName();

    public void handle(final Response m) {
        try {
            if (!responseClass.isInstance(m) && !(m instanceof ExceptionResponse)) {
                LOG.warn("Invalid class seen on response queue: {} expected: {}", m.getClass().getCanonicalName(), responseClass.getCanonicalName());
                return;
            }
            final Span span = spanMap.remove(m.getRequestMessageId());
            if(span != null)
                span.finish();
            if(m instanceof ExceptionResponse) {
                final ExceptionResponse exceptionResponse = (ExceptionResponse) m;
                final ServiceFuture<U> future = futureMap.remove(exceptionResponse.getRequestMessageId());
                if (future == null)
                    return;
                future.setException(new RpcException(exceptionResponse.getExceptionClass(), exceptionResponse.getMessage(), exceptionResponse.getStackTrace()));
            } else {
                @SuppressWarnings("unchecked") final U response = (U) m;
                final ServiceFuture<U> future = futureMap.remove(response.getRequestMessageId());
                if (future == null)
                    return;
                future.set(response);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
