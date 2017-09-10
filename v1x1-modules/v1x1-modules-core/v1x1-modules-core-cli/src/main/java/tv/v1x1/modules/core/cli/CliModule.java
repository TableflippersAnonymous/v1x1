package tv.v1x1.modules.core.cli;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.messages.Message;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.util.commands.AnnotationCommandProvider;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.commands.ParsedCommand;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

/**
 * Created by cobi on 3/4/2017.
 */
public class CliModule extends Module<CliGlobalConfig, CliUserConfig> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String[] args;

    public CliModule(final String[] args) {
        this.args = args;
    }

    @Override
    public String getName() {
        return "cli";
    }

    @Override
    protected void handle(final Message message) {
        /* Intentionally left empty */
    }

    @Override
    protected void initialize() {
        final Injector childInjector = getInjector().createChildInjector(new GuiceModule());
        final CommandDelegator commandDelegator = new CommandDelegator(new AnnotationCommandProvider(childInjector), "");
        commandDelegator.handleParsedCommand(
                new ChatMessage(null, null, Joiner.on(" ").join(args), ImmutableList.of(new Permission("cli"))),
                new ParsedCommand(args[0], Arrays.asList(args).subList(1, args.length))
        );
        System.exit(0);
    }

    @Override
    protected void shutdown() {

    }

    public static void main(final String[] args) throws Exception {
        if(args.length < 2) {
            LOG.error("Expected at least 2 arguments: config.yml <subcommand>");
            throw new RuntimeException("Error.  Expected at least 2 arguments: config.yml <subcommand>");
        }
        new CliModule(Arrays.asList(args).subList(1, args.length).toArray(new String[] {})).entryPoint(Arrays.asList(args).subList(0, 1).toArray(new String[] {}));
    }
}
