package tv.v1x1.modules.channel.wasm.vm.decoder;

import tv.v1x1.modules.channel.wasm.vm.Context;
import tv.v1x1.modules.channel.wasm.vm.DecodeException;
import tv.v1x1.modules.channel.wasm.vm.validation.ValidationException;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class ExportDescriptor {
    public static ExportDescriptor decode(final DataInputStream dataInputStream) throws IOException {
        final byte type = dataInputStream.readByte();
        switch(type) {
            case 0x00: /* typeidx */
                return FuncExportDescriptor.decode(dataInputStream);
            case 0x01: /* tableidx */
                return TableExportDescriptor.decode(dataInputStream);
            case 0x02: /* memidx */
                return MemExportDescriptor.decode(dataInputStream);
            case 0x03: /* globalidx */
                return GlobalExportDescriptor.decode(dataInputStream);
            default:
                throw new DecodeException("Invalid exportdesc type");
        }
    }

    public abstract void validate(final Context context) throws ValidationException;
}
