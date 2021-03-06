package tv.v1x1.common.services.queue;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.google.protobuf.InvalidProtocolBufferException;
import org.redisson.api.RBlockingQueue;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Message;
import zipkin.Endpoint;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Cobi
 */
public class MessageQueueImpl implements MessageQueue {

    private final RBlockingQueue<byte[]> blockingQueue;
    private final Tracer tracer;

    public MessageQueueImpl(final RBlockingQueue<byte[]> blockingQueue, final Tracer tracer) {
        this.blockingQueue = blockingQueue;
        this.tracer = tracer;
    }

    @Override
    public Message get() throws Exception {
        return getWithOthers(() -> new String[] {});
    }

    @Override
    public Message getWithOthers(final Callable<String[]> otherQueueNames) throws Exception {
        while (!Thread.interrupted()) {
            final byte[] bytes = blockingQueue.pollFromAny(50, TimeUnit.MILLISECONDS,
                    Arrays.stream(otherQueueNames.call()).map(queue -> "{MQ}" + queue).collect(Collectors.toList())
                            .toArray(new String[] {}));
            if (bytes == null)
                continue;
            final Message message = Message.fromBytes(bytes);
            if(message.getContext() != null && message.getContext().getTraceId() != null) {
                final TraceContext traceContext = TraceContext.newBuilder()
                        .traceId(message.getContext().getTraceId().getValue().getLeastSignificantBits())
                        .traceIdHigh(message.getContext().getTraceId().getValue().getMostSignificantBits())
                        .parentId(message.getContext().getParentSpanId())
                        .spanId(message.getContext().getSpanId())
                        .sampled(message.getContext().isSampled())
                        .build();
                tracer.joinSpan(traceContext)
                        .name(blockingQueue.getName() + "-recv")
                        .kind(Span.Kind.SERVER)
                        .tag("class", message.getClass().getCanonicalName())
                        .start().flush();
            }
            return message;
        }
        throw new InterruptedException();
    }

    @Override
    public void add(Message message) {
        if((message.getContext() == null || message.getContext().getTraceId() == null) && tracer.currentSpan() != null) {
            try {
                message = Message.fromBytes(message.toProto().toByteArray());
            } catch (final InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
            final Span span = tracer.currentSpan();
            message.setContext(new Context(new tv.v1x1.common.dto.core.UUID(UUID.randomUUID()),
                    new tv.v1x1.common.dto.core.UUID(new UUID(span.context().traceIdHigh(), span.context().traceId())),
                    span.context().parentId(),
                    span.context().spanId(),
                    span.context().sampled()));
        }
        if(message.getContext() != null && message.getContext().getTraceId() != null) {
            final Context context = message.getContext();
            try {
                message = Message.fromBytes(message.toProto().toByteArray());
            } catch (final InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
            final Span span = tracer.newChild(TraceContext.newBuilder()
                            .traceId(context.getTraceId().getValue().getLeastSignificantBits())
                            .traceIdHigh(context.getTraceId().getValue().getMostSignificantBits())
                            .parentId(context.getParentSpanId())
                            .spanId(context.getSpanId())
                            .sampled(context.isSampled()).build())
                    .name(blockingQueue.getName() + "-send")
                    .kind(Span.Kind.CLIENT)
                    .tag("class", message.getClass().getCanonicalName())
                    .remoteEndpoint(Endpoint.create(blockingQueue.getName(), 0));
            message.setContext(new Context(context.getContextId(),
                    new tv.v1x1.common.dto.core.UUID(new UUID(span.context().traceIdHigh(), span.context().traceId())),
                    span.context().parentId(),
                    span.context().spanId(),
                    span.context().sampled()));
            span.start().flush();
        }
        blockingQueue.add(message.toBytes());
    }
}
