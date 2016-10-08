package tv.twitchbot.common.rpc.client;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.twitchbot.common.dto.messages.Message;
import tv.twitchbot.common.dto.messages.Request;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.modules.GlobalConfiguration;
import tv.twitchbot.common.modules.Module;
import tv.twitchbot.common.modules.ModuleSettings;
import tv.twitchbot.common.modules.TenantConfiguration;
import tv.twitchbot.common.services.queue.MessageQueue;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by naomi on 10/8/2016.
 */
public abstract class ServiceClient<T extends Request, U extends Response<T>> {
    private final Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module;
    private final String queueName;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Map<tv.twitchbot.common.dto.core.UUID, ServiceFuture<U>> futureMap = new ConcurrentHashMap<>();

    public ServiceClient(Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module, final Class<U> responseClass) {
        this.module = module;
        this.queueName = "ServiceResponse|" + module.getName() + "|" + getClass().getCanonicalName() + "|" + UUID.randomUUID().toString();
        final MessageQueue messageQueue = module.getMessageQueueManager().forName(queueName);
        executorService.submit(() -> {
            for (; !Thread.interrupted(); ) {
                try {
                    Message m = messageQueue.get();
                    if (!responseClass.isInstance(m))
                        continue;
                    U response = (U) m;
                    ServiceFuture<U> future = futureMap.remove(response.getRequestMessageId());
                    if (future == null)
                        continue;
                    future.set(response);
                } catch (InterruptedException e) {
                    break;
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        });
    }

    protected ServiceFuture<U> send(T request) {
        ServiceFuture<U> future = new ServiceFuture<>();
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

    protected tv.twitchbot.common.dto.core.Module getModule() {
        return module.toDto();
    }

    protected abstract String getServiceName();
}
