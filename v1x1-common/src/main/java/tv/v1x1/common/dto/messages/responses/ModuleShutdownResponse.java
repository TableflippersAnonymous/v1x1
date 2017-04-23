package tv.v1x1.common.dto.messages.responses;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.dto.messages.requests.ModuleShutdownRequest;
import tv.v1x1.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by naomi on 10/5/16.
 */
public class ModuleShutdownResponse extends Response<ModuleShutdownRequest> {
    public static ModuleShutdownResponse fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final UUID requestMessageId, final RequestOuterClass.ModuleShutdownResponse extension) {
        return new ModuleShutdownResponse(module, uuid, timestamp, context, requestMessageId);
    }

    public ModuleShutdownResponse(final Module from, final UUID requestMessageId) {
        super(from, requestMessageId);
    }

    public ModuleShutdownResponse(final Module from, final UUID messageId, final long timestamp, final Context context, final UUID requestMessageId) {
        super(from, messageId, timestamp, context, requestMessageId);
    }

    @Override
    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return super.toProtoResponse()
                .setType(RequestOuterClass.RequestType.MODULE_SHUTDOWN)
                .setExtension(RequestOuterClass.ModuleShutdownResponse.data, RequestOuterClass.ModuleShutdownResponse.newBuilder().build());
    }
}
