package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import tv.v1x1.common.dto.db.ChannelGroupPlatformGroup;
import tv.v1x1.common.dto.db.Platform;

import java.util.Map;
import java.util.stream.StreamSupport;

public class DAOChannelGroupPlatformGroups {
    private final Mapper<ChannelGroupPlatformGroup> mapper;
    private final ChannelGroupPlatformGroupAccessor accessor;

    @Accessor
    public interface ChannelGroupPlatformGroupAccessor {
        @Query("SELECT * FROM channel_group_platform_group WHERE platform = ? AND channel_group_id = ?")
        Result<ChannelGroupPlatformGroup> allByChannelGroup(final Platform platform, final String channelGroupId);
    }

    public DAOChannelGroupPlatformGroups(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(ChannelGroupPlatformGroup.class);
        accessor = mappingManager.createAccessor(ChannelGroupPlatformGroupAccessor.class);
    }

    public Iterable<ChannelGroupPlatformGroup> getAll(final Platform platform, final String channelGroupId) {
        return accessor.allByChannelGroup(platform, channelGroupId);
    }

    public ChannelGroupPlatformGroup get(final Platform platform, final String channelGroupId, final String name) {
        return mapper.get(platform, channelGroupId, name);
    }

    public void put(final ChannelGroupPlatformGroup channelGroupPlatformGroup) {
        mapper.save(channelGroupPlatformGroup);
    }

    public void delete(final Platform platform, final String channelGroupId, final String name) {
        mapper.delete(platform, channelGroupId, name);
    }

    public void putOnly(final Platform platform, final String channelGroupId, final Map<String, String> entries) {
        StreamSupport.stream(getAll(platform, channelGroupId).spliterator(), false)
                .filter(channelGroupPlatformGroup -> !entries.containsKey(channelGroupPlatformGroup.getName()))
                .forEach(channelGroupPlatformGroup -> delete(platform, channelGroupId, channelGroupPlatformGroup.getName()));
        entries.entrySet().stream().map(entry -> new ChannelGroupPlatformGroup(platform, channelGroupId, entry.getKey(), entry.getValue()))
                .forEach(this::put);
    }

    public void deleteAll(final Platform platform, final String channelGroupId) {
        for(final ChannelGroupPlatformGroup channelGroupPlatformGroup : getAll(platform, channelGroupId))
            delete(channelGroupPlatformGroup.getPlatform(), channelGroupPlatformGroup.getChannelGroupId(), channelGroupPlatformGroup.getName());
    }
}
