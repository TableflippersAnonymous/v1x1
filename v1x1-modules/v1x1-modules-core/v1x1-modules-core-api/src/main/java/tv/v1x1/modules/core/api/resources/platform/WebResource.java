package tv.v1x1.modules.core.api.resources.platform;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import io.dropwizard.jersey.caching.CacheControl;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.modules.core.api.ApiModule;
import tv.v1x1.modules.core.api.api.rest.WebConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;

/**
 * Created by cobi on 7/12/2017.
 */
@Path("/api/v1/platform/web")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WebResource {
    private final ApiModule module;

    @Inject
    public WebResource(final ApiModule module) {
        this.module = module;
    }

    @Path("/config")
    @GET
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public WebConfig getConfig() {
        /* Eventually this should come from the datastore or config.  For now, this allows us to move the basics
         * out of the client.
         */
        return new WebConfig(
                ImmutableMap.of(
                        Platform.TWITCH, new String(module.requireCredential("Common|Twitch|ClientId")),
                        Platform.DISCORD, new String(module.requireCredential("Common|Discord|ClientId"))
                ),
                ImmutableMap.of(
                        Platform.TWITCH, new String(module.requireCredential("Common|Twitch|RedirectUri")),
                        Platform.DISCORD, new String(module.requireCredential("Common|Discord|RedirectUri"))
                ),
                "/api/v1",
                "wss://pubsub.v1x1.tv"
        );
    }
}
