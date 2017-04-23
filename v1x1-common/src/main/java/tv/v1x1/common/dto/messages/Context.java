package tv.v1x1.common.dto.messages;

import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.proto.messages.MessageOuterClass;

/**
 * Created by naomi on 4/15/2017.
 */
public class Context {
    public static Context fromProto(final MessageOuterClass.Message.Context proto)  {
        final UUID contextId = UUID.fromProto(proto.getContextId());
        final UUID traceId = proto.hasTraceId() ? UUID.fromProto(proto.getTraceId()) : null;
        final Long parentSpanId = proto.hasParentSpanId() ? proto.getParentSpanId() : null;
        final Long spanId = proto.hasSpanId() ? proto.getSpanId() : null;
        final boolean sampled = proto.hasSampled() && proto.getSampled();
        return new Context(contextId, traceId, parentSpanId, spanId, sampled);
    }

    private final UUID contextId;
    private final UUID traceId;
    private final Long parentSpanId;
    private final Long spanId;
    private final boolean sampled;

    public Context(final UUID contextId) {
        this(contextId, null, null, null, false);
    }

    public Context(final UUID contextId, final UUID traceId, final Long parentSpanId, final Long spanId, final boolean sampled) {
        this.contextId = contextId;
        this.traceId = traceId;
        this.parentSpanId = parentSpanId;
        this.spanId = spanId;
        this.sampled = sampled;
    }

    public UUID getContextId() {
        return contextId;
    }

    public UUID getTraceId() {
        return traceId;
    }

    public Long getParentSpanId() {
        return parentSpanId;
    }

    public Long getSpanId() {
        return spanId;
    }

    public boolean isSampled() {
        return sampled;
    }

    public MessageOuterClass.Message.Context toProto() {
        final MessageOuterClass.Message.Context.Builder builder = MessageOuterClass.Message.Context.newBuilder()
                .setContextId(contextId.toProto());
        if(traceId != null)
            builder.setTraceId(traceId.toProto());
        if(parentSpanId != null)
            builder.setParentSpanId(parentSpanId);
        if(spanId != null)
            builder.setSpanId(spanId);
        return builder.setSampled(sampled).build();
    }
}
