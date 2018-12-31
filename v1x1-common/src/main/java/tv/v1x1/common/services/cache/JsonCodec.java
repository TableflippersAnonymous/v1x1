package tv.v1x1.common.services.cache;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Created by cobi on 1/21/2017.
 */
public class JsonCodec<T> implements CodecCache.Codec<T> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
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
            final byte[] bytes = mapper.writeValueAsBytes(value);
            LOG.debug("JsonCodec encoding: {} to {}", value, bytes);
            return bytes;
        } catch (JsonProcessingException e) {
            throw new JsonCodecException(e);
        }
    }

    @Override
    public T decode(final byte[] value) {
        try {
            final T obj = mapper.readValue(value, clazz);
            LOG.debug("JsonCodec decoding: {} to {}", value, obj);
            return obj;
        } catch (IOException e) {
            throw new JsonCodecException(e);
        }
    }
}
