package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class ResumePayload extends Payload {
    public static class ResumeData {
        @JsonProperty
        private String token;
        @JsonProperty("session_id")
        private String sessionId;
        @JsonProperty
        private Long seq;

        public ResumeData() {
        }

        public ResumeData(final String token, final String sessionId, final Long seq) {
            this.token = token;
            this.sessionId = sessionId;
            this.seq = seq;
        }

        public String getToken() {
            return token;
        }

        public void setToken(final String token) {
            this.token = token;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(final String sessionId) {
            this.sessionId = sessionId;
        }

        public Long getSeq() {
            return seq;
        }

        public void setSeq(final Long seq) {
            this.seq = seq;
        }
    }

    @JsonProperty("d")
    private ResumeData resumeData;

    public ResumePayload() {
    }

    public ResumePayload(final ResumeData resumeData) {
        super(6);
        this.resumeData = resumeData;
    }

    public ResumeData getResumeData() {
        return resumeData;
    }

    public void setResumeData(final ResumeData resumeData) {
        this.resumeData = resumeData;
    }
}
