package tv.v1x1.modules.core.scheduler.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import tv.v1x1.modules.core.scheduler.dto.IntervalSchedule;

/**
 * Created by naomi on 10/23/2016.
 */
public class DAOIntervalSchedule {
    private final Mapper<IntervalSchedule> mapper;
    private final IntervalScheduleAccessor accessor;

    @Accessor
    public interface IntervalScheduleAccessor {
        @Query("SELECT * FROM interval_schedule")
        Result<IntervalSchedule> all();
    }

    public DAOIntervalSchedule(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(IntervalSchedule.class);
        accessor = mappingManager.createAccessor(IntervalScheduleAccessor.class);
    }

    public Iterable<IntervalSchedule> all() {
        return accessor.all();
    }

    public void save(final IntervalSchedule intervalSchedule) {
        mapper.save(intervalSchedule);
    }

    public void delete(final IntervalSchedule intervalSchedule) {
        mapper.delete(intervalSchedule);
    }
}
