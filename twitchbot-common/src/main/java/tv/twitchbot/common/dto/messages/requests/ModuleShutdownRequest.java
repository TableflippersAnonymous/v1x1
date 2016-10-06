package tv.twitchbot.common.dto.messages.requests;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.messages.Request;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class ModuleShutdownRequest extends Request {
    public static ModuleShutdownRequest fromProto(Module module, String responseQueueName, RequestOuterClass.ModuleShutdownRequest moduleShutdownRequest) {
        return new ModuleShutdownRequest(module, responseQueueName);
    }

    public ModuleShutdownRequest(Module module, String responseQueueName) {
        super(module, responseQueueName);
    }

    @Override
    protected RequestOuterClass.Request.Builder toProtoRequest() {
        return super.toProtoRequest()
                .setType(RequestOuterClass.RequestType.MODULE_SHUTDOWN)
                .setExtension(RequestOuterClass.ModuleShutdownRequest.data, RequestOuterClass.ModuleShutdownRequest.newBuilder().build());
    }
}
