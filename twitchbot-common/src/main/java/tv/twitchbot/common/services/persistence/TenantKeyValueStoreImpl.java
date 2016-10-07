package tv.twitchbot.common.services.persistence;


import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.core.Tenant;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by cobi on 10/5/16.
 */
public class TenantKeyValueStoreImpl implements KeyValueStore {
    private Tenant tenant;
    private KeyValueStore keyValueStore;

    public TenantKeyValueStoreImpl(Tenant tenant, KeyValueStore keyValueStore) {
        this.tenant = tenant;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public void put(byte[] key, byte[] value) {
        keyValueStore.put(compositeKey(new byte[][]{tenant.getId().getValue().toString().getBytes(), key}), value);
    }

    @Override
    public byte[] get(byte[] key) {
        return keyValueStore.get(compositeKey(new byte[][]{tenant.getId().getValue().toString().getBytes(), key}));
    }

    @Override
    public void delete(byte[] key) {
        keyValueStore.delete(compositeKey(new byte[][]{tenant.getId().getValue().toString().getBytes(), key}));
    }

    private byte[] compositeKey(byte[][] keys) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(keys.length);
            for (int idx = 0; idx < keys.length; idx++) {
                dos.writeInt(keys[idx].length);
                dos.write(keys[idx]);
            }
            dos.close();
        } catch (IOException ex) {
            /* Shouldn't be possible, but convert to RuntimeException if it does */
            throw new RuntimeException(ex);
        }
        return bos.toByteArray();
    }
}
