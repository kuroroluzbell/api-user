package com.fv.billpay.api.user.util;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

@ApplicationScoped
public class KeycloakProvider {
    @ConfigProperty(name = "keycloak.auth-server-url")
    String serverUrl;
    @ConfigProperty(name = "keycloak.realm")
    String realm;
    @ConfigProperty(name = "keycloak.resource")
    String clientId;
    @ConfigProperty(name = "keycloak.credentials.secret", defaultValue = "")
    String clientSecret;

    // Puedes parametrizar el usuario admin y password por config
    @ConfigProperty(name = "keycloak.admin.username")
    String adminUsername;
    @ConfigProperty(name = "keycloak.admin.password")
    String adminPassword;

    public Keycloak getInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret.isEmpty() ? null : clientSecret)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    public RealmResource getRealmResource() {
        return getInstance().realm(realm);
    }

    public UsersResource getUsersResource() {
        return getRealmResource().users();
    }
}
