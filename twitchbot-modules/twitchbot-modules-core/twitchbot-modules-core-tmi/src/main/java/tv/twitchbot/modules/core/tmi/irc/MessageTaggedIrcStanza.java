package tv.twitchbot.modules.core.tmi.irc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/9/2016.
 */
public class MessageTaggedIrcStanza extends TaggedIrcStanza {
    public static class Emote {
        public static class Range{
            private int beginIndex;
            private int endIndex;

            public Range(String encoded) {
                String[] parts = encoded.split("-", 2);
                beginIndex = Integer.valueOf(parts[0]);
                endIndex = Integer.valueOf(parts[1]);
            }

            public Range(int beginIndex, int endIndex) {
                this.beginIndex = beginIndex;
                this.endIndex = endIndex;
            }

            public int getBeginIndex() {
                return beginIndex;
            }

            public int getEndIndex() {
                return endIndex;
            }
        }

        private String id;
        private Range[] ranges;

        public Emote(String encoded) {
            String[] parts = encoded.split(":");
            id = parts[0];
            String encodedRanges = parts[1];
            ranges = Arrays.asList(encodedRanges.split(",")).stream().map(Range::new).collect(Collectors.toList()).toArray(new Range[] {});
        }

        public Emote(String id, Range[] ranges) {
            this.id = id;
            this.ranges = ranges;
        }
    }

    public enum Badge {
        STAFF, ADMIN, GLOBAL_MOD,
        MODERATOR, SUBSCRIBER, TURBO;
    }

    private Set<Badge> badges;
    private List<Emote> emotes;
    private int roomId, userId;

    public MessageTaggedIrcStanza(String rawLine, Map<String, String> tags, IrcSource source, IrcCommand command, String rawArgs, String[] args) {
        super(rawLine, tags, source, command, rawArgs, args);
        if(tags.containsKey("badges") && !tags.get("badges").isEmpty())
            badges = Arrays.asList(tags.get("badges").split(",")).stream().map(String::toUpperCase).map(Badge::valueOf).collect(Collectors.toSet());
        if(tags.containsKey("emotes") && !tags.get("emotes").isEmpty())
            emotes = Arrays.asList(tags.get("emotes").split("/")).stream().map(Emote::new).collect(Collectors.toList());
        if(tags.containsKey("room-id") && !tags.get("room-id").isEmpty())
            roomId = Integer.valueOf(tags.get("room-id"));
        if(tags.containsKey("user-id") && !tags.get("user-id").isEmpty())
            userId = Integer.valueOf(tags.get("user-id"));
    }

    public Set<Badge> getBadges() {
        return badges;
    }

    public List<Emote> getEmotes() {
        return emotes;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getUserId() {
        return userId;
    }
}
