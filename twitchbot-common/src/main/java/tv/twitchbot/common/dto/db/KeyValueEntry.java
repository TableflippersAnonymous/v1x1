package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Created by naomi on 10/17/2016.
 */
@Table(name = "key_value_entry")
public class KeyValueEntry {
    @PartitionKey(0)
    private String name;
    @PartitionKey(1)
    private byte[] key;
    private byte[] value;

    public KeyValueEntry(String name, byte[] key, byte[] value) {
        this.name = name;
        this.key = key;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
