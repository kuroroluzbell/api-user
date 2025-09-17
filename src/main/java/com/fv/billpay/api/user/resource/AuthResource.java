package com.fv.billpay.api.user.resource;

import com.fv.billpay.api.user.dto.request.LoginRequestDto;
import com.fv.billpay.api.user.dto.response.LoginResponseDto;
import com.fv.billpay.api.user.service.IAuthService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    IAuthService authService;

    @ConfigProperty(name = "auth.client-token")
    String expectedClientToken;

    @POST
    @Path("/login")
    public Response login(LoginRequestDto dto, @HeaderParam("X-Client-Token") String clientToken) {
        // Validar el token de client_id
        if (clientToken == null || !clientToken.equals(expectedClientToken)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid client token").build();
        }
        LoginResponseDto response = authService.login(dto);
        return Response.ok(response).build();
    }
}
