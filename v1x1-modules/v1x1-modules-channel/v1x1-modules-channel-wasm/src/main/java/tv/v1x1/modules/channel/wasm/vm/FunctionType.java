package tv.v1x1.modules.channel.wasm.vm;

import java.util.List;

public class FunctionType {
    private final List<ValType> parameters;
    private final List<ValType> returnTypes;

    public FunctionType(final List<ValType> parameters, final List<ValType> returnTypes) {
        this.parameters = parameters;
        this.returnTypes = returnTypes;
    }

    // https://webassembly.github.io/spec/core/valid/types.html#function-types
    public boolean validate() {
        return this.returnTypes.size() <= 1;
    }

    public List<ValType> getParameters() {
        return parameters;
    }

    public List<ValType> getReturnTypes() {
        return returnTypes;
    }
}
