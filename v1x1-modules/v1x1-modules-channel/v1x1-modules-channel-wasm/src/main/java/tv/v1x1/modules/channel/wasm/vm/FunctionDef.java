package tv.v1x1.modules.channel.wasm.vm;

import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.util.List;

public class FunctionDef {
    private I32 type;
    private List<ValType> locals;
    private Expression body;
}
