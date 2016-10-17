package tv.twitchbot.common.dao;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.twitchbot.common.dto.db.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by naomi on 10/16/2016.
 */
public class DAOGlobalUser {
    private Session session;
    private Mapper<GlobalUser> globalUserMapper;
    private Mapper<InverseGlobalUser> inverseGlobalUserMapper;

    public DAOGlobalUser(MappingManager mappingManager) {
        session = mappingManager.getSession();
        globalUserMapper = mappingManager.mapper(GlobalUser.class);
        inverseGlobalUserMapper = mappingManager.mapper(InverseGlobalUser.class);
    }

    public GlobalUser getById(UUID id) {
        return globalUserMapper.get(id);
    }

    public InverseGlobalUser getUser(Platform platform, String userId) {
        return inverseGlobalUserMapper.get(platform, userId);
    }

    public GlobalUser getByUser(Platform platform, String userId) {
        return getByUser(getUser(platform, userId));
    }

    public GlobalUser getByUser(InverseGlobalUser inverseGlobalUser) {
        if(inverseGlobalUser == null)
            return null;
        return getById(inverseGlobalUser.getGlobalUserId());
    }

    public GlobalUser getOrCreate(Platform platform, String userId, String displayName) {
        InverseGlobalUser inverseGlobalUser = getUser(platform, userId);
        if(inverseGlobalUser != null) {
            GlobalUser globalUser = getById(inverseGlobalUser.getGlobalUserId());
            if(globalUser == null)
                throw new IllegalStateException("GlobalUser null but InverseGlobalUser for: " + inverseGlobalUser.getGlobalUserId().toString() + " " + inverseGlobalUser.getUserId() + " " + inverseGlobalUser.getPlatform());
            return globalUser;
        } else {
            return createGlobalUser(platform, userId, displayName);
        }
    }

    public GlobalUser createGlobalUser(Platform platform, String userId, String displayName) {
        GlobalUser globalUser = new GlobalUser(UUID.randomUUID(), new ArrayList<>());
        globalUser.getEntries().add(new GlobalUser.Entry(platform, displayName, userId));
        InverseGlobalUser inverseGlobalUser = new InverseGlobalUser(platform, userId, globalUser.getId());
        BatchStatement b = new BatchStatement();
        b.add(globalUserMapper.saveQuery(globalUser));
        b.add(inverseGlobalUserMapper.saveQuery(inverseGlobalUser));
        session.execute(b);
        return globalUser;
    }

    public GlobalUser addChannel(GlobalUser globalUser, Platform platform, String userId, String displayName) {
        globalUser.getEntries().add(new GlobalUser.Entry(platform, displayName, userId));
        InverseGlobalUser inverseGlobalUser = new InverseGlobalUser(platform, userId, globalUser.getId());
        BatchStatement b = new BatchStatement();
        b.add(globalUserMapper.saveQuery(globalUser));
        b.add(inverseGlobalUserMapper.saveQuery(inverseGlobalUser));
        session.execute(b);
        return globalUser;
    }

    public GlobalUser removeChannel(GlobalUser globalUser, Platform platform, String userId) {
        BatchStatement b = new BatchStatement();
        if(globalUser.getEntries().removeIf(entry -> entry.getPlatform() == platform && entry.getUserId().equals(userId)))
            b.add(globalUserMapper.saveQuery(globalUser));
        InverseGlobalUser inverseGlobalUser = getUser(platform, userId);
        if(inverseGlobalUser != null)
            b.add(inverseGlobalUserMapper.deleteQuery(inverseGlobalUser));
        if(b.size() > 0)
            session.execute(b);
        return globalUser;
    }

    public void delete(GlobalUser globalUser) {
        BatchStatement b = new BatchStatement();
        b.add(globalUserMapper.deleteQuery(globalUser));
        for(GlobalUser.Entry entry : globalUser.getEntries()) {
            InverseGlobalUser inverseGlobalUser = getUser(entry.getPlatform(), entry.getUserId());
            if(inverseGlobalUser != null)
                b.add(inverseGlobalUserMapper.deleteQuery(inverseGlobalUser));
        }
        session.execute(b);
    }
}
