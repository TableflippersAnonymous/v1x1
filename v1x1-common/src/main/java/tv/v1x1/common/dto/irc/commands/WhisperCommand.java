package tv.v1x1.common.dto.irc.commands;

import tv.v1x1.common.dto.irc.IrcSource;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.MessageTaggedIrcStanza;
import tv.v1x1.common.dto.proto.core.IRC;

import java.util.Map;

/**
 * Created by cobi on 11/4/2016.
 */
public class WhisperCommand extends MessageTaggedIrcStanza {
    public static WhisperCommand fromProto(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final IRC.WhisperCommand whisperCommand) {
        final String target = whisperCommand.getTarget();
        final String message = whisperCommand.getMessage();
        return new WhisperCommand(rawLine, tags, source, rawArgs, args, target, message);
    }

    private final String target;
    private final String message;

    private Long id;
    private String threadId;

    public WhisperCommand(final String rawLine, final Map<String, String> tags, final IrcSource source, final String rawArgs, final String[] args, final String target, final String message) {
        super(rawLine, tags, source, IrcStanza.IrcCommand.WHISPER, rawArgs, args);
        this.target = target;
        this.message = message;

        if(tags.containsKey("message-id") && !tags.get("message-id").isEmpty())
            id = Long.valueOf(tags.get("message-id"));
        if(tags.containsKey("thread-id") && !tags.get("thread-id").isEmpty())
            threadId = tags.get("thread-id");
    }

    public String getTarget() {
        return target;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

    public String getThreadId() {
        return threadId;
    }

    @Override
    protected IRC.IrcStanza.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setCommand(IRC.IrcStanza.IrcCommand.WHISPER)
                .setExtension(IRC.WhisperCommand.data, toProtoCommand());
    }

    private IRC.WhisperCommand toProtoCommand() {
        final IRC.WhisperCommand.Builder builder = IRC.WhisperCommand.newBuilder()
                .setTarget(target)
                .setMessage(message);
        if(id != null)
            builder.setMessageId(id);
        if(threadId != null)
            builder.setThreadId(threadId);
        return builder.setMessageTaggedStanza(toProtoMessageTagged())
                .build();
    }
}
