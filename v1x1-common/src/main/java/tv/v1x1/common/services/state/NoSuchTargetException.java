package tv.v1x1.common.services.state;

/**
 * Created by naomi on 3/4/2017.
 */
public class NoSuchTargetException extends Exception {

    NoSuchTargetException() {
        super();
    }

    NoSuchTargetException(final String message) {
        super(message);
    }
}
