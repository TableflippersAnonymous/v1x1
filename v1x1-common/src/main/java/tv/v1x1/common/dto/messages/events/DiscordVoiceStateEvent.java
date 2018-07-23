package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;
import tv.v1x1.common.services.discord.dto.voice.VoiceState;

import java.util.List;
import java.util.stream.Collectors;

public class DiscordVoiceStateEvent extends Event {
    public static DiscordVoiceStateEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.DiscordVoiceStateEvent discordVoiceStateEvent) {
        final VoiceState oldVoiceState = discordVoiceStateEvent.hasOldVoiceState() ? VoiceState.fromProto(discordVoiceStateEvent.getOldVoiceState()) : null;
        final VoiceState newVoiceState = VoiceState.fromProto(discordVoiceStateEvent.getNewVoiceState());
        final List<Permission> perms =  discordVoiceStateEvent.getPermissionsList().stream().map(Permission::fromProto).collect(Collectors.toList());
        return new DiscordVoiceStateEvent(module, uuid, timestamp, context, oldVoiceState, newVoiceState, perms);
    }

    private final VoiceState oldVoiceState;
    private final VoiceState newVoiceState;
    private final List<Permission> permissions;

    public DiscordVoiceStateEvent(final Module from, final VoiceState oldVoiceState, final VoiceState newVoiceState, final List<Permission> permissionList) {
        super(from);
        this.oldVoiceState = oldVoiceState;
        this.newVoiceState = newVoiceState;
        this.permissions = permissionList;
    }

    public DiscordVoiceStateEvent(final Module from, final UUID messageId, final long timestamp, final Context context,
                                  final VoiceState oldVoiceState, final VoiceState newVoiceState, final List<Permission> permissionList) {
        super(from, messageId, timestamp, context);
        this.oldVoiceState = oldVoiceState;
        this.newVoiceState = newVoiceState;
        this.permissions = permissionList;
    }

    public VoiceState getOldVoiceState() {
        return oldVoiceState;
    }

    public VoiceState getNewVoiceState() {
        return newVoiceState;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        EventOuterClass.DiscordVoiceStateEvent.Builder discordVoiceStateEvent = EventOuterClass.DiscordVoiceStateEvent.newBuilder();
        if(oldVoiceState != null)
            discordVoiceStateEvent = discordVoiceStateEvent.setOldVoiceState(oldVoiceState.toProto());
        discordVoiceStateEvent = discordVoiceStateEvent.setNewVoiceState(newVoiceState.toProto());
        discordVoiceStateEvent = discordVoiceStateEvent.addAllPermissions(permissions.stream().map(Permission::toProto).collect(Collectors.toList()));
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.DISCORD_VOICE_STATE)
                .setExtension(EventOuterClass.DiscordVoiceStateEvent.data, discordVoiceStateEvent.build());
    }
}
