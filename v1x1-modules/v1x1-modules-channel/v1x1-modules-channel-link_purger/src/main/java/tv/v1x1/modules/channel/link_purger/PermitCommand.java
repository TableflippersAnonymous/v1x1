package tv.v1x1.modules.channel.link_purger;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.services.state.NoSuchUserException;
import tv.v1x1.common.util.commands.Command;

import java.util.List;

/**
 * @author Josh
 */
public class PermitCommand extends Command {
    private final LinkPurger module;
    public PermitCommand(LinkPurger linkPurger) {
        module = linkPurger;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("permit");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("link_purger.permit"));
    }

    @Override
    public String getUsage() {
        return "permit <user>";
    }

    @Override
    public String getDescription() {
        return "Allows a user to post a link";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commanderName = chatMessage.getSender().getMention();
        try {
            // TODO: Create and use a target-finding service, then convert !permit to use it
            final String targetId = module.getStateManager()
                    .getDisplayNameService()
                    .getIdFromDisplayName(channel, args.get(0));
            final String targetName = module.getStateManager().getDisplayNameService().getDisplayNameFromId(channel, targetId);
            if (module.permitUser(channel, targetId)) {
                Chat.i18nMessage(module, channel, "permit",
                        "target", targetName
                );
            } else {
                Chat.i18nMessage(module, channel, "permitfailed",
                        "commander", commanderName,
                        "target", targetName
                );
            }
        } catch(final NoSuchUserException e) {
            Chat.i18nMessage(module, channel, "notarget",
                    "commander", commanderName,
                    "target", args.get(0)
            );
        }
    }
}
