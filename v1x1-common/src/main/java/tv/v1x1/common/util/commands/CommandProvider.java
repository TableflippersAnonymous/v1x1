package tv.v1x1.common.util.commands;

import tv.v1x1.common.dto.core.ChatMessage;

/**
 * @author Josh
 */
public interface CommandProvider {
    /**
     * Gets a Command provided by this CommandProvider
     * @param command
     * @return a Command this CommandProvider knows about, or null if it doesn't know what it is
     */
    public Command provide(final String command, final ChatMessage chatMessage);
}
