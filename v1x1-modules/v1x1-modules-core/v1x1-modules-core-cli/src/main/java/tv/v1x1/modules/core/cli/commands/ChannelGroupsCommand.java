package tv.v1x1.modules.core.cli.commands;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.base.Joiner;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.db.Channel;
import tv.v1x1.common.dto.db.ChannelGroup;
import tv.v1x1.common.dto.db.ChannelGroupConfiguration;
import tv.v1x1.common.dto.db.ChannelGroupPlatformMapping;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.util.commands.annotations.Command;
import tv.v1x1.common.util.commands.annotations.CommandSet;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

/**
 * Created by naomi on 3/4/2017.
 */
@Singleton
@CommandSet("channel-groups")
public class ChannelGroupsCommand {
    @Table(name = "tenant")
    public static class TenantV1 {
        @UDT(name = "tenant_entry")
        public static class Entry {
            private Platform platform;
            @Field(name = "display_name")
            private String displayName;
            @Field(name = "channel_id")
            private String channelId;

            public Entry() {
            }

            public Entry(final Platform platform, final String displayName, final String channelId) {
                this.platform = platform;
                this.displayName = displayName;
                this.channelId = channelId;
            }

            public Platform getPlatform() {
                return platform;
            }

            public String getDisplayName() {
                return displayName;
            }

            public String getChannelId() {
                return channelId;
            }

            @Override
            public String toString() {
                return "Entry{" +
                        "platform=" + platform +
                        ", channelId='" + channelId + '\'' +
                        ", displayName='" + displayName + '\'' +
                        '}';
            }
        }

        @PartitionKey
        private UUID id;
        @Column(caseSensitive = true, name = "entries")
        private List<Entry> entries;

        public TenantV1() {
        }

        public TenantV1(final UUID id, final List<Entry> entries) {
            this.id = id;
            this.entries = entries;
        }

        public UUID getId() {
            return id;
        }

        public List<Entry> getEntries() {
            return entries;
        }

        @Override
        public String toString() {
            return "TenantV1{" +
                    "id=" + id +
                    ", entries=[" + Joiner.on(", ").join(entries) +
                    "]}";
        }
    }

    @Table(name = "channel_configuration")
    public static class ChannelConfigurationV1 {
        @PartitionKey(0)
        private String module;
        @PartitionKey(1)
        @Column(name = "tenant_id")
        private UUID tenantId;
        @ClusteringColumn(0)
        private Platform platform;
        @ClusteringColumn(1)
        @Column(name = "channel_id")
        private String channelId;
        private String json;

        public ChannelConfigurationV1() {
        }

        public ChannelConfigurationV1(final String module, final UUID tenantId, final Platform platform, final String channelId, final String json) {
            this.module = module;
            this.tenantId = tenantId;
            this.platform = platform;
            this.channelId = channelId;
            this.json = json;
        }

        public String getModule() {
            return module;
        }

        public UUID getTenantId() {
            return tenantId;
        }

        public Platform getPlatform() {
            return platform;
        }

        public String getChannelId() {
            return channelId;
        }

        public String getJson() {
            return json;
        }

        @Override
        public String toString() {
            return "ChannelConfigurationV1{" +
                    "module='" + module + '\'' +
                    ", tenantId=" + tenantId +
                    ", platform=" + platform +
                    ", channelId='" + channelId + '\'' +
                    ", json='" + json + '\'' +
                    '}';
        }
    }

    @Table(name = "channel_platform_mapping")
    public static class ChannelPlatformMappingV1 {
        @PartitionKey(0)
        private Platform platform;
        @PartitionKey(1)
        @Column(name = "channel_id")
        private String channelId;
        @ClusteringColumn(0)
        @Column(name = "platform_group")
        private String platformGroup;
        @Column(name = "group_id")
        private UUID groupId;

        public ChannelPlatformMappingV1() {
        }

        public ChannelPlatformMappingV1(final Platform platform, final String channelId, final String platformGroup, final UUID groupId) {
            this.platform = platform;
            this.channelId = channelId;
            this.platformGroup = platformGroup;
            this.groupId = groupId;
        }

        public Platform getPlatform() {
            return platform;
        }

        public String getChannelId() {
            return channelId;
        }

        public String getPlatformGroup() {
            return platformGroup;
        }

        public UUID getGroupId() {
            return groupId;
        }

        @Override
        public String toString() {
            return "ChannelPlatformMappingV1{" +
                    "platform=" + platform +
                    ", channelId='" + channelId + '\'' +
                    ", platformGroup='" + platformGroup + '\'' +
                    ", groupId=" + groupId +
                    '}';
        }
    }

    @Accessor
    public interface MigrateAccessor {
        @Query("SELECT * FROM tenant")
        Result<TenantV1> allTenants();

        @Query("SELECT * FROM channel_configuration")
        Result<ChannelConfigurationV1> allChannelConfigurations();

        @Query("SELECT * FROM channel_platform_mapping")
        Result<ChannelPlatformMappingV1> allPlatformMappings();
    }

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Command(
            value = "migrate",
            usage = "migrate",
            description = "Migrate Channel Groups"
    )
    public void migrate(final MappingManager mappingManager) {
        LOG.info("Beginning migration to Channel Groups");
        final MigrateAccessor migrateAccessor = mappingManager.createAccessor(MigrateAccessor.class);
        final Mapper<Tenant> tenantMapper = mappingManager.mapper(Tenant.class);
        final Mapper<ChannelGroup> channelGroupMapper = mappingManager.mapper(ChannelGroup.class);
        final Mapper<Channel> channelMapper = mappingManager.mapper(Channel.class);
        final Mapper<ChannelGroupConfiguration> channelGroupConfigurationMapper = mappingManager.mapper(ChannelGroupConfiguration.class);
        final Mapper<ChannelGroupPlatformMapping> channelGroupPlatformMappingMapper = mappingManager.mapper(ChannelGroupPlatformMapping.class);
        final Session session = mappingManager.getSession();
        LOG.info("Preparing to migrate TenantV1 to TenantV2");
        for(final TenantV1 tenantV1 : migrateAccessor.allTenants()) {
            LOG.info("Migrating {}", tenantV1);
            final Tenant newTenant = new Tenant(tenantV1.getId(), tenantV1.getEntries().size() > 0 ? tenantV1.getEntries().get(0).getDisplayName() : "Unknown");
            final BatchStatement batchStatement = new BatchStatement();
            batchStatement.add(tenantMapper.saveQuery(newTenant));
            for(final TenantV1.Entry entry : tenantV1.getEntries()) {
                LOG.info("Processing {}", entry);
                final ChannelGroup newChannelGroup = new ChannelGroup(entry.getPlatform(), entry.getChannelId(), entry.getDisplayName(), tenantV1.getId());
                batchStatement.add(channelGroupMapper.saveQuery(newChannelGroup));
                if(entry.getPlatform().equals(Platform.TWITCH)) {
                    final Channel newChannel = new Channel(entry.getPlatform(), entry.getChannelId() + ":main", entry.getDisplayName(), entry.getChannelId());
                    batchStatement.add(channelMapper.saveQuery(newChannel));
                }
            }
            LOG.info("Saving new data");
            session.execute(batchStatement);
            LOG.info("Saved");
        }
        LOG.info("Migrated TenantV1 to TenantV2");
        LOG.info("Preparing to migrate ChannelConfigurationV1 to ChannelGroupConfigurationV2");
        for(final ChannelConfigurationV1 channelConfigurationV1 : migrateAccessor.allChannelConfigurations()) {
            LOG.info("Migrating {}", channelConfigurationV1);
            final ChannelGroupConfiguration newChannelGroupConfiguration = new ChannelGroupConfiguration(
                    channelConfigurationV1.getModule(),
                    channelConfigurationV1.getTenantId(),
                    channelConfigurationV1.getPlatform(),
                    channelConfigurationV1.getChannelId(),
                    false,
                    channelConfigurationV1.getJson()
            );
            LOG.info("Saving new data");
            channelGroupConfigurationMapper.save(newChannelGroupConfiguration);
            LOG.info("Saved");
        }
        LOG.info("Migrated ChannelConfigurationV1 to ChannelGroupConfigurationV2");
        LOG.info("Preparing to migrate ChannelPlatformMappingV1 to ChannelGroupPlatformMappingV2");
        for(final ChannelPlatformMappingV1 channelPlatformMappingV1 : migrateAccessor.allPlatformMappings()) {
            LOG.info("Migrating {}", channelPlatformMappingV1);
            final ChannelGroupPlatformMapping newChannelGroupPlatformMapping = new ChannelGroupPlatformMapping(
                    channelPlatformMappingV1.getPlatform(),
                    channelPlatformMappingV1.getChannelId(),
                    channelPlatformMappingV1.getPlatformGroup(),
                    channelPlatformMappingV1.getGroupId()
            );
            LOG.info("Saving new data");
            channelGroupPlatformMappingMapper.save(newChannelGroupPlatformMapping);
            LOG.info("Saved");
        }
        LOG.info("Migrated ChannelPlatformMappingV1 to ChannelGroupPlatformMappingV2");
        LOG.info("Finished.");
    }
}
