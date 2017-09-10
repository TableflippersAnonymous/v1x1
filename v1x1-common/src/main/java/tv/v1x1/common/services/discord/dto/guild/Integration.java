package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.discord.dto.user.User;

/**
 * Created by naomi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Integration {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String type;
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    private boolean syncing;
    @JsonProperty("role_id")
    private String roleId;
    @JsonProperty("expire_behavior")
    private Integer expireBehavior;
    @JsonProperty("expire_grace_period")
    private Integer expireGracePeriod;
    @JsonProperty
    private User user;
    @JsonProperty
    private IntegrationAccount account;
    @JsonProperty("synced_at")
    private String syncedAt;

    public Integration() {
    }

    public Integration(final String id, final String name, final String type, final boolean enabled,
                       final boolean syncing, final String roleId, final Integer expireBehavior,
                       final Integer expireGracePeriod, final User user, final IntegrationAccount account,
                       final String syncedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.enabled = enabled;
        this.syncing = syncing;
        this.roleId = roleId;
        this.expireBehavior = expireBehavior;
        this.expireGracePeriod = expireGracePeriod;
        this.user = user;
        this.account = account;
        this.syncedAt = syncedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSyncing() {
        return syncing;
    }

    public void setSyncing(final boolean syncing) {
        this.syncing = syncing;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(final String roleId) {
        this.roleId = roleId;
    }

    public Integer getExpireBehavior() {
        return expireBehavior;
    }

    public void setExpireBehavior(final Integer expireBehavior) {
        this.expireBehavior = expireBehavior;
    }

    public Integer getExpireGracePeriod() {
        return expireGracePeriod;
    }

    public void setExpireGracePeriod(final Integer expireGracePeriod) {
        this.expireGracePeriod = expireGracePeriod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public IntegrationAccount getAccount() {
        return account;
    }

    public void setAccount(final IntegrationAccount account) {
        this.account = account;
    }

    public String getSyncedAt() {
        return syncedAt;
    }

    public void setSyncedAt(final String syncedAt) {
        this.syncedAt = syncedAt;
    }
}
