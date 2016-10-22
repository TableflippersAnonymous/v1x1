package tv.v1x1.common.util.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
}
