package tv.v1x1.modules.channel.wasm.vm;

import java.util.List;

public class FunctionDef {
    private TypeIdx type;
    private List<ValType> locals;
    private Expression body;
}
