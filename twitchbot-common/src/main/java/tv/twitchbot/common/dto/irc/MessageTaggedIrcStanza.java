package tv.twitchbot.common.dto.irc;

import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/9/2016.
 */
public abstract class MessageTaggedIrcStanza extends TaggedIrcStanza {
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

            public IRC.MessageTaggedIrcStanza.Emote.Range toProto() {
                return IRC.MessageTaggedIrcStanza.Emote.Range.newBuilder()
                        .setBeginIndex(beginIndex)
                        .setEndIndex(endIndex)
                        .build();
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

        public IRC.MessageTaggedIrcStanza.Emote toProto() {
            return IRC.MessageTaggedIrcStanza.Emote.newBuilder()
                    .setId(id)
                    .addAllRanges(Arrays.asList(ranges).stream().map(Range::toProto).collect(Collectors.toList()))
                    .build();
        }
    }

    public enum Badge {
        STAFF, ADMIN, GLOBAL_MOD,
        MODERATOR, SUBSCRIBER, TURBO;

        public IRC.MessageTaggedIrcStanza.Badge toProto() {
            switch(this) {
                case STAFF: return IRC.MessageTaggedIrcStanza.Badge.STAFF;
                case ADMIN: return IRC.MessageTaggedIrcStanza.Badge.ADMIN;
                case GLOBAL_MOD: return IRC.MessageTaggedIrcStanza.Badge.GLOBAL_MOD;
                case MODERATOR: return IRC.MessageTaggedIrcStanza.Badge.MODERATOR;
                case SUBSCRIBER: return IRC.MessageTaggedIrcStanza.Badge.SUBSCRIBER;
                case TURBO: return IRC.MessageTaggedIrcStanza.Badge.TURBO;
                default: throw new IllegalStateException("Unknown Badge: " + this);
            }
        }
    }

    private Set<Badge> badges = new HashSet<>();
    private List<Emote> emotes = new ArrayList<>();
    private int roomId, userId;

    public MessageTaggedIrcStanza(String rawLine, Map<String, String> tags, IrcSource source, IrcCommand command, String rawArgs, String[] args) {
        super(rawLine, tags, source, command, rawArgs, args);
        if(tags.containsKey("badges") && !tags.get("badges").isEmpty())
            badges = Arrays.asList(tags.get("badges").split(",")).stream().map(String::toUpperCase).map(s -> s.split("/")[0]).map(s -> {
                try {
                    return Badge.valueOf(s);
                } catch(IllegalArgumentException e) {
                    e.printStackTrace();
                    return null;
                }
            }).filter(badge -> badge != null).collect(Collectors.toSet());
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

    protected IRC.MessageTaggedIrcStanza toProtoMessageTagged() {
        return IRC.MessageTaggedIrcStanza.newBuilder()
                .addAllBadges(badges.stream().map(Badge::toProto).collect(Collectors.toList()))
                .addAllEmotes(emotes.stream().map(Emote::toProto).collect(Collectors.toList()))
                .setRoomId(roomId)
                .setUserId(userId)
                .setTaggedStanza(toProtoTagged())
                .build();
    }
}
