package tv.v1x1.common.rpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.modules.ModuleSettings;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.services.queue.MessageQueue;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * If you want to tell another module to do something, you extend this
 */
public abstract class ServiceClient<T extends Request, U extends Response<T>> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module;
    private final String queueName;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Map<tv.v1x1.common.dto.core.UUID, ServiceFuture<U>> futureMap = new ConcurrentHashMap<>();

    public ServiceClient(final Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module, final Class<U> responseClass) {
        this.module = module;
        this.queueName = "ServiceResponse|" + module.getName() + "|" + getClass().getCanonicalName() + "|" + UUID.randomUUID().toString();
        final MessageQueue messageQueue = module.getMessageQueueManager().forName(queueName);
        executorService.submit(() -> {
            while(!Thread.interrupted()) {
                try {
                    final Message m = messageQueue.get();
                    if (!responseClass.isInstance(m)) {
                        LOG.warn("Invalid class seen on response queue: {} expected: {}", m.getClass().getCanonicalName(), responseClass.getCanonicalName());
                        continue;
                    }
                    @SuppressWarnings("unchecked") final U response = (U) m;
                    final ServiceFuture<U> future = futureMap.remove(response.getRequestMessageId());
                    if (future == null)
                        continue;
                    future.set(response);
                } catch (final InterruptedException e) {
                    break;
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected Future<U> send(final T request) {
        final ServiceFuture<U> future = new ServiceFuture<>();
        futureMap.put(request.getMessageId(), future);
        module.send("Service|" + getServiceName(), request);
        return future;
    }

    public void shutdown() {
        executorService.shutdownNow();
    }

    protected String getQueueName() {
        return queueName;
    }

    protected tv.v1x1.common.dto.core.Module getModule() {
        return module.toDto();
    }

    protected abstract String getServiceName();
}
