package tv.v1x1.common.dto.irc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.proto.core.IRC;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/9/2016.
 */
public abstract class MessageTaggedIrcStanza extends TaggedIrcStanza {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Set<Badge> badges = new HashSet<>();
    private List<Emote> emotes = new ArrayList<>();
    private int roomId, userId;

    public static class Emote {
        private final String id;
        private final Range[] ranges;

        public static class Range{
            private final int beginIndex;
            private final int endIndex;

            public Range(final String encoded) {
                final String[] parts = encoded.split("-", 2);
                beginIndex = Integer.valueOf(parts[0]);
                endIndex = Integer.valueOf(parts[1]);
            }

            public Range(final int beginIndex, final int endIndex) {
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

        public Emote(final String encoded) {
            final String[] parts = encoded.split(":");
            id = parts[0];
            final String encodedRanges = parts[1];
            ranges = Arrays.stream(encodedRanges.split(",")).map(Range::new).collect(Collectors.toList()).toArray(new Range[] {});
        }

        public Emote(final String id, final Range[] ranges) {
            this.id = id;
            this.ranges = ranges;
        }

        public IRC.MessageTaggedIrcStanza.Emote toProto() {
            return IRC.MessageTaggedIrcStanza.Emote.newBuilder()
                    .setId(id)
                    .addAllRanges(Arrays.stream(ranges).map(Range::toProto).collect(Collectors.toList()))
                    .build();
        }
    }

    public enum Badge {
        STAFF, ADMIN, GLOBAL_MOD,
        MODERATOR, SUBSCRIBER, TURBO,
        PREMIUM, BITS, BROADCASTER, TWITCHCON2017,
        PARTNER, CLIP_CHAMP, BITS_CHARITY, SUB_GIFTER, BITS_LEADER,
        OVERWATCH_LEAGUE_INSIDER_1, VIP, TWITCHCON2018;

        public IRC.MessageTaggedIrcStanza.Badge toProto() {
            switch(this) {
                case STAFF: return IRC.MessageTaggedIrcStanza.Badge.STAFF;
                case ADMIN: return IRC.MessageTaggedIrcStanza.Badge.ADMIN;
                case GLOBAL_MOD: return IRC.MessageTaggedIrcStanza.Badge.GLOBAL_MOD;
                case MODERATOR: return IRC.MessageTaggedIrcStanza.Badge.MODERATOR;
                case SUBSCRIBER: return IRC.MessageTaggedIrcStanza.Badge.SUBSCRIBER;
                case TURBO: return IRC.MessageTaggedIrcStanza.Badge.TURBO;
                case PREMIUM: return IRC.MessageTaggedIrcStanza.Badge.PREMIUM;
                case BITS: return IRC.MessageTaggedIrcStanza.Badge.BITS;
                case BROADCASTER: return IRC.MessageTaggedIrcStanza.Badge.BROADCASTER;
                case TWITCHCON2017: return IRC.MessageTaggedIrcStanza.Badge.TWITCHCON2017;
                case PARTNER: return IRC.MessageTaggedIrcStanza.Badge.PARTNER;
                case CLIP_CHAMP: return IRC.MessageTaggedIrcStanza.Badge.CLIP_CHAMP;
                case BITS_CHARITY: return IRC.MessageTaggedIrcStanza.Badge.BITS_CHARITY;
                case SUB_GIFTER: return IRC.MessageTaggedIrcStanza.Badge.SUB_GIFTER;
                case BITS_LEADER: return IRC.MessageTaggedIrcStanza.Badge.BITS_LEADER;
                case OVERWATCH_LEAGUE_INSIDER_1: return IRC.MessageTaggedIrcStanza.Badge.OVERWATCH_LEAGUE_INSIDER_1;
                case VIP: return IRC.MessageTaggedIrcStanza.Badge.VIP;
                case TWITCHCON2018: return IRC.MessageTaggedIrcStanza.Badge.TWITCHCON2018;
                default: throw new IllegalStateException("Unknown Badge: " + this);
            }
        }
    }

    public MessageTaggedIrcStanza(final String rawLine, final Map<String, String> tags, final IrcSource source, final IrcCommand command, final String rawArgs, final String[] args) {
        super(rawLine, tags, source, command, rawArgs, args);
        if(tags.containsKey("badges") && !tags.get("badges").isEmpty())
            badges = Arrays.stream(tags.get("badges").split(",")).map(String::toUpperCase).map(s -> s.split("/")[0]).map(s -> {
                try {
                    return Badge.valueOf(s.replaceAll("-", "_"));
                } catch(IllegalArgumentException e) {
                    LOG.warn("Unknown Badge: {}", s.replaceAll("-", "_"));
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet());
        if(tags.containsKey("emotes") && !tags.get("emotes").isEmpty())
            emotes = Arrays.stream(tags.get("emotes").split("/")).map(Emote::new).collect(Collectors.toList());
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
