package tv.v1x1.common.services.discord.dto.voice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartialVoiceState {
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("session_id")
    private String sessionId;
    @JsonProperty
    private boolean deaf;
    @JsonProperty
    private boolean mute;
    @JsonProperty("self_deaf")
    private boolean selfDeaf;
    @JsonProperty("self_mute")
    private boolean selfMute;
    @JsonProperty
    private boolean suppress;

    public PartialVoiceState() {
    }

    public PartialVoiceState(final String channelId, final String userId, final String sessionId, final boolean deaf,
                             final boolean mute, final boolean selfDeaf, final boolean selfMute, final boolean suppress) {
        this.channelId = channelId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.deaf = deaf;
        this.mute = mute;
        this.selfDeaf = selfDeaf;
        this.selfMute = selfMute;
        this.suppress = suppress;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isDeaf() {
        return deaf;
    }

    public void setDeaf(final boolean deaf) {
        this.deaf = deaf;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(final boolean mute) {
        this.mute = mute;
    }

    public boolean isSelfDeaf() {
        return selfDeaf;
    }

    public void setSelfDeaf(final boolean selfDeaf) {
        this.selfDeaf = selfDeaf;
    }

    public boolean isSelfMute() {
        return selfMute;
    }

    public void setSelfMute(final boolean selfMute) {
        this.selfMute = selfMute;
    }

    public boolean isSuppress() {
        return suppress;
    }

    public void setSuppress(final boolean suppress) {
        this.suppress = suppress;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PartialVoiceState that = (PartialVoiceState) o;
        return deaf == that.deaf &&
                mute == that.mute &&
                selfDeaf == that.selfDeaf &&
                selfMute == that.selfMute &&
                suppress == that.suppress &&
                Objects.equal(channelId, that.channelId) &&
                Objects.equal(userId, that.userId) &&
                Objects.equal(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(channelId, userId, sessionId, deaf, mute, selfDeaf, selfMute, suppress);
    }
}
