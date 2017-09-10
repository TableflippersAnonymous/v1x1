package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class ReconnectPayload extends Payload {
    public ReconnectPayload() {
        super(7);
    }
}
