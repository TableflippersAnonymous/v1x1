package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = PayloadDeserializer.class)
public abstract class Payload {
    @JsonProperty
    private Integer op;

    public Payload() {
    }

    public Payload(final Integer op) {
        this.op = op;
    }

    public Integer getOp() {
        return op;
    }

    public void setOp(final Integer op) {
        this.op = op;
    }
}
