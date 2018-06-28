package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

public class DataSegmentDef {
    private I32 data;
    private Expression offset;
    private byte[] init;
}
