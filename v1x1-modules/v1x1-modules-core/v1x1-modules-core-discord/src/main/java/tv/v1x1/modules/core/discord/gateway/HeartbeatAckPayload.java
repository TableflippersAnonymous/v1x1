package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonDeserialize
public class HeartbeatAckPayload extends Payload {
    public HeartbeatAckPayload() {
        super(11);
    }
}
