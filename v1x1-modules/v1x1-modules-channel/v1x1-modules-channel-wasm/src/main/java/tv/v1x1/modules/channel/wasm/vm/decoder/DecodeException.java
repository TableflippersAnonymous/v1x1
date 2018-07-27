package tv.v1x1.modules.channel.wasm.vm.decoder;

import java.io.IOException;

public class DecodeException extends IOException {
    public DecodeException() {
    }

    public DecodeException(final String message) {
        super(message);
    }

    public DecodeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DecodeException(final Throwable cause) {
        super(cause);
    }
}
