package tv.v1x1.common.util;

public class NoSuchThingException extends Exception {
    NoSuchThingException() {
        super();
    }

    NoSuchThingException(final String message) {
        super(message);
    }
}
