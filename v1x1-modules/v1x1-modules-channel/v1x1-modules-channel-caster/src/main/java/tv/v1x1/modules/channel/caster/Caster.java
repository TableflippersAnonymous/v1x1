package tv.v1x1.modules.channel.caster;

import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.scanners.i18n.I18nDefault;
import tv.v1x1.common.scanners.i18n.I18nDefaults;
import tv.v1x1.common.scanners.permission.DefaultGroup;
import tv.v1x1.common.scanners.permission.Permissions;
import tv.v1x1.common.scanners.permission.RegisteredPermission;
import tv.v1x1.common.util.commands.CommandDelegator;

/**
 * @author Josh
 */
@Permissions(version = 1, value = {
        @RegisteredPermission(
                node = "caster.use",
                displayName = "Use Caster",
                description = "This allows you to use the !caster command",
                defaultGroups = {DefaultGroup.OWNER, DefaultGroup.BROADCASTER, DefaultGroup.MODS}
        )
})
@I18nDefaults(version = 1, value = {
        @I18nDefault(
                key = "notfound",
                message = "%commander%, I can't find %target% on Twitch BibleThump",
                displayName = "Not Found",
                description = "Sent when !caster is used on an invalid Twitch user"
        ),
        @I18nDefault(
                key = "nogame",
                message = "Everyone give %target% a follow! Check them out at %url%",
                displayName = "No Game",
                description = "Sent when !caster is used on a broadcaster who is not playing a specific game"
        ),
        @I18nDefault(
                key = "response",
                message = "Everyone give %target% a follow! They were last seen %summary%. Check them out at %url%",
                displayName = "Response",
                description = "Normal response to !caster command"
        ),
        @I18nDefault(
                key = "nostreams",
                message = "%commander%, %platform% doesn't support streaming",
                displayName = "No Stream Support",
                description = "Sent when !caster is used on a platform that doesn't support streaming"
        ),
        @I18nDefault(
                key = "invalidplatform",
                message = "%commander%, I don't support %platform%!",
                displayName = "No Platform Support",
                description = "Sent when !caster is used on a platform that v1x1 does not support"
        )
})
public class Caster extends RegisteredThreadedModule<CasterGlobalConfig, CasterUserConfig> implements EventListener {
    private static Caster instance;
    public static Caster getInstance() {
        return instance;
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
        instance = this;
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new CasterCommand(this));
        registerListener(this);
    }

    @EventHandler
    public void onChatMessage(ChatMessageEvent ev) {
        if(getConfiguration(ev.getChatMessage().getChannel()).isEnabled())
            delegator.handleChatMessage(ev);
    }
}
