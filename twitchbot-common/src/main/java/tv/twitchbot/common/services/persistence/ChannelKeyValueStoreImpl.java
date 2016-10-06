package tv.twitchbot.common.services.persistence;


import tv.twitchbot.common.dto.core.Channel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by naomi on 10/5/16.
 */
public class ChannelKeyValueStoreImpl implements KeyValueStore {
    private Channel channel;
    private KeyValueStore keyValueStore;

    public ChannelKeyValueStoreImpl(Channel channel, KeyValueStore keyValueStore) {
        this.channel = channel;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public void put(byte[] key, byte[] value) {
        keyValueStore.put(compositeKey(new byte[][]{channel.getClass().getCanonicalName().getBytes(), channel.getName().getBytes(), key}), value);
    }

    @Override
    public byte[] get(byte[] key) {
        return keyValueStore.get(compositeKey(new byte[][]{channel.getClass().getCanonicalName().getBytes(), channel.getName().getBytes(), key}));
    }

    @Override
    public void delete(byte[] key) {
        keyValueStore.delete(compositeKey(new byte[][]{channel.getClass().getCanonicalName().getBytes(), channel.getName().getBytes(), key}));
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
