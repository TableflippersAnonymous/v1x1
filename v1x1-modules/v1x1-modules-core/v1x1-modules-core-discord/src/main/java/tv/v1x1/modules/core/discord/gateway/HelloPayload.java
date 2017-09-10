package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class HelloPayload extends Payload {
    public static class HelloData {
        @JsonProperty("heartbeat_interval")
        private Integer heartbeatInterval;
        @JsonProperty("_trace")
        private List<String> trace;

        public HelloData() {
        }

        public HelloData(final Integer heartbeatInterval, final List<String> trace) {
            this.heartbeatInterval = heartbeatInterval;
            this.trace = trace;
        }

        public Integer getHeartbeatInterval() {
            return heartbeatInterval;
        }

        public void setHeartbeatInterval(final Integer heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
        }

        public List<String> getTrace() {
            return trace;
        }

        public void setTrace(final List<String> trace) {
            this.trace = trace;
        }
    }

    @JsonProperty("d")
    private HelloData helloData;

    public HelloPayload() {
    }

    public HelloPayload(final HelloData helloData) {
        super(10);
        this.helloData = helloData;
    }

    public HelloData getHelloData() {
        return helloData;
    }

    public void setHelloData(final HelloData helloData) {
        this.helloData = helloData;
    }
}
