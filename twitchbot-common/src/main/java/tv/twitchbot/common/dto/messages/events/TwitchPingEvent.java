package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.PingCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when TMI sends us a keep-alive message
 * @author Naomi
 */
public class TwitchPingEvent extends Event {
    public static TwitchPingEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchPingEvent twitchPingEvent) {
        String token = twitchPingEvent.getToken();
        PingCommand pingCommand = (PingCommand) IrcStanza.fromProto(twitchPingEvent.getPingCommand());
        return new TwitchPingEvent(module, uuid, timestamp, token, pingCommand);
    }

    private String token;

    private PingCommand pingCommand;

    public TwitchPingEvent(Module from, String token, PingCommand pingCommand) {
        super(from);
        this.token = token;
        this.pingCommand = pingCommand;
    }

    public TwitchPingEvent(Module from, UUID messageId, long timestamp, String token, PingCommand pingCommand) {
        super(from, messageId, timestamp);
        this.token = token;
        this.pingCommand = pingCommand;
    }

    public String getToken() {
        return token;
    }

    public PingCommand getPingCommand() {
        return pingCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_PING)
                .setExtension(EventOuterClass.TwitchPingEvent.data, EventOuterClass.TwitchPingEvent.newBuilder()
                        .setToken(token)
                        .setPingCommand(pingCommand.toProto())
                        .build()
                );
    }
}
