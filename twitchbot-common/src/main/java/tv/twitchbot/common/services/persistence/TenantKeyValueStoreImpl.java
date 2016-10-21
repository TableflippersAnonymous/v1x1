package tv.twitchbot.common.services.persistence;


import tv.twitchbot.common.dto.core.Tenant;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by naomi on 10/5/16.
 */
public class TenantKeyValueStoreImpl implements KeyValueStore {
    private final Tenant tenant;
    private final KeyValueStore keyValueStore;

    public TenantKeyValueStoreImpl(final Tenant tenant, final KeyValueStore keyValueStore) {
        this.tenant = tenant;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public void put(final byte[] key, final byte[] value) {
        keyValueStore.put(compositeKey(new byte[][]{tenant.getId().getValue().toString().getBytes(), key}), value);
    }

    @Override
    public byte[] get(final byte[] key) {
        return keyValueStore.get(compositeKey(new byte[][]{tenant.getId().getValue().toString().getBytes(), key}));
    }

    @Override
    public void delete(final byte[] key) {
        keyValueStore.delete(compositeKey(new byte[][]{tenant.getId().getValue().toString().getBytes(), key}));
    }

    private byte[] compositeKey(final byte[][] keys) {
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
