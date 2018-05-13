package tv.v1x1.common.rpc.services;

import brave.propagation.CurrentTraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.dto.messages.responses.ExceptionResponse;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.services.queue.MessageQueue;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cobi on 10/8/2016.
 */
public abstract class Service<T extends Request, U extends Response<T>> implements Comparable<Service<T, U>> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Module<?, ?> module;
    private final ExecutorService executorService;
    private final String serviceName;
    private final Class<T> requestClass;

    public Service(final Module<?, ?> module, final String serviceName, final Class<T> requestClass) {
        this.module = module;
        this.executorService = module.getInjector().getInstance(CurrentTraceContext.class).executorService(Executors.newSingleThreadExecutor());
        this.serviceName = serviceName;
        this.requestClass = requestClass;
    }

    public void start() {
        executorService.submit(() -> {
            final MessageQueue messageQueue = module.getMessageQueueManager().forName(getServiceQueue());
            for(;;) {
                try {
                    final Message m = messageQueue.get();
                    LOG.debug("Processing message id={} from={} class={}", m.getMessageId(), m.getFrom().getName(), m.getClass().getCanonicalName());
                    if(!requestClass.isInstance(m)) {
                        LOG.warn("Invalid class seen on request queue: {} expected: {}", m.getClass().getCanonicalName(), requestClass.getCanonicalName());
                        continue;
                    }
                    @SuppressWarnings("unchecked") final T request = (T) m;
                    handleRequest(request);
                } catch (final InterruptedException e) {
                    LOG.info("Exiting service due to InterruptedException", e);
                    break;
                } catch (final Exception e) {
                    LOG.warn("Caught exception processing service call:", e);
                }
            }
        });
    }

    protected void handleRequest(final T request) {
        try {
            module.send(request.getResponseQueueName(), call(request));
        } catch(final Exception e) {
            LOG.warn("Got exception while responding to request.", e);
            module.send(request.getResponseQueueName(), new ExceptionResponse(module.toDto(), request.getMessageId(), e));
            throw e;
        }
    }

    protected Module<?, ?> getModule() {
        return module;
    }

    public void shutdown() {
        executorService.shutdownNow();
    }

    protected abstract U call(T request);

    private String getServiceQueue() {
        return "Service|" + serviceName;
    }

    @Override
    public int compareTo(final Service<T, U> o) {
        return serviceName.compareTo(o.serviceName);
    }
}
