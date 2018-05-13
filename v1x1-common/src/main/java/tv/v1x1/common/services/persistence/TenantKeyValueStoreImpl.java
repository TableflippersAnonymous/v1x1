package tv.v1x1.common.services.persistence;


import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.util.data.CompositeKey;

/**
 * Created by cobi on 10/5/16.
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
        keyValueStore.put(CompositeKey.makeKey(tenant.getId().getValue().toString().getBytes(), key), value);
    }

    @Override
    public byte[] get(final byte[] key) {
        return keyValueStore.get(CompositeKey.makeKey(tenant.getId().getValue().toString().getBytes(), key));
    }

    @Override
    public void delete(final byte[] key) {
        keyValueStore.delete(CompositeKey.makeKey(tenant.getId().getValue().toString().getBytes(), key));
    }
}
