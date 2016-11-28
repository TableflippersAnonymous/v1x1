package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import tv.v1x1.common.dto.db.JoinedTwitchChannel;

/**
 * Created by jcarter on 11/27/16.
 */
public class DAOJoinedTwitchChannel {
    private final Mapper<JoinedTwitchChannel> mapper;
    private final JoinedTwitchChannelAccessor accessor;

    @Accessor
    public interface JoinedTwitchChannelAccessor {
        @Query("SELECT * FROM joined_twitch_channel")
        Result<JoinedTwitchChannel> all();
    }

    public DAOJoinedTwitchChannel(final MappingManager mappingManager) {
        this.mapper = mappingManager.mapper(JoinedTwitchChannel.class);
        this.accessor = mappingManager.createAccessor(JoinedTwitchChannelAccessor.class);
    }

    public JoinedTwitchChannel join(final String channel) {
        final JoinedTwitchChannel joinedTwitchChannel = new JoinedTwitchChannel(channel);
        mapper.save(joinedTwitchChannel);
        return joinedTwitchChannel;
    }

    public JoinedTwitchChannel get(final String channel) {
        return mapper.get(channel);
    }

    public void delete(final String channel) {
        mapper.delete(new JoinedTwitchChannel(channel));
    }

    public Iterable<JoinedTwitchChannel> list() {
        return accessor.all();
    }
}
