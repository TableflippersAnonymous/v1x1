package tv.v1x1.common.dao;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.v1x1.common.dto.db.TwitchOauthToken;

import java.util.UUID;

/**
 * Created by naomi on 11/12/2016.
 */
public class DAOTwitchOauthToken {
    private final Mapper<TwitchOauthToken> mapper;

    public DAOTwitchOauthToken(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(TwitchOauthToken.class);
    }

    public void put(final TwitchOauthToken twitchOauthToken) {
        mapper.save(twitchOauthToken);
    }

    public TwitchOauthToken get(final UUID globalUserId, final String userId) {
        return mapper.get(globalUserId, userId);
    }

    public void delete(final TwitchOauthToken twitchOauthToken) {
        mapper.delete(twitchOauthToken);
    }
}
