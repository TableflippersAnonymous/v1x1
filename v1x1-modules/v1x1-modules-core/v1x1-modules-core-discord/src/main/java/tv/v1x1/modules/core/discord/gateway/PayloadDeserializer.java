package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Created by cobi on 4/13/2018.
 */
public class PayloadDeserializer extends StdDeserializer<Payload> {
    public PayloadDeserializer() {
        super(Payload.class);
    }

    @Override
    public Payload deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = p.getCodec().readTree(p);

        switch(node.get("op").asInt()) {
            case 0: return p.getCodec().treeToValue(node, DispatchPayload.class);
            case 1: return p.getCodec().treeToValue(node, HeartbeatPayload.class);
            case 2: return p.getCodec().treeToValue(node, IdentifyPayload.class);
            case 3: return p.getCodec().treeToValue(node, StatusUpdatePayload.class);
            case 4: return p.getCodec().treeToValue(node, VoiceStateUpdatePayload.class);
            /* opcode 5 is Voice Server Ping; sent to the gateway; not to be used */
            case 6: return p.getCodec().treeToValue(node, ResumePayload.class);
            case 7: return p.getCodec().treeToValue(node, ReconnectPayload.class);
            case 8: return p.getCodec().treeToValue(node, RequestGuildMembersPayload.class);
            case 9: return p.getCodec().treeToValue(node, InvalidSessionPayload.class);
            case 10: return p.getCodec().treeToValue(node, HelloPayload.class);
            case 11: return p.getCodec().treeToValue(node, HeartbeatAckPayload.class);
            default: throw new RuntimeException("Unknown opcode " + node.get("op").asInt());
        }
    }
}
