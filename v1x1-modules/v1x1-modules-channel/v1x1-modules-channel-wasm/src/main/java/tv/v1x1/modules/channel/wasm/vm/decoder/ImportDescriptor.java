package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class ImportDescriptor {
    public static ImportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final byte type = dataInputStream.readByte();
        switch(type) {
            case 0x00: /* typeidx */
                return FuncImportDescriptor.decode(dataInputStream);
            case 0x01: /* tabletype */
                return TableImportDescriptor.decode(dataInputStream);
            case 0x02: /* memtype */
                return MemImportDescriptor.decode(dataInputStream);
            case 0x03: /* globaltype */
                return GlobalImportDescriptor.decode(dataInputStream);
            default:
                throw new DecodeException("Invalid importdesc type");
        }
    }

    public abstract void validate(final Context context) throws ValidationException;
}
