package tv.v1x1.modules.core.scheduler.dto;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by cobi on 10/23/2016.
 */
@Table(name = "interval_schedule")
public class IntervalSchedule {
    @PartitionKey
    private UUID id;
    private ByteBuffer module;
    private ByteBuffer payload;
    private long interval;

    public IntervalSchedule() {
    }

    public IntervalSchedule(final UUID id, final byte[] module, final byte[] payload, final long interval) {
        this.id = id;
        this.module = ByteBuffer.wrap(module);
        this.payload = ByteBuffer.wrap(payload);
        this.interval = interval;
    }

    public UUID getId() {
        return id;
    }

    public byte[] getModuleBytes() {
        final byte[] bytes = new byte[module.remaining()];
        module.mark();
        module.get(bytes);
        module.reset();
        return bytes;
    }

    public byte[] getPayloadBytes() {
        final byte[] bytes = new byte[payload.remaining()];
        payload.mark();
        payload.get(bytes);
        payload.reset();
        return bytes;
    }

    public ByteBuffer getModule() {
        return module;
    }

    public ByteBuffer getPayload() {
        return payload;
    }

    public long getInterval() {
        return interval;
    }
}
