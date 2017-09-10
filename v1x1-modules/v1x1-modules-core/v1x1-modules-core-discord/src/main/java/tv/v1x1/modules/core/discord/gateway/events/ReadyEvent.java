package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.channel.Channel;
import tv.v1x1.common.services.discord.dto.guild.UnavailableGuild;
import tv.v1x1.common.services.discord.dto.user.User;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

import java.util.List;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class ReadyEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("v")
        private Integer version;
        @JsonProperty
        private User user;
        @JsonProperty("private_channels")
        private List<Channel> privateChannels;
        @JsonProperty
        private List<UnavailableGuild> guilds;
        @JsonProperty("session_id")
        private String sessionId;
        @JsonProperty("_trace")
        private List<String> trace;

        public Data() {
        }

        public Data(final Integer version, final User user, final List<Channel> privateChannels,
                    final List<UnavailableGuild> guilds, final String sessionId, final List<String> trace) {
            this.version = version;
            this.user = user;
            this.privateChannels = privateChannels;
            this.guilds = guilds;
            this.sessionId = sessionId;
            this.trace = trace;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(final Integer version) {
            this.version = version;
        }

        public User getUser() {
            return user;
        }

        public void setUser(final User user) {
            this.user = user;
        }

        public List<Channel> getPrivateChannels() {
            return privateChannels;
        }

        public void setPrivateChannels(final List<Channel> privateChannels) {
            this.privateChannels = privateChannels;
        }

        public List<UnavailableGuild> getGuilds() {
            return guilds;
        }

        public void setGuilds(final List<UnavailableGuild> guilds) {
            this.guilds = guilds;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(final String sessionId) {
            this.sessionId = sessionId;
        }

        public List<String> getTrace() {
            return trace;
        }

        public void setTrace(final List<String> trace) {
            this.trace = trace;
        }
    }

    @JsonProperty("d")
    private Data data;

    public ReadyEvent() {
    }

    public ReadyEvent(final Long sequenceNumber, final Data data) {
        super(sequenceNumber, "READY");
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(final Data data) {
        this.data = data;
    }
}
