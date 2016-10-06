package tv.twitchbot.common.dto.messages.responses;

import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.messages.Response;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class ModuleShutdownResponse extends Response {
    public static ModuleShutdownResponse fromProto(Module module, RequestOuterClass.ModuleShutdownResponse extension) {
        return new ModuleShutdownResponse(module);
    }

    public ModuleShutdownResponse(Module module) {
        super(module);
    }

    @Override
    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return super.toProtoResponse()
                .setType(RequestOuterClass.RequestType.MODULE_SHUTDOWN)
                .setExtension(RequestOuterClass.ModuleShutdownResponse.data, RequestOuterClass.ModuleShutdownResponse.newBuilder().build());
    }
}
