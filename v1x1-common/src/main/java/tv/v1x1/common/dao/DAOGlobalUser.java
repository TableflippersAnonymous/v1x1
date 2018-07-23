package tv.v1x1.common.dao;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.redisson.api.RedissonClient;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.InverseGlobalUser;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.services.persistence.Deduplicator;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.common.util.data.CompositeKey;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
        final GlobalUser globalUser = globalUserMapper.get(id);
        if(globalUser == null)
            return null;
        final int length = globalUser.getEntries().size();
        final Set<GlobalUser.Entry> set = new HashSet<>();
        for(final GlobalUser.Entry entry : ImmutableList.copyOf(globalUser.getEntries()))
            if(!set.add(entry))
                globalUser.getEntries().remove(entry);
        if(globalUser.getEntries().size() != length)
            globalUserMapper.save(globalUser);
        return globalUser;
    }

    public InverseGlobalUser getUser(final Platform platform, final String userId) {
        return inverseGlobalUserMapper.get(platform, userId);
    }

    public GlobalUser getByUser(final Platform platform, final String userId) {
        return getByUser(getUser(platform, userId));
    }

    /**
     * Shortcut to get a {@link tv.v1x1.common.dto.core.GlobalUser}
     * @param platform
     * @param userId
     * @return a core GlobalUser
     * @throws NoSuchUserException when a GlobalUser cannot be found
     */
    public tv.v1x1.common.dto.core.GlobalUser getByUserAsCore(final Platform platform, final String userId) throws NoSuchUserException {
        final GlobalUser globalUser = getByUser(platform, userId);
        if(globalUser != null)
            return globalUser.toCore();
        throw new NoSuchUserException("GlobalUser can't be found");
    }

    /**
     * Shortcut to find a User via {@link GlobalUser}
     * @param platform
     * @param userId
     * @return A User
     * @throws NoSuchUserException when a GlobalUser can't be found, or the platform user isn't linked (shouldn't happen)
     */
    public User getPlatformUser(final Platform platform, final String userId) throws NoSuchUserException {
        final tv.v1x1.common.dto.core.GlobalUser globalUser = getByUserAsCore(platform, userId);
        if(globalUser != null) {
            final User user = globalUser.getUser(platform, userId).orElse(null);
            if(user != null)
                return user;
            else
                throw new NoSuchUserException("User is not linked to GlobalUser");
        }
        throw new NoSuchUserException("GlobalUser can't be found");
    }

    public GlobalUser getByUser(final InverseGlobalUser inverseGlobalUser) {
        if (inverseGlobalUser == null)
            return null;
        return getById(inverseGlobalUser.getGlobalUserId());
    }

    public GlobalUser getOrCreate(final Platform platform, final String userId, final String displayName) {
        return getOrCreate(platform, userId, displayName, false);
    }

    public GlobalUser getOrCreate(final Platform platform, final String userId, final String displayName, final boolean skipDeduplicator) {
        final InverseGlobalUser inverseGlobalUser = getUser(platform, userId);
        if (inverseGlobalUser == null)
            return createGlobalUser(platform, userId, displayName, skipDeduplicator);
        final GlobalUser globalUser = getById(inverseGlobalUser.getGlobalUserId());
        if (globalUser == null)
            throw new IllegalStateException("GlobalUser null but InverseGlobalUser for: " + inverseGlobalUser.getGlobalUserId().toString() + " " + inverseGlobalUser.getUserId() + " " + inverseGlobalUser.getPlatform());
        return globalUser;
    }

    public GlobalUser createGlobalUser(final Platform platform, final String userId, final String displayName) {
        return createGlobalUser(platform, userId, displayName, false);
    }

    public GlobalUser createGlobalUser(final Platform platform, final String userId, final String displayName, final boolean skipDeduplicator) {
        if(!skipDeduplicator && createDeduplicator.seenAndAdd(new tv.v1x1.common.dto.core.UUID(UUID.nameUUIDFromBytes(CompositeKey.makeKey(platform.name(), userId))))) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getOrCreate(platform, userId, displayName, true);
        }
        final GlobalUser globalUser = new GlobalUser(UUID.randomUUID(), new ArrayList<>());
        try {
            globalUser.getEntries().add(new GlobalUser.Entry(
                    platform,
                    (displayName == null
                            ? displayNameService.getIdFromDisplayName(platform, userId)
                            : displayName),
                    userId));
        } catch (final NoSuchTargetException e) {
            throw new RuntimeException(e);
        }
        final InverseGlobalUser inverseGlobalUser = new InverseGlobalUser(platform, userId, globalUser.getId());
        final BatchStatement b = new BatchStatement();
        b.add(globalUserMapper.saveQuery(globalUser));
        b.add(inverseGlobalUserMapper.saveQuery(inverseGlobalUser));
        session.execute(b);
        return globalUser;
    }

    public GlobalUser addUser(final GlobalUser globalUser, final Platform platform, final String userId, final String displayName) {
        final GlobalUser.Entry entry = new GlobalUser.Entry(platform, displayName, userId);
        if(!globalUser.getEntries().contains(entry))
            globalUser.getEntries().add(entry);
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

    public class NoSuchUserException extends Exception {
        NoSuchUserException() {
            super();
        }

        NoSuchUserException(final String message) {
            super(message);
        }

    }
}
