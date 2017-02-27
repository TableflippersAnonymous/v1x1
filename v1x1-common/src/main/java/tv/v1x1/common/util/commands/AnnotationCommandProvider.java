package tv.v1x1.common.util.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.util.commands.annotations.CommandSet;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jcarter on 1/28/17.
 */
public class AnnotationCommandProvider implements CommandProvider {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final StaticCommandProvider rootCommandProvider = new StaticCommandProvider();
    private final Map<Class<?>, StaticCommandProvider> commandProviders = new HashMap<>();
    private final Injector injector;

    public AnnotationCommandProvider(final Injector injector) {
        this.injector = injector;
        try {
            scanCommands();
        } catch(final Exception e) {
            LOG.error("Got error scanning annotations.", e);
            throw e;
        }
    }

    @Override
    public Command provide(final String command, final ChatMessage chatMessage) {
        LOG.debug("Proxying provide command for {}", command);
        return rootCommandProvider.provide(command, chatMessage);
    }

    private void scanCommands() {
        LOG.debug("Scanning commands.");
        final Reflections reflections = new Reflections("tv.v1x1");
        reflections.getTypesAnnotatedWith(CommandSet.class).forEach(this::scanClass);
    }

    private void scanClass(final Class<?> clazz) {
        LOG.debug("Scanning {} ...", clazz.getCanonicalName());
        if(commandProviders.containsKey(clazz))
            return;
        if(!clazz.isAnnotationPresent(CommandSet.class))
            return;
        LOG.debug("@CommandSet present.");
        final Object obj = injector.getInstance(clazz);
        final StaticCommandProvider commandProvider;
        final CommandSet commandSet = clazz.getAnnotation(CommandSet.class);
        if(commandSet.value().equals("_ROOT_")) {
            commandProvider = rootCommandProvider;
            commandProviders.put(clazz, commandProvider);
        } else {
            commandProvider = new StaticCommandProvider();
            final CommandDelegator commandDelegator = new CommandDelegator(commandProvider, "");
            commandProviders.put(clazz, commandProvider);
            final Command command = new Command() {
                @Override
                public List<String> getCommands() {
                    return ImmutableList.of(commandSet.value());
                }

                @Override
                public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
                    LOG.debug("Processing command {}/{}", command, Joiner.on("/").join(args));
                    final String subCmd = args.get(0).toLowerCase();
                    if(!commandDelegator.handleParsedCommand(chatMessage, new ParsedCommand(subCmd, args.subList(1, args.size()))))
                        handleArgMismatch(chatMessage, command, args.subList(1, args.size()));
                }
            };
            if(!commandSet.parent().equals(Object.class)) {
                if(!commandProviders.containsKey(commandSet.parent()))
                    scanClass(commandSet.parent());
                if(!commandProviders.containsKey(commandSet.parent()))
                    throw new IllegalStateException("Unknown commandSet parent at " + clazz.getCanonicalName() + " looking for " + commandSet.parent().getCanonicalName());
                commandProviders.get(commandSet.parent()).registerCommand(command);
            } else {
                rootCommandProvider.registerCommand(command);
            }
        }
        for(final Method method : clazz.getMethods())
            scanMethod(clazz, obj, method, commandProvider);
    }

    private void scanMethod(final Class<?> clazz, final Object obj, final Method method, final StaticCommandProvider commandProvider) {
        LOG.debug("Scanning method {}/{}", clazz.getCanonicalName(), method.getName());
        if(!method.isAnnotationPresent(tv.v1x1.common.util.commands.annotations.Command.class))
            return;
        final tv.v1x1.common.util.commands.annotations.Command commandAnnotation = method.getAnnotation(tv.v1x1.common.util.commands.annotations.Command.class);
        commandProvider.registerCommand(new Command() {
            @Override
            public List<String> getCommands() {
                return Arrays.asList(commandAnnotation.value());
            }

            @Override
            public List<Permission> getAllowedPermissions() {
                if(commandAnnotation.permissions().length == 0)
                    return null;
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
                LOG.debug("Executing command {}/{} ...", clazz.getCanonicalName(), method.getName());
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
                        parameters[i] = injector.getInstance(parameterType);
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
