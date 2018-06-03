package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.base.Objects;
import tv.v1x1.common.dto.core.DiscordUser;
import tv.v1x1.common.dto.core.TwitchUser;
import tv.v1x1.common.dto.core.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by cobi on 10/15/2016.
 */
@Table(name = "global_user")
public class GlobalUser {
    @UDT(name = "global_user_entry")
    public static class Entry {
        private Platform platform;
        @Field(name = "display_name")
        private String displayName;

        // Unique, unchanging identifier of user on specific platform
        @Field(name = "user_id")
        private String userId;

        public Entry() {
        }

        public Entry(final Platform platform, final String displayName, final String userId) {
            this.platform = platform;
            this.displayName = displayName;
            this.userId = userId;
        }

        public Platform getPlatform() {
            return platform;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getUserId() {
            return userId;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Entry entry = (Entry) o;
            return platform == entry.platform &&
                    Objects.equal(userId, entry.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(platform, userId);
        }

        public User toCore(final tv.v1x1.common.dto.core.GlobalUser globalUser) {
            switch(platform) {
                case DISCORD: return new DiscordUser(userId, globalUser, displayName);
                case TWITCH: return new TwitchUser(userId, globalUser, displayName);
                default: throw new IllegalStateException("Unknown user platform " + platform.name());
            }
        }
    }

    @PartitionKey
    private UUID id;
    @Column(caseSensitive = true, name = "entries")
    private List<Entry> entries;

    public GlobalUser() {
    }

    public GlobalUser(final UUID id, final List<Entry> entries) {
        this.id = id;
        this.entries = entries;
    }

    public UUID getId() {
        return id;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public tv.v1x1.common.dto.core.GlobalUser toCore() {
        final List<User> users = new ArrayList<>();
        final tv.v1x1.common.dto.core.GlobalUser globalUser = new tv.v1x1.common.dto.core.GlobalUser(
                new tv.v1x1.common.dto.core.UUID(id),
                users
        );
        users.addAll(entries.stream().map(entry -> entry.toCore(globalUser)).collect(Collectors.toList()));
        return globalUser;
    }
}
