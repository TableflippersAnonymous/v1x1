package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.PrivateMessage;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.WhisperCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.proto.core.PlatformOuterClass;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 11/4/2016.
 */
public class TwitchPrivateMessageEvent extends PrivateMessageEvent {
    public static TwitchPrivateMessageEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final PrivateMessage privateMessage, final EventOuterClass.TwitchPrivateMessageEvent twitchPrivateMessageEvent) {
        final WhisperCommand whisperCommand = (WhisperCommand) IrcStanza.fromProto(twitchPrivateMessageEvent.getWhisperCommand());
        return new TwitchPrivateMessageEvent(module, uuid, timestamp, context, privateMessage, whisperCommand);
    }

    private final WhisperCommand whisperCommand;

    public TwitchPrivateMessageEvent(final Module from, final PrivateMessage privateMessage, final WhisperCommand whisperCommand) {
        super(from, privateMessage);
        this.whisperCommand = whisperCommand;
    }

    public TwitchPrivateMessageEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final PrivateMessage privateMessage, final WhisperCommand whisperCommand) {
        super(from, messageId, timestamp, context, privateMessage);
        this.whisperCommand = whisperCommand;
    }

    public WhisperCommand getWhisperCommand() {
        return whisperCommand;
    }

    @Override
    protected EventOuterClass.PrivateMessageEvent.Builder toProtoPrivateMessage() {
        return super.toProtoPrivateMessage()
                .setType(PlatformOuterClass.Platform.TWITCH)
                .setExtension(EventOuterClass.TwitchPrivateMessageEvent.data, EventOuterClass.TwitchPrivateMessageEvent.newBuilder()
                        .setWhisperCommand(whisperCommand.toProto())
                        .build()
                );
    }
}
