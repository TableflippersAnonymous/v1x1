package tv.v1x1.common.util.data;

import java.io.ByteArrayInputStream;
import org.codehaus.jackson.util.ByteArrayBuilder;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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

    public static byte[][] getKeys(final byte[] key) {
        final ByteArrayInputStream bis = new ByteArrayInputStream(key);
        final DataInputStream dis = new DataInputStream(bis);
        try {
            final int arrayLength = dis.readInt();
            final byte[][] ret = new byte[arrayLength][];
            for (int idx = 0; idx < arrayLength; idx++) {
                final int length = dis.readInt();
                ret[idx] = new byte[length];
                final int readLength = dis.read(ret[idx]);
                if (length != readLength)
                    throw new IllegalArgumentException("Reached end of key while trying to read " + length + " bytes.  Actually read: " + readLength);
            }
            return ret;
        } catch (final IOException ex) {
            /* Shouldn't be possible, but convert to RuntimeException if it does */
            throw new RuntimeException(ex);
        }
    }
}
