package tv.v1x1.common.util.data;

import org.codehaus.jackson.util.ByteArrayBuilder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Josh
 */
public class CompositeKey {

    private CompositeKey() {

    }

    public static byte[] makeKey(final byte[][] keys) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(keys.length);
            for (int idx = 0; idx < keys.length; idx++) {
                dos.writeInt(keys[idx].length);
                dos.write(keys[idx]);
            }
            dos.close();
        } catch (final IOException ex) {
            /* Shouldn't be possible, but convert to RuntimeException if it does */
            throw new RuntimeException(ex);
        }
        return bos.toByteArray();
    }

    public static byte[] makeKey(String... keys) {
        byte[][] byteKeys = new byte[keys.length][];
        for(int i = 0; i < byteKeys.length; ++i) {
            byteKeys[i] = keys[i].getBytes();
        }
        return makeKey(byteKeys);
    }
}
