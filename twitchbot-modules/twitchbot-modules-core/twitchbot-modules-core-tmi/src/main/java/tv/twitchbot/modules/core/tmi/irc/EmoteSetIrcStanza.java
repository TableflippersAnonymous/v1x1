package tv.twitchbot.modules.core.tmi.irc;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/9/2016.
 */
public class EmoteSetIrcStanza extends TaggedIrcStanza {
    private Set<Integer> emoteSets;

    public EmoteSetIrcStanza(String rawLine, Map<String, String> tags, IrcSource source, IrcCommand command, String rawArgs, String[] args) {
        super(rawLine, tags, source, command, rawArgs, args);
        if(tags.containsKey("emote-sets") && !tags.get("emote-sets").isEmpty())
            emoteSets = Arrays.asList(tags.get("emote-sets").split(",")).stream().map(Integer::valueOf).collect(Collectors.toSet());
    }

    public Set<Integer> getEmoteSets() {
        return emoteSets;
    }
}
