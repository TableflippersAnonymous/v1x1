package tv.twitchbot.common.util.commands;

import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Josh on 2016-10-06.
 */
public class CommandParser {
    public static ParsedCommand parse(ChatMessageEvent chatMessageEvent, String prefix) {
        String msg = chatMessageEvent.getChatMessage().getText();
        if(msg == null) return null;
        if(msg.startsWith(prefix)) {
            List<String> args = Arrays.asList(msg.substring(prefix.length()).split(" "));
            String command = args.remove(0);
            return new ParsedCommand(command, args);
        }
        return null;
    }
}
