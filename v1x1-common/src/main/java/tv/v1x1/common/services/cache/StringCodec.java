package tv.v1x1.common.services.cache;

/**
 * Created by jcarter on 2/24/17.
 */
public class StringCodec implements CodecCache.Codec<String> {
    public static StringCodec INSTANCE = new StringCodec();

    @Override
    public byte[] encode(final String value) {
        return value.getBytes();
    }

    @Override
    public String decode(final byte[] value) {
        return new String(value);
    }
}
