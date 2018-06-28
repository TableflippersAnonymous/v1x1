package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.util.List;

public class ElementSegmentDef {
    private I32 table;
    private Expression offset;
    private List<I32> init;
}
