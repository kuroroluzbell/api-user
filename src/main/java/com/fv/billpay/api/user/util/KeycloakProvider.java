package com.fv.billpay.api.user.util;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import java.util.List;
import java.util.stream.Collectors;

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

    public String getServerUrl() {
        return serverUrl;
    }
    public String getRealm() {
        return realm;
    }
    public String getClientId() {
        return clientId;
    }
    public String getClientSecret() {
        return clientSecret;
    }

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

    // Asignar un rol de realm a un usuario
    public void assignRealmRoleToUser(String userId, String roleName) {
        UserResource userResource = getUsersResource().get(userId);
        RoleRepresentation role = getRealmResource().roles().get(roleName).toRepresentation();
        userResource.roles().realmLevel().add(List.of(role));
    }

    // Quitar un rol de realm a un usuario
    public void removeRealmRoleFromUser(String userId, String roleName) {
        UserResource userResource = getUsersResource().get(userId);
        RoleRepresentation role = getRealmResource().roles().get(roleName).toRepresentation();
        userResource.roles().realmLevel().remove(List.of(role));
    }

    // Listar roles de realm asignados a un usuario
    public List<String> getRealmRolesOfUser(String userId) {
        UserResource userResource = getUsersResource().get(userId);
        List<RoleRepresentation> roles = userResource.roles().realmLevel().listAll();
        return roles.stream().map(RoleRepresentation::getName).collect(Collectors.toList());
    }
}
