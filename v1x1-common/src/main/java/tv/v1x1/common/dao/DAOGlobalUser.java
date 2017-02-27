package tv.v1x1.common.dao;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.api.RedissonClient;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.InverseGlobalUser;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.util.data.CompositeKey;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by cobi on 10/16/2016.
 */
@Singleton
public class DAOGlobalUser {
    private final Deduplicator createDeduplicator;
    private final Session session;
    private final Mapper<GlobalUser> globalUserMapper;
    private final Mapper<InverseGlobalUser> inverseGlobalUserMapper;
    private final DisplayNameService displayNameService;

    @Inject
    public DAOGlobalUser(final RedissonClient redissonClient, final MappingManager mappingManager, final DisplayNameService displayNameService) {
        session = mappingManager.getSession();
        globalUserMapper = mappingManager.mapper(GlobalUser.class);
        inverseGlobalUserMapper = mappingManager.mapper(InverseGlobalUser.class);
        createDeduplicator = new Deduplicator(redissonClient, "Common|DAOGlobalUser");
        this.displayNameService = displayNameService;
    }

    public GlobalUser getById(final UUID id) {
        return globalUserMapper.get(id);
    }

    public InverseGlobalUser getUser(final Platform platform, final String userId) {
        return inverseGlobalUserMapper.get(platform, userId);
    }

    public GlobalUser getByUser(final Platform platform, final String userId) {
        return getByUser(getUser(platform, userId));
    }

    public GlobalUser getByUser(final InverseGlobalUser inverseGlobalUser) {
        if (inverseGlobalUser == null)
            return null;
        return getById(inverseGlobalUser.getGlobalUserId());
    }

    public GlobalUser getOrCreate(final Platform platform, final String userId, final String displayName) {
        final InverseGlobalUser inverseGlobalUser = getUser(platform, userId);
        if (inverseGlobalUser == null)
            return createGlobalUser(platform, userId, displayName);
        final GlobalUser globalUser = getById(inverseGlobalUser.getGlobalUserId());
        if (globalUser == null)
            throw new IllegalStateException("GlobalUser null but InverseGlobalUser for: " + inverseGlobalUser.getGlobalUserId().toString() + " " + inverseGlobalUser.getUserId() + " " + inverseGlobalUser.getPlatform());
        return globalUser;
    }

    public GlobalUser createGlobalUser(final Platform platform, final String userId, final String displayName) {
        if(createDeduplicator.seenAndAdd(new tv.v1x1.common.dto.core.UUID(UUID.nameUUIDFromBytes(CompositeKey.makeKey(platform.name(), userId))))) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getOrCreate(platform, userId, displayName);
        }
        final GlobalUser globalUser = new GlobalUser(UUID.randomUUID(), new ArrayList<>());
        globalUser.getEntries().add(new GlobalUser.Entry(
                platform,
                (displayName == null && platform == Platform.TWITCH)
                        ? displayNameService.getDisplayNameFromId(new TwitchChannel(null, null, null), userId)
                        : displayName,
                userId));
        final InverseGlobalUser inverseGlobalUser = new InverseGlobalUser(platform, userId, globalUser.getId());
        final BatchStatement b = new BatchStatement();
        b.add(globalUserMapper.saveQuery(globalUser));
        b.add(inverseGlobalUserMapper.saveQuery(inverseGlobalUser));
        session.execute(b);
        return globalUser;
    }

    public GlobalUser addUser(final GlobalUser globalUser, final Platform platform, final String userId, final String displayName) {
        globalUser.getEntries().add(new GlobalUser.Entry(platform, displayName, userId));
        final InverseGlobalUser inverseGlobalUser = new InverseGlobalUser(platform, userId, globalUser.getId());
        final BatchStatement b = new BatchStatement();
        b.add(globalUserMapper.saveQuery(globalUser));
        b.add(inverseGlobalUserMapper.saveQuery(inverseGlobalUser));
        session.execute(b);
        return globalUser;
    }

    public GlobalUser removeUser(final GlobalUser globalUser, final Platform platform, final String userId) {
        final BatchStatement b = new BatchStatement();
        if (globalUser.getEntries().removeIf(entry -> entry.getPlatform() == platform && entry.getUserId().equals(userId)))
            b.add(globalUserMapper.saveQuery(globalUser));
        final InverseGlobalUser inverseGlobalUser = getUser(platform, userId);
        if (inverseGlobalUser != null)
            b.add(inverseGlobalUserMapper.deleteQuery(inverseGlobalUser.getPlatform(), inverseGlobalUser.getUserId()));
        if (b.size() > 0)
            session.execute(b);
        return globalUser;
    }

    public void delete(final GlobalUser globalUser) {
        final BatchStatement b = new BatchStatement();
        b.add(globalUserMapper.deleteQuery(globalUser.getId()));
        for (final GlobalUser.Entry entry : globalUser.getEntries()) {
            final InverseGlobalUser inverseGlobalUser = getUser(entry.getPlatform(), entry.getUserId());
            if (inverseGlobalUser != null)
                b.add(inverseGlobalUserMapper.deleteQuery(inverseGlobalUser.getPlatform(), inverseGlobalUser.getUserId()));
        }
        session.execute(b);
    }
}
