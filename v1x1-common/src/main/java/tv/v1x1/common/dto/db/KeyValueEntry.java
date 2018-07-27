package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.nio.ByteBuffer;

/**
 * Created by naomi on 10/17/2016.
 */
@Table(name = "key_value_entry")
public class KeyValueEntry {
    @PartitionKey(0)
    private String name;
    @PartitionKey(1)
    private ByteBuffer key;
    private ByteBuffer value;

    public KeyValueEntry() {
    }

    public KeyValueEntry(final String name, final byte[] key, final byte[] value) {
        this.name = name;
        this.key = ByteBuffer.wrap(key);
        this.value = ByteBuffer.wrap(value);
    }

    public String getName() {
        return name;
    }

    public synchronized byte[] keyAsByteArray() {
        return bufferAsByteArray(key);
    }

    public synchronized byte[] valueAsByteArray() {
        return bufferAsByteArray(value);
    }

    private synchronized byte[] bufferAsByteArray(final ByteBuffer buffer) {
        final byte[] ret = new byte[buffer.remaining()];
        buffer.mark();
        buffer.get(ret);
        buffer.reset();
        return ret;
    }
}
