package com.fv.billpay.api.user.resource;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fv.billpay.api.user.dto.request.UserCreateRequestDto;
import com.fv.billpay.api.user.dto.request.UserUpdateRequestDto;
import com.fv.billpay.api.user.dto.response.UserPageResponseDto;
import com.fv.billpay.api.user.dto.response.UserResponseDto;
import com.fv.billpay.api.user.service.IUserService;

import jakarta.annotation.security.RolesAllowed;
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

    @ConfigProperty(name = "auth.client-token")
    String expectedClientToken;

    @POST
    public Response createUser(@Valid UserCreateRequestDto dto, @HeaderParam("X-Client-Token") String clientToken) {
        if (clientToken == null || !clientToken.equals(expectedClientToken)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid client token").build();
        }
        UserResponseDto response = userService.createUser(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @RolesAllowed({"user", "admin","billpay_user_update"})
    public Response updateUser(@Valid UserUpdateRequestDto dto) {
        UserResponseDto response = userService.updateUser(dto);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"user", "admin","billpay_user_delete"})
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
    @RolesAllowed({"user", "admin","billpay_user_selectone"})
    public Response getUserById(@PathParam("id") String id) {
        UserResponseDto response = userService.getUserById(id);
        if (response != null) {
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @RolesAllowed({"user", "admin","billpay_user_selectall"})
    //@RolesAllowed({"*"})
    public Response getAllUsers(@QueryParam("page") @DefaultValue("0") int page,
                                @QueryParam("size") @DefaultValue("10") int size) {
        UserPageResponseDto response = userService.getAllUsers(page, size);
        return Response.ok(response).build();
    }

    @PUT
    @Path("/{id}/profile-image")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin", "billpay_user_update"})
    public Response updateProfileImage(@PathParam("id") String userId, 
                                       String profileImageBase64) {
        try {
            // Validar que el userId sea un UUID válido
            java.util.UUID.fromString(userId);
            
            // Validar que se envió una imagen
            if (profileImageBase64 == null || profileImageBase64.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Profile image is required\"}")
                        .build();
            }
            
            // Decodificar la imagen base64
            byte[] imageBytes = java.util.Base64.getDecoder().decode(profileImageBase64.trim());
            
            boolean updated = userService.updateProfileImage(userId, imageBytes);
            
            if (updated) {
                return Response.ok()
                        .entity("{\"message\": \"Profile image updated successfully\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"User not found\"}")
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid UUID or base64 image format\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error updating profile image\"}")
                    .build();
        }
    }

}
