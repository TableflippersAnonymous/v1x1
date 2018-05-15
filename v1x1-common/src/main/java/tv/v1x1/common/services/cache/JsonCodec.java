package tv.v1x1.common.services.cache;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by cobi on 1/21/2017.
 */
public class JsonCodec<T> implements CodecCache.Codec<T> {
    private final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    private final Class<T> clazz;

    public static class JsonCodecException extends RuntimeException {
        public JsonCodecException(final Throwable cause) {
            super(cause);
        }
    }

    public JsonCodec(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] encode(final T value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new JsonCodecException(e);
        }
    }

    @Override
    public T decode(final byte[] value) {
        try {
            return mapper.readValue(value, clazz);
        } catch (IOException e) {
            throw new JsonCodecException(e);
        }
    }
}
