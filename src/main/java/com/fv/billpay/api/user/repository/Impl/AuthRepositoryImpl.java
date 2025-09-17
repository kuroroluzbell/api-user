package com.fv.billpay.api.user.repository.Impl;

import com.fv.billpay.api.user.dto.request.LoginRequestDto;
import com.fv.billpay.api.user.dto.response.LoginResponseDto;
import com.fv.billpay.api.user.repository.IAuthRepository;
import com.fv.billpay.api.user.util.KeycloakProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
// ...existing code...
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;

@ApplicationScoped
public class AuthRepositoryImpl implements IAuthRepository {
    @Inject
    KeycloakProvider keycloakProvider;

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        Keycloak keycloak = Keycloak.getInstance(
            keycloakProvider.getServerUrl(),
            keycloakProvider.getRealm(),
            dto.getUsername(),
            dto.getPassword(),
            keycloakProvider.getClientId(),
            keycloakProvider.getClientSecret().isEmpty() ? null : keycloakProvider.getClientSecret()
        );
        AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
        LoginResponseDto response = new LoginResponseDto();
        response.setAccessToken(tokenResponse.getToken());
        response.setRefreshToken(tokenResponse.getRefreshToken());
        response.setTokenType(tokenResponse.getTokenType());
    response.setExpiresIn((int) tokenResponse.getExpiresIn());
        return response;
    }
}
