package tv.v1x1.common.util.data;

import org.redisson.api.RFuture;
import org.redisson.api.RMapCache;
import org.redisson.client.codec.Codec;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 11/12/2016.
 */
public class FixedRMapCache implements RMapCache<byte[], byte[]> {
    private final RMapCache<byte[], byte[]> mapCache;

    public FixedRMapCache(final RMapCache<byte[], byte[]> mapCache) {
        this.mapCache = mapCache;
    }


    @Override
    public byte[] putIfAbsent(final byte[] key, final byte[] value, final long ttl, final TimeUnit ttlUnit) {
        return mapCache.putIfAbsent(key, value, ttl, ttlUnit);
    }

    @Override
    public byte[] putIfAbsent(final byte[] key, final byte[] value, final long ttl, final TimeUnit ttlUnit, final long maxIdleTime, final TimeUnit maxIdleUnit) {
        return mapCache.putIfAbsent(key, value, ttl, ttlUnit, maxIdleTime, maxIdleUnit);
    }

    @Override
    public byte[] put(final byte[] key, final byte[] value, final long ttl, final TimeUnit unit) {
        return mapCache.put(key, value, ttl, unit);
    }

    @Override
    public byte[] put(final byte[] key, final byte[] value, final long ttl, final TimeUnit ttlUnit, final long maxIdleTime, final TimeUnit maxIdleUnit) {
        return mapCache.put(key, value, ttl, ttlUnit, maxIdleTime, maxIdleUnit);
    }

    @Override
    public boolean fastPut(final byte[] key, final byte[] value, final long ttl, final TimeUnit ttlUnit) {
        return mapCache.fastPut(key, value, ttl, ttlUnit);
    }

    @Override
    public boolean fastPut(final byte[] key, final byte[] value, final long ttl, final TimeUnit ttlUnit, final long maxIdleTime, final TimeUnit maxIdleUnit) {
        return mapCache.fastPut(key, value, ttl, ttlUnit, maxIdleTime, maxIdleUnit);
    }

    @Override
    public int size() {
        return mapCache.size();
    }

    @Override
    public boolean isEmpty() {
        return mapCache.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return mapCache.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return mapCache.containsValue(value);
    }

    @Override
    public byte[] get(final Object key) {
        return mapCache.get(key);
    }

    @Override
    public byte[] put(final byte[] key, final byte[] value) {
        return mapCache.put(key, value);
    }

    @Override
    public byte[] remove(final Object key) {
        //return mapCache.remove(key);
        if(!(key instanceof byte[]))
            return null;
        return put((byte[]) key, null, 1, TimeUnit.NANOSECONDS);
    }

    @Override
    public void putAll(final Map<? extends byte[], ? extends byte[]> m) {
        mapCache.putAll(m);
    }

    @Override
    public void clear() {
        mapCache.clear();
    }

    @Override
    public int valueSize(final byte[] key) {
        return mapCache.valueSize(key);
    }

    @Override
    public byte[] addAndGet(final byte[] key, final Number delta) {
        return mapCache.addAndGet(key, delta);
    }

    @Override
    public Map<byte[], byte[]> getAll(final Set<byte[]> keys) {
        return mapCache.getAll(keys);
    }

    @Override
    public long fastRemove(final byte[]... keys) {
        // return mapCache.fastRemove(keys);
        for(final byte[] key : keys) {
            mapCache.fastPut(key, null, 1, TimeUnit.NANOSECONDS);
        }
        return keys.length;
    }

    @Override
    public boolean fastPut(final byte[] key, final byte[] value) {
        return mapCache.fastPut(key, value);
    }

    @Override
    public boolean fastPutIfAbsent(final byte[] key, final byte[] value) {
        return mapCache.fastPutIfAbsent(key, value);
    }

    @Override
    public Set<byte[]> readAllKeySet() {
        return mapCache.readAllKeySet();
    }

    @Override
    public Collection<byte[]> readAllValues() {
        return mapCache.readAllValues();
    }

    @Override
    public Set<Entry<byte[], byte[]>> readAllEntrySet() {
        return mapCache.readAllEntrySet();
    }

    @Override
    public Set<byte[]> keySet() {
        return mapCache.keySet();
    }

    @Override
    public Collection<byte[]> values() {
        return mapCache.values();
    }

    @Override
    public Set<Entry<byte[], byte[]>> entrySet() {
        return mapCache.entrySet();
    }

    @Override
    public byte[] putIfAbsent(final byte[] key, final byte[] value) {
        return mapCache.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(final Object key, final Object value) {
        return mapCache.remove(key, value);
    }

    @Override
    public boolean replace(final byte[] key, final byte[] oldValue, final byte[] newValue) {
        return mapCache.replace(key, oldValue, newValue);
    }

    @Override
    public byte[] replace(final byte[] key, final byte[] value) {
        return mapCache.replace(key, value);
    }

    @Override
    public boolean expire(final long timeToLive, final TimeUnit timeUnit) {
        return mapCache.expire(timeToLive, timeUnit);
    }

    @Override
    public boolean expireAt(final long timestamp) {
        return mapCache.expireAt(timestamp);
    }

    @Override
    public boolean expireAt(final Date timestamp) {
        return mapCache.expireAt(timestamp);
    }

    @Override
    public boolean clearExpire() {
        return mapCache.clearExpire();
    }

    @Override
    public long remainTimeToLive() {
        return mapCache.remainTimeToLive();
    }

    @Override
    public RFuture<byte[]> putIfAbsentAsync(final byte[] key, final byte[] value, final long ttl, final TimeUnit unit) {
        return mapCache.putIfAbsentAsync(key, value, ttl, unit);
    }

    @Override
    public RFuture<byte[]> putIfAbsentAsync(final byte[] key, final byte[] value, final long ttl, final TimeUnit ttlUnit, final long maxIdleTime, final TimeUnit maxIdleUnit) {
        return mapCache.putIfAbsentAsync(key, value, ttl, ttlUnit, maxIdleTime, maxIdleUnit);
    }

    @Override
    public RFuture<byte[]> putAsync(final byte[] key, final byte[] value, final long ttl, final TimeUnit unit) {
        return mapCache.putAsync(key, value, ttl, unit);
    }

    @Override
    public RFuture<byte[]> putAsync(final byte[] key, final byte[] value, final long ttl, final TimeUnit ttlUnit, final long maxIdleTime, final TimeUnit maxIdleUnit) {
        return mapCache.putAsync(key, value, ttl, ttlUnit, maxIdleTime, maxIdleUnit);
    }

    @Override
    public RFuture<Boolean> fastPutAsync(final byte[] key, final byte[] value, final long ttl, final TimeUnit unit) {
        return mapCache.fastPutAsync(key, value, ttl, unit);
    }

    @Override
    public RFuture<Boolean> fastPutAsync(final byte[] key, final byte[] value, final long ttl, final TimeUnit ttlUnit, final long maxIdleTime, final TimeUnit maxIdleUnit) {
        return mapCache.fastPutAsync(key, value, ttl, ttlUnit, maxIdleTime, maxIdleUnit);
    }

    @Override
    public RFuture<Integer> valueSizeAsync(final byte[] key) {
        return mapCache.valueSizeAsync(key);
    }

    @Override
    public RFuture<Map<byte[], byte[]>> getAllAsync(final Set<byte[]> keys) {
        return mapCache.getAllAsync(keys);
    }

    @Override
    public RFuture<Void> putAllAsync(final Map<? extends byte[], ? extends byte[]> map) {
        return mapCache.putAllAsync(map);
    }

    @Override
    public RFuture<byte[]> addAndGetAsync(final byte[] key, final Number value) {
        return mapCache.addAndGetAsync(key, value);
    }

    @Override
    public RFuture<Boolean> containsValueAsync(final Object value) {
        return mapCache.containsValueAsync(value);
    }

    @Override
    public RFuture<Boolean> containsKeyAsync(final Object key) {
        return mapCache.containsKeyAsync(key);
    }

    @Override
    public RFuture<Integer> sizeAsync() {
        return mapCache.sizeAsync();
    }

    @Override
    public RFuture<Long> fastRemoveAsync(final byte[]... keys) {
        return mapCache.fastRemoveAsync(keys);
    }

    @Override
    public RFuture<Boolean> fastPutAsync(final byte[] key, final byte[] value) {
        return mapCache.fastPutAsync(key, value);
    }

    @Override
    public RFuture<Boolean> fastPutIfAbsentAsync(final byte[] key, final byte[] value) {
        return mapCache.fastPutIfAbsentAsync(key, value);
    }

    @Override
    public RFuture<Set<byte[]>> readAllKeySetAsync() {
        return mapCache.readAllKeySetAsync();
    }

    @Override
    public RFuture<Collection<byte[]>> readAllValuesAsync() {
        return mapCache.readAllValuesAsync();
    }

    @Override
    public RFuture<Set<Entry<byte[], byte[]>>> readAllEntrySetAsync() {
        return mapCache.readAllEntrySetAsync();
    }

    @Override
    public RFuture<byte[]> getAsync(final byte[] key) {
        return mapCache.getAsync(key);
    }

    @Override
    public RFuture<byte[]> putAsync(final byte[] key, final byte[] value) {
        return mapCache.putAsync(key, value);
    }

    @Override
    public RFuture<byte[]> removeAsync(final byte[] key) {
        return mapCache.removeAsync(key);
    }

    @Override
    public RFuture<byte[]> replaceAsync(final byte[] key, final byte[] value) {
        return mapCache.replaceAsync(key, value);
    }

    @Override
    public RFuture<Boolean> replaceAsync(final byte[] key, final byte[] oldValue, final byte[] newValue) {
        return mapCache.replaceAsync(key, oldValue, newValue);
    }

    @Override
    public RFuture<Boolean> removeAsync(final Object key, final Object value) {
        return mapCache.removeAsync(key, value);
    }

    @Override
    public RFuture<byte[]> putIfAbsentAsync(final byte[] key, final byte[] value) {
        return mapCache.putIfAbsentAsync(key, value);
    }

    @Override
    public RFuture<Boolean> expireAsync(final long timeToLive, final TimeUnit timeUnit) {
        return mapCache.expireAsync(timeToLive, timeUnit);
    }

    @Override
    public RFuture<Boolean> expireAtAsync(final Date timestamp) {
        return mapCache.expireAtAsync(timestamp);
    }

    @Override
    public RFuture<Boolean> expireAtAsync(final long timestamp) {
        return mapCache.expireAtAsync(timestamp);
    }

    @Override
    public RFuture<Boolean> clearExpireAsync() {
        return mapCache.clearExpireAsync();
    }

    @Override
    public RFuture<Long> remainTimeToLiveAsync() {
        return mapCache.remainTimeToLiveAsync();
    }

    @Override
    public void migrate(final String host, final int port, final int database) {
        mapCache.migrate(host, port, database);
    }

    @Override
    public boolean move(final int database) {
        return mapCache.move(database);
    }

    @Override
    public String getName() {
        return mapCache.getName();
    }

    @Override
    public boolean delete() {
        return mapCache.delete();
    }

    @Override
    public void rename(final String newName) {
        mapCache.rename(newName);
    }

    @Override
    public boolean renamenx(final String newName) {
        return mapCache.renamenx(newName);
    }

    @Override
    public boolean isExists() {
        return mapCache.isExists();
    }

    @Override
    public Codec getCodec() {
        return mapCache.getCodec();
    }

    @Override
    public RFuture<Void> migrateAsync(final String host, final int port, final int database) {
        return mapCache.migrateAsync(host, port, database);
    }

    @Override
    public RFuture<Boolean> moveAsync(final int database) {
        return mapCache.moveAsync(database);
    }

    @Override
    public RFuture<Boolean> deleteAsync() {
        return mapCache.deleteAsync();
    }

    @Override
    public RFuture<Void> renameAsync(final String newName) {
        return mapCache.renameAsync(newName);
    }

    @Override
    public RFuture<Boolean> renamenxAsync(final String newName) {
        return mapCache.renamenxAsync(newName);
    }

    @Override
    public RFuture<Boolean> isExistsAsync() {
        return mapCache.isExistsAsync();
    }
}
