package tv.v1x1.modules.channel.wasm.vm;

public class TrapException extends Exception {
    public TrapException() {
    }

    public TrapException(final String message) {
        super(message);
    }

    public TrapException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TrapException(final Throwable cause) {
        super(cause);
    }

    public TrapException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
