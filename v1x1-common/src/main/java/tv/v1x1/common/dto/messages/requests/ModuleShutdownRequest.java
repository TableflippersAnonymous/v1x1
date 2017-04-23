package tv.v1x1.common.dto.messages.requests;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class ModuleShutdownRequest extends Request {
    public static ModuleShutdownRequest fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final String responseQueueName, final RequestOuterClass.ModuleShutdownRequest moduleShutdownRequest) {
        return new ModuleShutdownRequest(module, uuid, timestamp, context, responseQueueName);
    }

    public ModuleShutdownRequest(final Module from, final String responseQueueName) {
        super(from, responseQueueName);
    }

    public ModuleShutdownRequest(final Module from, final UUID messageId, final long timestamp, final Context context, final String responseQueueName) {
        super(from, messageId, timestamp, context, responseQueueName);
    }

    @Override
    protected RequestOuterClass.Request.Builder toProtoRequest() {
        return super.toProtoRequest()
                .setType(RequestOuterClass.RequestType.MODULE_SHUTDOWN)
                .setExtension(RequestOuterClass.ModuleShutdownRequest.data, RequestOuterClass.ModuleShutdownRequest.newBuilder().build());
    }
}
