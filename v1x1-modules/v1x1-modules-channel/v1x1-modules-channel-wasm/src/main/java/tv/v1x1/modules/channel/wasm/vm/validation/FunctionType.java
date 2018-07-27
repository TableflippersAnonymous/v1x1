package tv.v1x1.modules.channel.wasm.vm.validation;

import com.google.common.base.Objects;
import tv.v1x1.modules.channel.wasm.vm.decoder.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.types.I32;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FunctionType {
    private final List<ValType> parameters;
    private final List<ValType> returnTypes;

    public FunctionType(final List<ValType> parameters, final List<ValType> returnTypes) {
        this.parameters = parameters;
        this.returnTypes = returnTypes;
    }

    public static List<FunctionType> decodeVec(final DataInputStream dataInputStream) throws IOException {
        final List<FunctionType> ret = new ArrayList<>();
        final I32 count = I32.decodeU(dataInputStream);
        for(long i = 0; i < count.getValU(); i++)
            ret.add(decode(dataInputStream));
        return ret;
    }

    public static FunctionType decode(final DataInputStream dataInputStream) throws IOException {
        final byte id = dataInputStream.readByte();
        if(id != 0x60)
            throw new DecodeException("Invalid functype magic");
        final List<ValType> parameters = ValType.decodeVec(dataInputStream);
        final List<ValType> returnTypes = ValType.decodeVec(dataInputStream);
        return new FunctionType(parameters, returnTypes);
    }

    // https://webassembly.github.io/spec/core/valid/types.html#function-types
    public void validate() throws ValidationException {
        if(this.returnTypes.size() > 1)
            throw new ValidationException();
    }

    public List<ValType> getParameters() {
        return parameters;
    }

    public List<ValType> getReturnTypes() {
        return returnTypes;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FunctionType that = (FunctionType) o;
        return Objects.equal(parameters, that.parameters) &&
                Objects.equal(returnTypes, that.returnTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(parameters, returnTypes);
    }

    @Override
    public String toString() {
        return "FunctionType{" +
                "parameters=" + parameters +
                ", returnTypes=" + returnTypes +
                '}';
    }
}
