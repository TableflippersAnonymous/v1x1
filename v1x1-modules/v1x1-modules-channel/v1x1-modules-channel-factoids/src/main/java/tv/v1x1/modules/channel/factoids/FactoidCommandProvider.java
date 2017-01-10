package tv.v1x1.modules.channel.factoids;

import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandProvider;
import tv.v1x1.modules.channel.factoids.dao.DAOFactoid;
import tv.v1x1.modules.channel.factoids.dao.Factoid;

/**
 * @author Josh
 */
public class FactoidCommandProvider implements CommandProvider {
    final private FactoidsModule module;
    final private DAOFactoid daoFactoid;

    public FactoidCommandProvider(final FactoidsModule module, final DAOFactoid daoFactoid) {
        this.module = module;
        this.daoFactoid = daoFactoid;
    }

    @Override
    public Command provide(final String command, final ChatMessage chatMessage) {
        final Factoid factoid = daoFactoid.chaseDownById(chatMessage.getChannel().getTenant().getId().getValue(), command);
        if(factoid != null) {
            return new CustomCommand(module, factoid.getId(), factoid.getData(), factoid.getPermission());
        }
        return null;
    }
}
