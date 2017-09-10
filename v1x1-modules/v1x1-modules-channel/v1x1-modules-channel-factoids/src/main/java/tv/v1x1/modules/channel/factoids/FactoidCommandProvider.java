package tv.v1x1.modules.channel.factoids;

import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandProvider;

/**
 * @author Josh
 */
public class FactoidCommandProvider implements CommandProvider {
    final private FactoidsModule module;

    public FactoidCommandProvider(final FactoidsModule module) {
        this.module = module;
    }

    @Override
    public Command provide(final String command, final ChatMessage chatMessage) {
        try {
            final Factoid factoid = module.getConfiguration(chatMessage.getChannel()).chaseDownById(command);
            if(factoid != null && !factoid.isHidden()) {
                return new CustomCommand(module, command, factoid.getData(), factoid.getPermission());
            }
        } catch(RuntimeException ex) {
            Chat.i18nMessage(module, chatMessage.getChannel(), "toomany.aliases",
                    "commander", chatMessage.getSender().getDisplayName(),
                    "alias", command);
        }
        return null;
    }
}
