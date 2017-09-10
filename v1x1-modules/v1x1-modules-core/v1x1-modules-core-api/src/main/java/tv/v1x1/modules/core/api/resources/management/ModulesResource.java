package tv.v1x1.modules.core.api.resources.management;

import com.google.inject.Inject;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.ModuleInstance;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.db.GlobalConfiguration;
import tv.v1x1.common.services.coordination.ModuleRegistry;
import tv.v1x1.common.services.persistence.DAOManager;
import tv.v1x1.modules.core.api.api.rest.ApiList;
import tv.v1x1.modules.core.api.api.rest.Configuration;
import tv.v1x1.modules.core.api.auth.Authorizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/26/2016.
 */
/*
  /management
    /modules - GET: list running modules
      /{module}
        /instances - GET: list running instance of module
        /config - GET: GlobalConfiguration for module; PUT: Update GlobalConfiguration
 */
@Path("/api/v1/management/modules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModulesResource {
    private final Authorizer authorizer;
    private final ModuleRegistry moduleRegistry;
    private final DAOManager daoManager;

    @Inject
    public ModulesResource(final Authorizer authorizer, final ModuleRegistry moduleRegistry, final DAOManager daoManager) {
        this.authorizer = authorizer;
        this.moduleRegistry = moduleRegistry;
        this.daoManager = daoManager;
    }

    @GET
    public ApiList<String> listRunningModules(@HeaderParam("Authorization") final String authorization) {
        authorizer.forAuthorization(authorization).ensurePermission("api.modules.read");
        return new ApiList<>(moduleRegistry.modules().stream().map(Module::getName).collect(Collectors.toList()));
    }

    @Path("/{module}/instances")
    @GET
    public ApiList<String> listRunningInstances(@HeaderParam("Authorization") final String authorization,
                                                @PathParam("module") final String moduleName) {
        authorizer.forAuthorization(authorization).ensurePermission("api.modules.read");
        return new ApiList<>(moduleRegistry.getInstances(new Module(moduleName)).stream().map(ModuleInstance::getId)
                .map(UUID::toString).collect(Collectors.toList()));
    }

    @Path("/{module}/config")
    @GET
    public Configuration getConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("module") final String moduleName) {
        authorizer.forAuthorization(authorization).ensurePermission("api.modules.read");
        return new Configuration(daoManager.getDaoGlobalConfiguration().get(new Module(moduleName)).getJson(), true);
    }

    @Path("/{module}/config")
    @PUT
    public Configuration putConfig(@HeaderParam("Authorization") final String authorization,
                                   @PathParam("module") final String moduleName,
                                   final Configuration configuration) {
        authorizer.forAuthorization(authorization).ensurePermission("api.modules.write");
        daoManager.getDaoGlobalConfiguration().put(new GlobalConfiguration(moduleName, configuration.getConfigJson()));
        return configuration;
    }
}
