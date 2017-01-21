package tv.v1x1.common.services.cache;

import java.util.function.Function;

/**
 * Created by cobi on 1/21/2017.
 */
public class LambdaCodec<T> implements CodecCache.Codec<T> {
    private final Function<T, byte[]> encode;
    private final Function<byte[], T> decode;

    public LambdaCodec(final Function<T, byte[]> encode, final Function<byte[], T> decode) {
        this.encode = encode;
        this.decode = decode;
    }

    @Override
    public byte[] encode(final T value) {
        return encode.apply(value);
    }

    @Override
    public T decode(final byte[] value) {
        return decode.apply(value);
    }
}
