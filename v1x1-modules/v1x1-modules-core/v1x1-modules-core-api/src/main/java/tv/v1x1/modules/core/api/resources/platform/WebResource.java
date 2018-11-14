package tv.v1x1.modules.core.api.resources.platform;

import com.google.inject.Inject;
import io.dropwizard.jersey.caching.CacheControl;
import tv.v1x1.modules.core.api.api.rest.WebConfig;
import tv.v1x1.modules.core.api.services.ApiDataProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;

/**
 * Created by naomi on 7/12/2017.
 */
@Path("/api/v1/platform/web")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WebResource {
    private final ApiDataProvider dataProvider;

    @Inject
    public WebResource(final ApiDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Path("/config")
    @GET
    @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
    public WebConfig getConfig() {
        return dataProvider.getConfiguration();
    }
}
