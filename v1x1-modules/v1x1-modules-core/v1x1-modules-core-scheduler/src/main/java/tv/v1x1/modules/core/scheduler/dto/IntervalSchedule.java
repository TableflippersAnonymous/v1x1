package tv.v1x1.modules.core.scheduler.dto;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

/**
 * Created by naomi on 10/23/2016.
 */
@Table(name = "interval_schedule")
public class IntervalSchedule {
    @PartitionKey
    private UUID id;
    private byte[] module;
    private byte[] payload;
    private long interval;

    public IntervalSchedule(final UUID id, final byte[] module, final byte[] payload, final long interval) {
        this.id = id;
        this.module = module;
        this.payload = payload;
        this.interval = interval;
    }

    public UUID getId() {
        return id;
    }

    public byte[] getModule() {
        return module;
    }

    public byte[] getPayload() {
        return payload;
    }

    public long getInterval() {
        return interval;
    }
}
