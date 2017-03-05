package tv.v1x1.modules.core.cli.commands;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.db.ChannelConfiguration;
import tv.v1x1.common.dto.db.ChannelPlatformMapping;
import tv.v1x1.common.dto.db.GlobalUser;
import tv.v1x1.common.dto.db.JoinedTwitchChannel;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.dto.db.TwitchOauthToken;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.state.NoSuchUserException;
import tv.v1x1.common.services.state.TwitchDisplayNameService;
import tv.v1x1.common.util.commands.annotations.Command;
import tv.v1x1.common.util.commands.annotations.CommandSet;

import java.lang.invoke.MethodHandles;

/**
 * Created by cobi on 3/4/2017.
 */
@Singleton
@CommandSet("twitch-ids")
public class TwitchIdsCommand {
    @Accessor
    public interface MigrateAccessor {
        @Query("SELECT * FROM tenant")
        Result<Tenant> allTenants();

        @Query("SELECT * FROM global_user")
        Result<GlobalUser> allGlobalUsers();

        @Query("SELECT * FROM channel_configuration")
        Result<ChannelConfiguration> allChannelConfigurations();

        @Query("SELECT * FROM channel_platform_mapping")
        Result<ChannelPlatformMapping> allPlatformMappings();

        @Query("SELECT * FROM twitch_oauth_token")
        Result<TwitchOauthToken> allTwitchOauthTokens();

        @Query("SELECT * FROM joined_twitch_channel")
        Result<JoinedTwitchChannel> allJoinedTwitchChannels();
    }

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Command(
            value = "migrate",
            usage = "migrate",
            description = "Migrate Twitch IDs"
    )
    public void migrate(final DAOManager daoManager, final MappingManager mappingManager, final TwitchDisplayNameService twitchDisplayNameService) {
        final MigrateAccessor migrateAccessor = mappingManager.createAccessor(MigrateAccessor.class);
        LOG.info("Beginning migration ...");
        LOG.info("Fetching Tenants to migrate ...");
        for(final Tenant tenant : migrateAccessor.allTenants()) {
            LOG.info("Migrating Tenant {} ...", tenant.getId());
            for(final Tenant.Entry entry : ImmutableList.copyOf(tenant.getEntries())) {
                LOG.info("Migrating Tenant {} Entry {}/{}: {} ...", tenant.getId(), entry.getPlatform(), entry.getChannelId(), entry.getDisplayName());
                if(entry.getPlatform() != Platform.TWITCH)
                    continue;
                try {
                    final String channelId = twitchDisplayNameService.getChannelIdByChannelName(entry.getChannelId());
                    final String displayName = twitchDisplayNameService.getDisplayNameFromChannelId(channelId);
                    daoManager.getDaoTenant().addChannel(tenant, Platform.TWITCH, channelId, displayName);
                    daoManager.getDaoTenant().removeChannel(tenant, Platform.TWITCH, entry.getChannelId());
                    LOG.info("Migration of Tenant {} Entry {}/{}: {} successful", tenant.getId(), entry.getPlatform(), entry.getChannelId(), entry.getDisplayName());
                } catch(final Exception e) {
                    LOG.warn("Failed migrating Tenant entry: ", e);
                }
            }
        }
        LOG.info("Fetching GlobalUsers to migrate ...");
        for(final GlobalUser globalUser : migrateAccessor.allGlobalUsers()) {
            LOG.info("Migrating GlobalUser {} ...", globalUser.getId());
            for(final GlobalUser.Entry entry : ImmutableList.copyOf(globalUser.getEntries())) {
                LOG.info("Migrating GlobalUser {} Entry {}/{}: {} ...", globalUser.getId(), entry.getPlatform(), entry.getUserId(), entry.getDisplayName());
                if(entry.getPlatform() != Platform.TWITCH)
                    continue;
                try {
                    final String userId = twitchDisplayNameService.getUserIdFromUsername(entry.getUserId());
                    final String displayName = twitchDisplayNameService.getDisplayNameFromUserId(userId);
                    daoManager.getDaoGlobalUser().addUser(globalUser, Platform.TWITCH, userId, displayName);
                    daoManager.getDaoGlobalUser().removeUser(globalUser, Platform.TWITCH, entry.getUserId());
                    LOG.info("Migration of GlobalUser {} Entry {}/{}: {} successful", globalUser.getId(), entry.getPlatform(), entry.getUserId(), entry.getDisplayName());
                } catch(final Exception e) {
                    LOG.warn("Failed migrating GlobalUser entry: ", e);
                }
            }
        }
        LOG.info("Fetching ChannelConfigurations to migrate ...");
        for(final ChannelConfiguration channelConfiguration : migrateAccessor.allChannelConfigurations()) {
            LOG.info("Migrating ChannelConfiguration {}/{}/{} ...", channelConfiguration.getModule(), channelConfiguration.getPlatform(), channelConfiguration.getChannelId());
            if(channelConfiguration.getPlatform() != Platform.TWITCH)
                continue;
            final ChannelConfiguration newChannelConfiguration;
            try {
                newChannelConfiguration = new ChannelConfiguration(
                        channelConfiguration.getModule(),
                        channelConfiguration.getTenantId(),
                        channelConfiguration.getPlatform(),
                        twitchDisplayNameService.getChannelIdByChannelName(channelConfiguration.getChannelId()),
                        channelConfiguration.getJson()
                );
                LOG.info("Saving new ChannelConfiguration: {}/{}/{} ...", newChannelConfiguration.getModule(), newChannelConfiguration.getPlatform(), newChannelConfiguration.getChannelId());
                daoManager.getDaoChannelConfiguration().put(newChannelConfiguration);
                LOG.info("Deleting old ChannelConfiguration: {}/{}/{} ...", channelConfiguration.getModule(), channelConfiguration.getPlatform(), channelConfiguration.getChannelId());
                mappingManager.mapper(ChannelConfiguration.class).delete(channelConfiguration);
                LOG.info("Migration of ChannelConfiguration {}/{}/{} successful", channelConfiguration.getModule(), channelConfiguration.getPlatform(), channelConfiguration.getChannelId());
            } catch(final Exception e) {
                LOG.warn("Failed migrating ChannelConfiguration {}/{}/{}", channelConfiguration.getModule(), channelConfiguration.getPlatform(), channelConfiguration.getChannelId(), e);
            }
        }
        LOG.info("Fetching ChannelPlatformMappings to migrate ...");
        for(final ChannelPlatformMapping channelPlatformMapping : migrateAccessor.allPlatformMappings()) {
            LOG.info("Migrating ChannelPlatformMapping {}/{}/{}", channelPlatformMapping.getPlatform(), channelPlatformMapping.getChannelId(), channelPlatformMapping.getPlatformGroup());
            if(channelPlatformMapping.getPlatform() != Platform.TWITCH)
                continue;
            try {
                final String channelId = twitchDisplayNameService.getChannelIdByChannelName(channelPlatformMapping.getChannelId());
                LOG.info("Saving new ChannelPlatformMapping {}/{}/{}", channelPlatformMapping.getPlatform(), channelId, channelPlatformMapping.getPlatformGroup());
                daoManager.getDaoTenantGroup().setChannelPlatformMapping(
                        channelPlatformMapping.getPlatform(),
                        channelId,
                        channelPlatformMapping.getPlatformGroup(),
                        daoManager.getDaoTenantGroup().getTenantGroup(
                                daoManager.getDaoTenant().getByChannel(channelPlatformMapping.getPlatform(), channelId).toCore(),
                                channelPlatformMapping.getGroupId()
                        )
                );
                LOG.info("Deleting old ChannelPlatformMapping {}/{}/{}", channelPlatformMapping.getPlatform(), channelPlatformMapping.getChannelId(), channelPlatformMapping.getPlatformGroup());
                daoManager.getDaoTenantGroup().clearChannelPlatformMapping(
                        channelPlatformMapping.getPlatform(),
                        channelPlatformMapping.getChannelId(),
                        channelPlatformMapping.getPlatformGroup()
                );
                LOG.info("Migrated ChannelPlatformMapping {}/{}/{} successfully", channelPlatformMapping.getPlatform(), channelPlatformMapping.getChannelId(), channelPlatformMapping.getPlatformGroup());
            } catch(final Exception e) {
                LOG.warn("Failed migrating ChannelPlatformMapping {}/{}/{}", channelPlatformMapping.getPlatform(), channelPlatformMapping.getChannelId(), channelPlatformMapping.getPlatformGroup(), e);
            }
        }
        LOG.info("Fetching TwitchOauthTokens to migrate ...");
        for(final TwitchOauthToken twitchOauthToken : migrateAccessor.allTwitchOauthTokens()) {
            LOG.info("Migrating TwitchOauthToken {}/{}", twitchOauthToken.getGlobalUserId(), twitchOauthToken.getUserId());
            try {
                final TwitchOauthToken newTwitchOauthToken = new TwitchOauthToken(
                        twitchOauthToken.getGlobalUserId(),
                        twitchDisplayNameService.getUserIdFromUsername(twitchOauthToken.getUserId()),
                        twitchOauthToken.getOauthToken(),
                        twitchOauthToken.getScopes()
                );
                LOG.info("Saving new TwitchOauthToken {}/{}", newTwitchOauthToken.getGlobalUserId(), newTwitchOauthToken.getUserId());
                daoManager.getDaoTwitchOauthToken().put(newTwitchOauthToken);
                LOG.info("Deleting old TwitchOauthToken {}/{}", twitchOauthToken.getGlobalUserId(), twitchOauthToken.getUserId());
                daoManager.getDaoTwitchOauthToken().delete(twitchOauthToken);
                LOG.info("Migrated TwitchOauthToken {}/{} successfully", twitchOauthToken.getGlobalUserId(), twitchOauthToken.getUserId());
            } catch(final Exception e) {
                LOG.warn("Failed migrating TwitchOauthToken {}/{}", twitchOauthToken.getGlobalUserId(), twitchOauthToken.getUserId(), e);
            }
        }
        LOG.info("Fetching JoinedTwitchChannels to migrate ...");
        for(final JoinedTwitchChannel joinedTwitchChannel : migrateAccessor.allJoinedTwitchChannels()) {
            LOG.info("Migrating JoinedTwitchChannel {}", joinedTwitchChannel.getChannel());
            try {
                final JoinedTwitchChannel newJoinedTwitchChannel = new JoinedTwitchChannel(twitchDisplayNameService.getChannelIdByChannelName(joinedTwitchChannel.getChannel()));
                LOG.info("Saving new JoinedTwitchChannel {}", newJoinedTwitchChannel.getChannel());
                daoManager.getDaoJoinedTwitchChannel().join(newJoinedTwitchChannel.getChannel());
                LOG.info("Deleting old JoinedTwitchChannel {}", joinedTwitchChannel.getChannel());
                daoManager.getDaoJoinedTwitchChannel().delete(joinedTwitchChannel.getChannel());
                LOG.info("Migrated JoinedTwitchChannel {} successfully", joinedTwitchChannel.getChannel());
            } catch(final Exception e) {
                LOG.warn("Failed migrating JoinedTwitchChannel {}", joinedTwitchChannel.getChannel(), e);
            }
        }
        LOG.info("Finished Migration.");
    }
}
