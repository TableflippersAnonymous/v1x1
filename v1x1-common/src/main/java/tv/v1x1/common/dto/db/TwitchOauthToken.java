package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;
import java.util.UUID;

/**
 * Created by naomi on 11/12/2016.
 */
@Table(name = "twitch_oauth_token")
public class TwitchOauthToken {
    @PartitionKey
    @Column(name = "global_user_id")
    private UUID globalUserId;
    @ClusteringColumn
    @Column(name = "user_id")
    private String userId;
    @Column(name = "oauth_token")
    private String oauthToken;
    private List<String> scopes;

    public TwitchOauthToken() {
    }

    public TwitchOauthToken(final UUID globalUserId, final String userId, final String oauthToken, final List<String> scopes) {
        this.globalUserId = globalUserId;
        this.userId = userId;
        this.oauthToken = oauthToken;
        this.scopes = scopes;
    }

    public UUID getGlobalUserId() {
        return globalUserId;
    }

    public String getUserId() {
        return userId;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public List<String> getScopes() {
        return scopes;
    }
}
