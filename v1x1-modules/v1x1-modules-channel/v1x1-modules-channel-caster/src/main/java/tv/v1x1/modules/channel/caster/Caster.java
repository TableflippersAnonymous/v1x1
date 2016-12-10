package tv.v1x1.modules.channel.caster;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.util.commands.CommandDelegator;

/**
 * @author Josh
 */
public class Caster extends RegisteredThreadedModule<CasterModuleSettings, CasterGlobalConfig, CasterTenantSettings, CasterChannelSettings> implements EventListener {
    static {
        Module module = new Module("caster");
        I18n.registerDefault(module, "notfound", "%commander%, I can't find %target% on Twitch :[");
        I18n.registerDefault(module, "nogame", "Everyone give %target% a follow! https://twitch.tv/%target%");
        I18n.registerDefault(module, "response", "Everyone give %target% a follow! They were last seen %summary%. Check them out at https://twitch.tv/%target%");
    }
    private CommandDelegator delegator;

    @Override
    public String getName() {
        return "caster";
    }
    public static void main(final String[] args) throws Exception {
        new Caster().entryPoint(args);
    }

    @Override
    protected void initialize() {
        super.initialize();
        TwitchApi api = getTwitchApi();
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new CasterCommand(this));
        registerListener(this);
    }

    @EventHandler
    public void onChatMessage(ChatMessageEvent ev) {
        if(getTenantConfiguration(ev.getChatMessage().getChannel().getTenant()).isEnabled())
            delegator.handleChatMessage(ev);
    }
}
