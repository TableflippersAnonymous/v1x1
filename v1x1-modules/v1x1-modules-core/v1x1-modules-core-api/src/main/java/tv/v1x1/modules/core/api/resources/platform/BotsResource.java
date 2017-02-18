package tv.v1x1.modules.core.api.resources.platform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import io.dropwizard.jersey.caching.CacheControl;
import tv.v1x1.common.dao.DAOJoinedTwitchChannel;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.Tenant;
import tv.v1x1.common.modules.GlobalConfiguration;
import tv.v1x1.common.modules.TenantConfiguration;
import tv.v1x1.common.services.cache.CacheManager;
import tv.v1x1.common.services.persistence.ConfigurationCacheManager;
import tv.v1x1.common.services.persistence.ConfigurationProvider;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.common.services.persistence.TenantConfigurationProvider;
import tv.v1x1.common.services.queue.MessageQueueManager;
import tv.v1x1.modules.core.api.api.ApiList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /platform
    /bots
      /{platform} - GET: list of bots that v1x1 is using on platform.
        /{botname} - GET: list of channels bot is in
 */
@Path("/api/v1/platform/bots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BotsResource {
    public static class TmiTenantConfiguration implements TenantConfiguration {
        @JsonProperty("custom_bot")
        private boolean customBot = false;

        @JsonProperty("bot_name")
        private String botName;

        public boolean isCustomBot() {
            return customBot;
        }

        public String getBotName() {
            return botName;
        }
    }

    public static class TmiGlobalConfiguration implements GlobalConfiguration {
        @JsonProperty("default_username")
        private String defaultUsername = "v1x1";

        public String getDefaultUsername() {
            return defaultUsername;
        }
    }

    private final DAOJoinedTwitchChannel daoJoinedTwitchChannel;
    private final DAOTenant daoTenant;
    private final TenantConfigurationProvider<TmiTenantConfiguration> tmiTenantConfigProvider;
    private final ConfigurationProvider<TmiGlobalConfiguration> tmiGlobalConfigProvider;

    @Inject
    public BotsResource(final DAOManager daoManager, final CacheManager cacheManager, final MessageQueueManager messageQueueManager, final ConfigurationCacheManager configurationCacheManager) {
        this.daoJoinedTwitchChannel = daoManager.getDaoJoinedTwitchChannel();
        this.daoTenant = daoManager.getDaoTenant();
        this.tmiTenantConfigProvider = new TenantConfigurationProvider<>(new Module("tmi"), cacheManager, daoManager, TmiTenantConfiguration.class, messageQueueManager, configurationCacheManager);
        this.tmiGlobalConfigProvider = new ConfigurationProvider<>(new Module("tmi"), cacheManager, daoManager, TmiGlobalConfiguration.class, messageQueueManager, configurationCacheManager);
    }

    @Path("/{platform}")
    @GET
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public ApiList<String> listBotsOnPlatform(@PathParam("platform") final String platform) {
        if(!platform.equals("twitch"))
            throw new NotFoundException();
        return new ApiList<>(new ArrayList<>(getTwitchBots().keySet()));
    }

    @Path("/{platform}/{botname}")
    @GET
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public ApiList<String> listChannelsBotOn(@PathParam("platform") final String platform, @PathParam("botname") final String botName) {
        if(!platform.equals("twitch"))
            throw new NotFoundException();
        final Map<String, List<String>> twitchBots = getTwitchBots();
        if(!twitchBots.containsKey(botName))
            throw new NotFoundException();
        return new ApiList<>(twitchBots.get(botName));
    }

    private Map<String, List<String>> getTwitchBots() {
        final String defaultUsername = tmiGlobalConfigProvider.getConfiguration().getDefaultUsername();
        final Map<String, List<String>> ret = new HashMap<>();
        daoJoinedTwitchChannel.list().forEach(joinedTwitchChannel -> {
            final Tenant tenant = daoTenant.getByChannel(Platform.TWITCH, joinedTwitchChannel.getChannel());
            if(tenant == null)
                return;
            final TmiTenantConfiguration tmiTenantConfiguration = tmiTenantConfigProvider.getTenantConfiguration(tenant.toCore());
            if(tmiTenantConfiguration == null)
                return;
            final String botName = tmiTenantConfiguration.isCustomBot()
                    ? tmiTenantConfiguration.getBotName()
                    : tmiTenantConfiguration.getBotName() == null
                        ? defaultUsername
                        : tmiTenantConfiguration.getBotName();
            if(!ret.containsKey(botName))
                ret.put(botName, new ArrayList<>());
            ret.get(botName).add(joinedTwitchChannel.getChannel());
        });
        return ret;
    }
}
