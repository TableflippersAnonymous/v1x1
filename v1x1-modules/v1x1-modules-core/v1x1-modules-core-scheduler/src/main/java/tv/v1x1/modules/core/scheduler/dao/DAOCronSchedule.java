package tv.v1x1.modules.core.scheduler.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import tv.v1x1.modules.core.scheduler.dto.CronSchedule;

/**
 * Created by cobi on 10/23/2016.
 */
public class DAOCronSchedule {
    private final Mapper<CronSchedule> mapper;
    private final CronScheduleAccessor accessor;

    @Accessor
    public interface CronScheduleAccessor {
        @Query("SELECT * FROM cron_schedule")
        Result<CronSchedule> all();
    }

    public DAOCronSchedule(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(CronSchedule.class);
        accessor = mappingManager.createAccessor(CronScheduleAccessor.class);
    }

    public Iterable<CronSchedule> all() {
        return accessor.all();
    }

    public void save(final CronSchedule cronSchedule) {
        mapper.save(cronSchedule);
    }

    public void delete(final CronSchedule cronSchedule) {
        mapper.delete(cronSchedule);
    }
}
