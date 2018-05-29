package tv.v1x1.modules.channel.voicelog;

import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.modules.channel.voicelog.commands.VoiceLogCommand;

/**
 * @author Josh
 */
public class VoiceLog extends RegisteredThreadedModule<VoiceLogGlobalConfiguration, VoiceLogUserConfiguration> {
    public static void main(final String[] args) throws Exception {
        new VoiceLog().entryPoint(args);
    }
    
    @Override
    public String getName() {
        return "voicelog";
    }
    @Override
    public void initialize() {
        super.initialize();
        new CommandDelegator("!").registerCommand(new VoiceLogCommand(this));
        registerListener(new VoiceLogListener(this));
    }
}
