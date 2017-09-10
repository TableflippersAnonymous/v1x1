package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.channel.Channel;
import tv.v1x1.common.services.discord.dto.emoji.Emoji;
import tv.v1x1.common.services.discord.dto.permissions.Role;
import tv.v1x1.common.services.discord.dto.user.PresenceUpdate;
import tv.v1x1.common.services.discord.dto.voice.PartialVoiceState;

import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
public class CompleteGuild extends Guild {
    @JsonProperty("joined_at")
    private String joinedAt;
    @JsonProperty
    private boolean large;
    @JsonProperty("member_count")
    private Integer memberCount;
    @JsonProperty("voice_states")
    private List<PartialVoiceState> voiceStates;
    @JsonProperty
    private List<GuildMember> members;
    @JsonProperty
    private List<Channel> channels;
    @JsonProperty
    private List<PresenceUpdate> presences;

    public CompleteGuild() {
    }

    public CompleteGuild(final String id, final boolean unavailable, final String name, final String icon,
                         final String splash, final String ownerId, final String regionId, final String afkChannelId,
                         final Integer afkTimeout, final boolean embedEnabled, final String embedChannelId,
                         final VerificationLevel verificationLevel,
                         final MessageNotificationLevel defaultMessageNotifications,
                         final ExplicitContentFilterLevel explicitContentFilter, final List<Role> roles,
                         final List<Emoji> emojis, final List<String> features, final MFALevel mfaLevel,
                         final String applicationId, final boolean widgetEnabled, final String widgetChannelId,
                         final String joinedAt, final boolean large, final Integer memberCount,
                         final List<PartialVoiceState> voiceStates, final List<GuildMember> members,
                         final List<Channel> channels, final List<PresenceUpdate> presences) {
        super(id, unavailable, name, icon, splash, ownerId, regionId, afkChannelId, afkTimeout, embedEnabled,
                embedChannelId, verificationLevel, defaultMessageNotifications, explicitContentFilter, roles, emojis,
                features, mfaLevel, applicationId, widgetEnabled, widgetChannelId);
        this.joinedAt = joinedAt;
        this.large = large;
        this.memberCount = memberCount;
        this.voiceStates = voiceStates;
        this.members = members;
        this.channels = channels;
        this.presences = presences;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(final String joinedAt) {
        this.joinedAt = joinedAt;
    }

    public boolean isLarge() {
        return large;
    }

    public void setLarge(final boolean large) {
        this.large = large;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(final Integer memberCount) {
        this.memberCount = memberCount;
    }

    public List<PartialVoiceState> getVoiceStates() {
        return voiceStates;
    }

    public void setVoiceStates(final List<PartialVoiceState> voiceStates) {
        this.voiceStates = voiceStates;
    }

    public List<GuildMember> getMembers() {
        return members;
    }

    public void setMembers(final List<GuildMember> members) {
        this.members = members;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(final List<Channel> channels) {
        this.channels = channels;
    }

    public List<PresenceUpdate> getPresences() {
        return presences;
    }

    public void setPresences(final List<PresenceUpdate> presences) {
        this.presences = presences;
    }
}
