package com.fv.account.api.user.resource;

import com.fv.account.api.user.dto.request.UserCreateRequestDto;
import com.fv.account.api.user.dto.request.UserUpdateRequestDto;
import com.fv.account.api.user.dto.response.UserPageResponseDto;
import com.fv.account.api.user.dto.response.UserResponseDto;
import com.fv.account.api.user.service.IUserService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Resource REST para el CRUD de usuarios en Keycloak.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    IUserService userService;

    @POST
    //@RolesAllowed({"user", "admin","billpay_user_create"})
    //@RolesAllowed({"*"})
    public Response createUser(@Valid UserCreateRequestDto dto) {
        UserResponseDto response = userService.createUser(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    //@RolesAllowed({"user", "admin","billpay_user_update"})
    public Response updateUser(@Valid UserUpdateRequestDto dto) {
        UserResponseDto response = userService.updateUser(dto);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    //@RolesAllowed({"user", "admin","billpay_user_delete"})
    public Response deleteUser(@PathParam("id") String id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}")
//    @RolesAllowed({"user", "admin","billpay_user_selectone"})
    public Response getUserById(@PathParam("id") String id) {
        UserResponseDto response = userService.getUserById(id);
        if (response != null) {
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    //@RolesAllowed({"user", "admin","billpay_user_selectall"})
    //@RolesAllowed({"*"})
    public Response getAllUsers(@QueryParam("page") @DefaultValue("0") int page,
                                @QueryParam("size") @DefaultValue("10") int size) {
        UserPageResponseDto response = userService.getAllUsers(page, size);
        return Response.ok(response).build();
    }
}
