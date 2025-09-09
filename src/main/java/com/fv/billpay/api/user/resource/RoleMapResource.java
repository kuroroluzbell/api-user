package com.fv.billpay.api.user.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import com.fv.billpay.api.user.service.IRoleMapService;

@Path("/user-role-map")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleMapResource {
    @Inject
    IRoleMapService roleMapService;

    @POST
    @Path("/{id}/roles/{roleName}")
    public Response assignRealmRoleToUser(@PathParam("id") String userId, @PathParam("roleName") String roleName) {
        roleMapService.assignRealmRoleToUser(userId, roleName);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}/roles/{roleName}")
    public Response removeRealmRoleFromUser(@PathParam("id") String userId, @PathParam("roleName") String roleName) {
        roleMapService.removeRealmRoleFromUser(userId, roleName);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/roles")
    public Response getRealmRolesOfUser(@PathParam("id") String userId) {
        List<String> roles = roleMapService.getRealmRolesOfUser(userId);
        return Response.ok(roles).build();
    }
}
