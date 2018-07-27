package tv.v1x1.modules.channel.wasm.api;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;

import javax.ws.rs.core.MultivaluedMap;

public class HttpResponseEvent extends Event {
    private final int responseCode;
    private final MultivaluedMap<String, Object> headers;
    private final byte[] body;
    private final byte[] eventPayload;

    public HttpResponseEvent(final Module from, final int responseCode, final MultivaluedMap<String, Object> headers, final byte[] body, final byte[] eventPayload) {
        super(from);
        this.responseCode = responseCode;
        this.headers = headers;
        this.body = body;
        this.eventPayload = eventPayload;
    }

    public HttpResponseEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final int responseCode, final MultivaluedMap<String, Object> headers, final byte[] body, final byte[] eventPayload) {
        super(from, messageId, timestamp, context);
        this.responseCode = responseCode;
        this.headers = headers;
        this.body = body;
        this.eventPayload = eventPayload;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] getEventPayload() {
        return eventPayload;
    }
}
