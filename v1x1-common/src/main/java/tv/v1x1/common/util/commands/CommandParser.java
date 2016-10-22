package tv.v1x1.common.util.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Josh on 2016-10-06.
 */
public class CommandParser {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static ParsedCommand parse(final ChatMessageEvent chatMessageEvent, final String prefix) {
        final String msg = chatMessageEvent.getChatMessage().getText();
        if(msg == null) return null;
        LOG.debug("prefix={} msg={}", prefix, msg);
        if(msg.startsWith(prefix)) {
            final List<String> args = new ArrayList<>(Arrays.asList(msg.substring(prefix.length()).split(" ")));
            final String command = args.remove(0);
            return new ParsedCommand(command, args);
        }
        return null;
    }
}
