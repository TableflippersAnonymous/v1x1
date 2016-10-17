package tv.twitchbot.common.modules;

import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.messages.Message;
import tv.twitchbot.common.modules.eventhandler.EventHandler;
import tv.twitchbot.common.modules.eventhandler.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josh
 */
public abstract class RegisteredThreadedModule<T extends ModuleSettings, U extends GlobalConfiguration, V extends TenantConfiguration> extends ThreadedModule<T, U, V> {

    private EventListener listener;
    private List<Method> handlers;

    protected void registerListener(EventListener listener) {
        this.listener = listener;
        handlers.clear();
        for(Method m : listener.getClass().getDeclaredMethods()) {
            if(m.isAnnotationPresent(EventHandler.class)) {
                if(m.getParameterCount() == 1 && Event.class.isAssignableFrom(m.getParameters()[0].getType())) {
                    handlers.add(m);
                }
            }
        }
    }

    @Override
    protected void processMessage(Message message) {
        if(message instanceof Event) {
            for(Method m : handlers) {
                if(m.getParameters()[0].getType().equals(message.getClass())) {
                    try {
                        m.invoke(listener, message);
                    } catch (IllegalAccessException e) {
                        System.out.println("Failure calling handler for module");
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        System.out.println("Failure calling handler for module");
                        e.printStackTrace();
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
