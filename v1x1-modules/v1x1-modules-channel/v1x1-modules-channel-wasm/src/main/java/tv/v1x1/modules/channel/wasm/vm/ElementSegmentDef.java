package tv.v1x1.modules.channel.wasm.vm;

import java.util.List;

public class ElementSegmentDef {
    private TableIdx table;
    private Expression offset;
    private List<FuncIdx> init;
}
