package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.UDT;
import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.core.TwitchChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/15/2016.
 */
@Table(name = "global_user")
public class GlobalUser {
    @UDT(name = "global_user_entry")
    public static class Entry {
        private Platform platform;
        @Column(name = "display_name")
        private String displayName;

        // Unique, unchanging identifier of user on specific platform
        @Column(name = "user_id")
        private String userId;

        public Entry(Platform platform, String displayName, String userId) {
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

        public User toCore(tv.twitchbot.common.dto.core.GlobalUser globalUser) {
            switch(platform) {
                case DISCORD: return new DiscordUser(userId, globalUser, displayName);
                case TWITCH: return new TwitchUser(userId, globalUser, displayName);
                default: throw new IllegalStateException("Unknown user platform " + platform.name());
            }
        }
    }

    @PartitionKey
    private UUID id;
    private List<Entry> entries;

    public GlobalUser(UUID id, List<Entry> entries) {
        this.id = id;
        this.entries = entries;
    }

    public UUID getId() {
        return id;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public tv.twitchbot.common.dto.core.GlobalUser toCore() {
        List<User> users = new ArrayList<>();
        tv.twitchbot.common.dto.core.GlobalUser globalUser = new tv.twitchbot.common.dto.core.GlobalUser(
                new tv.twitchbot.common.dto.core.UUID(id),
                users
        );
        users.addAll(entries.stream().map(entry -> entry.toCore(globalUser)).collect(Collectors.toList()));
        return globalUser;
    }
}
