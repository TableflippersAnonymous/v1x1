package tv.v1x1.common.util.commands;

import com.google.inject.Injector;
import org.reflections.Reflections;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.util.commands.annotations.CommandSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jcarter on 1/28/17.
 */
public class AnnotationCommandProvider implements CommandProvider {
    private final StaticCommandProvider staticCommandProvider = new StaticCommandProvider();
    private final Injector injector;

    public AnnotationCommandProvider(final Injector injector) {
        this.injector = injector;
        scanCommands();
    }

    @Override
    public Command provide(final String command, final ChatMessage chatMessage) {
        return staticCommandProvider.provide(command, chatMessage);
    }

    private void scanCommands() {
        final Reflections reflections = new Reflections("tv.v1x1");
        for(final Class<?> clazz : reflections.getTypesAnnotatedWith(CommandSet.class))
            scanClass(clazz);
    }

    private void scanClass(final Class<?> clazz) {
        final Object obj = injector.getInstance(clazz);
        for(final Method method : clazz.getMethods())
            scanMethod(clazz, obj, method);
    }

    private void scanMethod(final Class<?> clazz, final Object obj, final Method method) {
        if(!method.isAnnotationPresent(tv.v1x1.common.util.commands.annotations.Command.class))
            return;
        final tv.v1x1.common.util.commands.annotations.Command commandAnnotation = method.getAnnotation(tv.v1x1.common.util.commands.annotations.Command.class);
        staticCommandProvider.registerCommand(new Command() {
            @Override
            public List<String> getCommands() {
                return Arrays.asList(commandAnnotation.value());
            }

            @Override
            public List<Permission> getAllowedPermissions() {
                return Arrays.stream(commandAnnotation.permissions()).map(Permission::new).collect(Collectors.toList());
            }

            @Override
            public String getUsage() {
                return commandAnnotation.usage();
            }

            @Override
            public String getDescription() {
                return commandAnnotation.description();
            }

            @Override
            public String getHelp() {
                return commandAnnotation.help();
            }

            @Override
            public int getMinArgs() {
                return commandAnnotation.minArgs();
            }

            @Override
            public int getMaxArgs() {
                return commandAnnotation.maxArgs();
            }

            @Override
            public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
                final Object[] parameters = new Object[method.getParameterCount()];
                for(int i = 0; i < parameters.length; i++) {
                    final Class<?> parameterType = method.getParameterTypes()[i];
                    if(parameterType.isAssignableFrom(chatMessage.getClass()))
                        parameters[i] = chatMessage;
                    else if(parameterType.isAssignableFrom(command.getClass()))
                        parameters[i] = command;
                    else if(parameterType.isAssignableFrom(args.getClass()))
                        parameters[i] = args;
                    else
                        throw new IllegalStateException("Unknown parameter type: " + parameterType.getCanonicalName() + "/" + i + " in " + clazz.getCanonicalName() + "/" + method.getName());
                }
                try {
                    method.invoke(obj, parameters);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
