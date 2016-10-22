package tv.v1x1.common.services.persistence;

/**
 * Created by cobi on 10/5/16.
 */
public interface KeyValueStore {
    void put(byte[] key, byte[] value);
    byte[] get(byte[] key);
    void delete(byte[] key);
}
