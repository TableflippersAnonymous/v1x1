package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.nio.ByteBuffer;

/**
 * Created by cobi on 10/17/2016.
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

    public synchronized byte[] getKey() {
        final byte[] ret = new byte[key.remaining()];
        key.mark();
        key.get(ret);
        key.reset();
        return ret;
    }

    public synchronized byte[] getValue() {
        final byte[] ret = new byte[value.remaining()];
        value.mark();
        value.get(ret);
        value.reset();
        return ret;
    }
}
