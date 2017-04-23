package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.PingCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when TMI sends us a keep-alive message
 * @author Cobi
 */
public class TwitchPingEvent extends Event {
    public static TwitchPingEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.TwitchPingEvent twitchPingEvent) {
        final String token = twitchPingEvent.getToken();
        final PingCommand pingCommand = (PingCommand) IrcStanza.fromProto(twitchPingEvent.getPingCommand());
        return new TwitchPingEvent(module, uuid, timestamp, context, token, pingCommand);
    }

    private final String token;

    private final PingCommand pingCommand;

    public TwitchPingEvent(final Module from, final String token, final PingCommand pingCommand) {
        super(from);
        this.token = token;
        this.pingCommand = pingCommand;
    }

    public TwitchPingEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final String token, final PingCommand pingCommand) {
        super(from, messageId, timestamp, context);
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
