package tv.v1x1.common.services.slack.exceptions;

public class SlackApiException extends RuntimeException {
    public SlackApiException() {
    }

    public SlackApiException(final String message) {
        super(message);
    }

    public SlackApiException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SlackApiException(final Throwable cause) {
        super(cause);
    }

    public SlackApiException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
