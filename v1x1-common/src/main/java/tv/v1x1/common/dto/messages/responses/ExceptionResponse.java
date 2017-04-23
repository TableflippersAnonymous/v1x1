package tv.v1x1.common.dto.messages.responses;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Request;
import tv.v1x1.common.dto.messages.Response;
import tv.v1x1.common.dto.proto.messages.RequestOuterClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cobi on 11/6/2016.
 */
public class ExceptionResponse extends Response<Request> {
    public static ExceptionResponse fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final UUID requestMessageId, final RequestOuterClass.ExceptionResponse extension) {
        return new ExceptionResponse(module, uuid, timestamp, context, requestMessageId, extension.getExceptionClass(), extension.getExceptionMessage(), extension.getExceptionStackTraceList());
    }

    private final String exceptionClass;
    private final String message;
    private final List<String> stackTrace;

    public ExceptionResponse(final Module from, final UUID requestMessageId, final Throwable throwable) {
        super(from, requestMessageId);
        this.exceptionClass = throwable.getClass().getCanonicalName();
        this.message = throwable.getMessage();
        this.stackTrace = Arrays.asList(throwable.getStackTrace()).stream().map(StackTraceElement::toString).collect(Collectors.toList());
    }

    public ExceptionResponse(final Module from, final UUID requestMessageId, final String exceptionClass, final String message, final List<String> stackTrace) {
        super(from, requestMessageId);
        this.exceptionClass = exceptionClass;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public ExceptionResponse(final Module from, final UUID messageId, final long timestamp, final Context context, final UUID requestMessageId, final String exceptionClass, final String message, final List<String> stackTrace) {
        super(from, messageId, timestamp, context, requestMessageId);
        this.exceptionClass = exceptionClass;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getStackTrace() {
        return stackTrace;
    }

    @Override
    protected RequestOuterClass.Response.Builder toProtoResponse() {
        return super.toProtoResponse()
                .setType(RequestOuterClass.RequestType.EXCEPTION)
                .setExtension(RequestOuterClass.ExceptionResponse.data,
                        RequestOuterClass.ExceptionResponse.newBuilder()
                            .setExceptionClass(exceptionClass)
                            .setExceptionMessage(message)
                            .addAllExceptionStackTrace(stackTrace)
                            .build()
                );
    }
}
