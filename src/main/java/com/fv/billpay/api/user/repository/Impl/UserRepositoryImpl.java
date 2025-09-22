package com.fv.billpay.api.user.repository.Impl;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.fv.billpay.api.user.dto.request.UserCreateRequestDto;
import com.fv.billpay.api.user.dto.request.UserUpdateRequestDto;
import com.fv.billpay.api.user.dto.response.UserPageResponseDto;
import com.fv.billpay.api.user.dto.response.UserResponseDto;
import com.fv.billpay.api.user.repository.IUserRepository;
import com.fv.billpay.api.user.util.KeycloakProvider;
import com.fv.billpay.api.user.entity.User;

import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Collections;
import jakarta.enterprise.context.ApplicationScoped;
/**
 * Implementación de IUserRepository. Aquí se debe integrar con Keycloak Admin Client.
 */
@ApplicationScoped
public class UserRepositoryImpl implements IUserRepository {
    @Inject
    KeycloakProvider keycloakProvider;
    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto dto) {
        // Crear usuario en Keycloak
        UsersResource usersResource = keycloakProvider.getUsersResource();
        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEnabled(dto.getEnabled());
        user.setEmailVerified(true);

        // Set credentials (password)
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.getPassword());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            String location = response.getHeaderString("Location");
            String keycloakUserId = location != null ? location.substring(location.lastIndexOf("/") + 1) : null;
            
            try {
                // Guardar usuario en PostgreSQL
                User dbUser = new User();
                dbUser.setKeycloakId(UUID.fromString(keycloakUserId));
                dbUser.setUsername(dto.getUsername());
                dbUser.setEmail(dto.getEmail());
                dbUser.setFirstName(dto.getFirstName());
                dbUser.setLastName(dto.getLastName());
                dbUser.setCreatedAt(LocalDateTime.now());
                dbUser.persist();

                // Preparar respuesta
                UserResponseDto result = new UserResponseDto();
                result.setId(keycloakUserId);
                result.setUsername(dto.getUsername());
                result.setEmail(dto.getEmail());
                result.setFirstName(dto.getFirstName());
                result.setLastName(dto.getLastName());
                result.setEnabled(dto.getEnabled());
                response.close();
                return result;
                
            } catch (Exception e) {
                // Si falla el guardado en PostgreSQL, intentar eliminar de Keycloak
                try {
                    usersResource.get(keycloakUserId).remove();
                } catch (Exception keycloakException) {
                    // Log del error pero no lanzar excepción para no ocultar la original
                    System.err.println("Error al eliminar usuario de Keycloak tras fallo en PostgreSQL: " + keycloakException.getMessage());
                }
                response.close();
                throw new RuntimeException("Error guardando usuario en PostgreSQL: " + e.getMessage(), e);
            }
        } else {
            String errorMsg = response.readEntity(String.class);
            response.close();
            throw new RuntimeException("Error creando usuario en Keycloak: " + response.getStatus() + " - " + errorMsg);
        }
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto dto) {
        // Actualizar en Keycloak
        UsersResource usersResource = keycloakProvider.getUsersResource();
        UserRepresentation user = usersResource.get(dto.getId()).toRepresentation();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEnabled(dto.getEnabled());

        usersResource.get(dto.getId()).update(user);

        // Actualizar en PostgreSQL
        User dbUser = User.find("keycloakId", UUID.fromString(dto.getId())).firstResult();
        if (dbUser != null) {
            dbUser.setUsername(dto.getUsername());
            dbUser.setEmail(dto.getEmail());
            dbUser.setFirstName(dto.getFirstName());
            dbUser.setLastName(dto.getLastName());
            // No actualizar createdAt, solo al crear
            dbUser.persist();
        }

        // Devolver el usuario actualizado
        UserResponseDto result = new UserResponseDto();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setEmail(user.getEmail());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setEnabled(user.isEnabled());
        return result;
    }

    @Override
    @Transactional
    public boolean deleteUser(String id) {
        try {
            // Eliminar de Keycloak
            UsersResource usersResource = keycloakProvider.getUsersResource();
            usersResource.get(id).remove();
            
            // Eliminar de PostgreSQL
            User dbUser = User.find("keycloakId", UUID.fromString(id)).firstResult();
            if (dbUser != null) {
                dbUser.delete();
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserResponseDto getUserById(String id) {
        UsersResource usersResource = keycloakProvider.getUsersResource();
        try {
            UserRepresentation user = usersResource.get(id).toRepresentation();
            UserResponseDto dto = new UserResponseDto();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEnabled(user.isEnabled());
            return dto;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserPageResponseDto getAllUsers(int page, int size) {
        UsersResource usersResource = keycloakProvider.getUsersResource();
        List<UserRepresentation> userList = usersResource.list(page * size, size);

        List<UserResponseDto> users = userList.stream().map(user -> {
            UserResponseDto dto = new UserResponseDto();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEnabled(user.isEnabled());
            return dto;
        }).toList();

        int total = usersResource.count();
        int totalPages = (int) Math.ceil((double) total / size);

        UserPageResponseDto pageDto = new UserPageResponseDto();
        pageDto.setUsers(users);
        pageDto.setTotalElements(total);
        pageDto.setTotalPages(totalPages);
        pageDto.setPage(page);
        pageDto.setSize(size);
        return pageDto;
    }

    @Override
    public void assignRealmRoleToUser(String userId, String roleName) {
        keycloakProvider.assignRealmRoleToUser(userId, roleName);
    }

    @Override
    public void removeRealmRoleFromUser(String userId, String roleName) {
        keycloakProvider.removeRealmRoleFromUser(userId, roleName);
    }

    @Override
    public java.util.List<String> getRealmRolesOfUser(String userId) {
        return keycloakProvider.getRealmRolesOfUser(userId);
    }
}
