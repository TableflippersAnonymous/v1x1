package tv.v1x1.modules.core.api.resources.management;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by cobi on 10/26/2016.
 */
/*
  /management
    /modules - GET: list running modules
      /{module}
        /instances - GET: list running instance of module
          /{instanceid}
            /state - GET: show state of module instance; PUT: change state of module instance
        /config - GET: GlobalConfiguration for module; PUT: Update GlobalConfiguration
        /state - GET: show state of module; PUT: change state of module
 */
@Path("/api/v1/management/modules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModulesResource {
    @GET
    public List<String> listRunningModules() {
        return null; //TODO
    }

    @Path("/{module}/instances")
    @GET
    public List<String> listRunningInstances(@PathParam("module") final String moduleName) {
        return null; //TODO
    }

    @Path("/{module}/instances/{instance}/state")
    @GET
    public String getInstanceState(@PathParam("module") final String moduleName, @PathParam("instance") final String instanceId) {
        return null; //TODO
    }

    @Path("/{module}/instances/{instance}/state")
    @PUT
    public String putInstanceState(@PathParam("module") final String moduleName, @PathParam("instance") final String instanceId,
                                   final String newState) {
        return null; //TODO
    }

    @Path("/{module}/config")
    @GET
    public Configuration getConfig(@PathParam("module") final String moduleName) {
        return null; //TODO
    }

    @Path("/{module}/config")
    @PUT
    public Configuration putConfig(@PathParam("module") final String moduleName, final Configuration configuration) {
        return null; //TODO
    }

    @Path("/{module}/state")
    @GET
    public String getModuleState(@PathParam("module") final String moduleName) {
        return null; //TODO
    }

    @Path("/{module}/state")
    @PUT
    public String putModuleState(@PathParam("module") final String moduleName, final String newState) {
        return null; //TODO
    }
}
