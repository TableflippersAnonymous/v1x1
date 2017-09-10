package tv.v1x1.modules.core.discord.gateway.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.modules.core.discord.gateway.DispatchPayload;

import java.util.List;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class ResumedEvent extends DispatchPayload {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("_trace")
        private List<String> trace;

        public Data() {
        }

        public Data(final List<String> trace) {
            this.trace = trace;
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

    public ResumedEvent() {
    }

    public ResumedEvent(final Long sequenceNumber, final Data data) {
        super(sequenceNumber, "RESUMED");
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(final Data data) {
        this.data = data;
    }
}
