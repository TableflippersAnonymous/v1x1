package tv.twitchbot.common.rpc.services;

import com.google.protobuf.InvalidProtocolBufferException;
import tv.twitchbot.common.dto.messages.Message;
import tv.twitchbot.common.dto.messages.Request;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.modules.GlobalConfiguration;
import tv.twitchbot.common.modules.Module;
import tv.twitchbot.common.modules.ModuleSettings;
import tv.twitchbot.common.modules.TenantConfiguration;
import tv.twitchbot.common.services.queue.MessageQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by naomi on 10/8/2016.
 */
public abstract class Service<T extends Request, U extends Response<T>> implements Comparable<Service<T, U>> {
    private Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String serviceName;
    private Class<T> requestClass;

    public Service(Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> module, String serviceName, final Class<T> requestClass) {
        this.module = module;
        this.serviceName = serviceName;
        this.requestClass = requestClass;
    }

    public void start() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                MessageQueue messageQueue = module.getMessageQueueManager().forName(getServiceQueue());
                for(;;) {
                    try {
                        Message m = messageQueue.get();
                        if(!requestClass.isInstance(m))
                            continue;
                        T request = (T) m;
                        module.send(request.getResponseQueueName(), call(request));
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                        continue;
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
    }

    protected Module<? extends ModuleSettings, ? extends GlobalConfiguration, ? extends TenantConfiguration> getModule() {
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
    public int compareTo(Service<T, U> o) {
        return serviceName.compareTo(o.serviceName);
    }
}
