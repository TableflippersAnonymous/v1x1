package tv.twitchbot.common.dto.messages.requests;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.messages.Request;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class ModuleShutdownRequest extends Request {
    public static ModuleShutdownRequest fromProto(Module module, UUID uuid, long timestamp, String responseQueueName, RequestOuterClass.ModuleShutdownRequest moduleShutdownRequest) {
        return new ModuleShutdownRequest(module, uuid, timestamp, responseQueueName);
    }

    public ModuleShutdownRequest(Module from, String responseQueueName) {
        super(from, responseQueueName);
    }

    public ModuleShutdownRequest(Module from, UUID messageId, long timestamp, String responseQueueName) {
        super(from, messageId, timestamp, responseQueueName);
    }

    @Override
    protected RequestOuterClass.Request.Builder toProtoRequest() {
        return super.toProtoRequest()
                .setType(RequestOuterClass.RequestType.MODULE_SHUTDOWN)
                .setExtension(RequestOuterClass.ModuleShutdownRequest.data, RequestOuterClass.ModuleShutdownRequest.newBuilder().build());
    }
}
