package tv.v1x1.common.services.cache;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by naomi on 1/21/2017.
 */
public class CodecCache<K, V> implements SharedCache<K, V> {
    public interface Codec<T> {
        byte[] encode(final T value);
        T decode(final byte[] value);
    }

    public static final Codec<byte[]> BYTE_CODEC = new Codec<byte[]>() {
        @Override
        public byte[] encode(final byte[] value) {
            return value;
        }

        @Override
        public byte[] decode(final byte[] value) {
            return value;
        }
    };


    private final SharedCache<byte[], byte[]> cache;
    private final Codec<K> keyCodec;
    private final Codec<V> valCodec;

    public CodecCache(final SharedCache<byte[], byte[]> cache, final Codec<K> keyCodec, final Codec<V> valCodec) {
        this.cache = cache;
        this.keyCodec = keyCodec;
        this.valCodec = valCodec;
    }

    @Override
    public V get(final K key) throws ExecutionException {
        return valCodec.decode(cache.get(keyCodec.encode(key)));
    }

    @Override
    public void put(final K key, final V value) {
        cache.put(keyCodec.encode(key), valCodec.encode(value));
    }

    @Override
    public void invalidate(final K key) {
        cache.invalidate(keyCodec.encode(key));
    }

    @Override
    public void close() throws IOException {
        cache.close();
    }
}
