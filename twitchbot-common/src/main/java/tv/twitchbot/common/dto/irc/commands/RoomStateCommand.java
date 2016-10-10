package tv.twitchbot.common.dto.irc.commands;

import tv.twitchbot.common.dto.irc.IrcSource;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class RoomStateCommand extends IrcStanza {
    public static RoomStateCommand fromProto(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, IRC.RoomStateCommand roomStateCommand) {
        String channel = roomStateCommand.getChannel();
        return new RoomStateCommand(rawLine, tags, source, rawArgs, args, channel);
    }

    private String channel;

    private String broadcasterLanguage;
    private boolean r9k, subsOnly;
    private int slow;

    public RoomStateCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel) {
        super(rawLine, tags, source, IrcCommand.ROOMSTATE, rawArgs, args);
        this.channel = channel;
        if(tags.containsKey("broadcaster-lang") && !tags.get("broadcaster-lang").isEmpty())
            broadcasterLanguage = tags.get("broadcaster-lang");
        if(tags.containsKey("r9k") && !tags.get("r9k").isEmpty())
            r9k = tags.get("r9k").equals("1");
        if(tags.containsKey("subs-only") && !tags.get("subs-only").isEmpty())
            subsOnly = tags.get("subs-only").equals("1");
        if(tags.containsKey("slow") && !tags.get("slow").isEmpty())
            slow = Integer.valueOf(tags.get("slow"));
    }

    public String getChannel() {
        return channel;
    }

    public String getBroadcasterLanguage() {
        return broadcasterLanguage;
    }

    public boolean isR9k() {
        return r9k;
    }

    public boolean isSubsOnly() {
        return subsOnly;
    }

    public int getSlow() {
        return slow;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.ROOMSTATE)
                .setExtension(IRC.RoomStateCommand.data, toProtoCommand());
    }

    private IRC.RoomStateCommand toProtoCommand() {
        IRC.RoomStateCommand.Builder builder = IRC.RoomStateCommand.newBuilder()
                .setChannel(channel);
        if(broadcasterLanguage != null)
            builder.setBroadcasterLanguage(broadcasterLanguage);
        return builder.setR9K(r9k)
                .setSubsOnly(subsOnly)
                .setSlow(slow)
                .build();
    }
}
