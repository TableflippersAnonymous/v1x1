package tv.twitchbot.common.dto.messages.responses;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.dto.messages.requests.ModuleShutdownRequest;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class ModuleShutdownResponse extends Response<ModuleShutdownRequest> {
    public static ModuleShutdownResponse fromProto(Module module, UUID uuid, long timestamp, UUID requestMessageId, RequestOuterClass.ModuleShutdownResponse extension) {
        return new ModuleShutdownResponse(module, uuid, timestamp, requestMessageId);
    }

    public ModuleShutdownResponse(Module from, UUID requestMessageId) {
        super(from, requestMessageId);
    }

    public ModuleShutdownResponse(Module from, UUID messageId, long timestamp, UUID requestMessageId) {
        super(from, messageId, timestamp, requestMessageId);
    }

    @Override
    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return super.toProtoResponse()
                .setType(RequestOuterClass.RequestType.MODULE_SHUTDOWN)
                .setExtension(RequestOuterClass.ModuleShutdownResponse.data, RequestOuterClass.ModuleShutdownResponse.newBuilder().build());
    }
}
