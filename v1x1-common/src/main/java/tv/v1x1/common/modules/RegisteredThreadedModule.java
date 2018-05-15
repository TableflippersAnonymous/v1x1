package tv.v1x1.common.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josh
 */
public abstract class RegisteredThreadedModule<T extends GlobalConfiguration, U extends UserConfiguration> extends ThreadedModule<T, U> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private EventListener listener;
    private List<Method> handlers;

    protected void registerListener(final EventListener listener) {
        this.listener = listener;
        handlers.clear();
        for(final Method m : listener.getClass().getDeclaredMethods()) {
            if(m.isAnnotationPresent(EventHandler.class) && m.getParameterCount() == 1 && Event.class.isAssignableFrom(m.getParameters()[0].getType())) {
                handlers.add(m);
                LOG.debug("Added event handler: {} in {}", m.getName(), listener.getClass().getCanonicalName());
            }
        }
    }

    @Override
    protected void processMessage(final Message message) {
        if(message instanceof Event) {
            for(final Method m : handlers) {
                if(m.getParameters()[0].getType().isInstance(message)) {
                    try {
                        m.invoke(listener, message);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        LOG.error("Failure calling handler for module", e);
                    }
                }
            }
        }
    }

    @Override
    protected void initialize() {
        handlers = new ArrayList<>();
    }
}
