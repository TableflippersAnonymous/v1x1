package tv.v1x1.modules.core.scheduler.dto;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Set;
import java.util.UUID;

/**
 * Created by cobi on 10/23/2016.
 */
@Table(name = "cron_schedule")
public class CronSchedule {
    @PartitionKey
    private UUID id;
    private byte[] module;
    private byte[] payload;
    private Set<Integer> minute;
    private Set<Integer> hour;
    @Column(name = "day_of_month")
    private Set<Integer> dayOfMonth;
    private Set<Integer> month;
    @Column(name = "day_of_week")
    private Set<Integer> dayOfWeek;

    public CronSchedule(final UUID id, final byte[] module, final byte[] payload, final Set<Integer> minute, final Set<Integer> hour,
                        final Set<Integer> dayOfMonth, final Set<Integer> month, final Set<Integer> dayOfWeek) {
        this.id = id;
        this.module = module;
        this.payload = payload;
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
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

    public Set<Integer> getMinute() {
        return minute;
    }

    public Set<Integer> getHour() {
        return hour;
    }

    public Set<Integer> getDayOfMonth() {
        return dayOfMonth;
    }

    public Set<Integer> getMonth() {
        return month;
    }

    public Set<Integer> getDayOfWeek() {
        return dayOfWeek;
    }
}
