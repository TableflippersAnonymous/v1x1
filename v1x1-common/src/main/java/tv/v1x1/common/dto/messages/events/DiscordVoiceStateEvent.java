package tv.v1x1.common.dto.messages.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;
import tv.v1x1.common.services.discord.dto.voice.VoiceState;

import java.lang.invoke.MethodHandles;

public class DiscordVoiceStateEvent extends Event {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static DiscordVoiceStateEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.DiscordVoiceStateEvent discordVoiceStateEvent) {
        LOG.info("Decoding protobuf: {}", discordVoiceStateEvent.toString());
        final VoiceState oldVoiceState = discordVoiceStateEvent.hasOldVoiceState() ? VoiceState.fromProto(discordVoiceStateEvent.getOldVoiceState()) : null;
        final VoiceState newVoiceState = VoiceState.fromProto(discordVoiceStateEvent.getNewVoiceState());
        return new DiscordVoiceStateEvent(module, uuid, timestamp, context, oldVoiceState, newVoiceState);
    }

    private final VoiceState oldVoiceState;
    private final VoiceState newVoiceState;

    public DiscordVoiceStateEvent(final Module from, final VoiceState oldVoiceState, final VoiceState newVoiceState) {
        super(from);
        this.oldVoiceState = oldVoiceState;
        this.newVoiceState = newVoiceState;
    }

    public DiscordVoiceStateEvent(final Module from, final UUID messageId, final long timestamp, final Context context,
                                  final VoiceState oldVoiceState, final VoiceState newVoiceState) {
        super(from, messageId, timestamp, context);
        this.oldVoiceState = oldVoiceState;
        this.newVoiceState = newVoiceState;
    }

    public VoiceState getOldVoiceState() {
        return oldVoiceState;
    }

    public VoiceState getNewVoiceState() {
        return newVoiceState;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        EventOuterClass.DiscordVoiceStateEvent.Builder discordVoiceStateEvent = EventOuterClass.DiscordVoiceStateEvent.newBuilder();
        if(oldVoiceState != null)
            discordVoiceStateEvent = discordVoiceStateEvent.setOldVoiceState(oldVoiceState.toProto());
        discordVoiceStateEvent = discordVoiceStateEvent.setNewVoiceState(newVoiceState.toProto());
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.DISCORD_VOICE_STATE)
                .setExtension(EventOuterClass.DiscordVoiceStateEvent.data, discordVoiceStateEvent.build());
    }

    @Override
    public String toString() {
        return "DiscordVoiceStateEvent{" +
                "oldVoiceState=" + oldVoiceState +
                ", newVoiceState=" + newVoiceState +
                '}';
    }
}
