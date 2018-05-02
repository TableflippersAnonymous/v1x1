package tv.v1x1.common.services.slack.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorEvent extends Event {
    public static class ErrorData {
        @JsonProperty
        private Integer code;
        @JsonProperty("msg")
        private String message;

        public ErrorData() {
        }

        public ErrorData(final Integer code, final String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(final Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }
    }

    @JsonProperty
    private ErrorData error;

    public ErrorEvent() {
    }

    public ErrorEvent(final ErrorData error) {
        super("error");
        this.error = error;
    }

    public ErrorData getError() {
        return error;
    }

    public void setError(final ErrorData error) {
        this.error = error;
    }
}
