package com.fv.billpay.api.user.repository;

import java.util.List;

import jakarta.inject.Inject;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.fv.billpay.api.user.dto.request.UserCreateRequestDto;
import com.fv.billpay.api.user.dto.request.UserUpdateRequestDto;
import com.fv.billpay.api.user.dto.response.UserPageResponseDto;
import com.fv.billpay.api.user.dto.response.UserResponseDto;
import com.fv.billpay.api.user.util.KeycloakProvider;

import jakarta.ws.rs.core.Response;
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
    public UserResponseDto createUser(UserCreateRequestDto dto) {
        UsersResource usersResource = keycloakProvider.getUsersResource();
        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEnabled(dto.getEnabled());
        user.setEmailVerified(false);

        // Set credentials (password)
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.getPassword());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            String location = response.getHeaderString("Location");
            String userId = location != null ? location.substring(location.lastIndexOf("/") + 1) : null;
            UserResponseDto result = new UserResponseDto();
            result.setId(userId);
            result.setUsername(dto.getUsername());
            result.setEmail(dto.getEmail());
            result.setFirstName(dto.getFirstName());
            result.setLastName(dto.getLastName());
            result.setEnabled(dto.getEnabled());
            response.close();
            return result;
        } else {
            String errorMsg = response.readEntity(String.class);
            response.close();
            throw new RuntimeException("Error creando usuario en Keycloak: " + response.getStatus() + " - " + errorMsg);
        }
    }

    @Override
    public UserResponseDto updateUser(UserUpdateRequestDto dto) {
    UsersResource usersResource = keycloakProvider.getUsersResource();
    UserRepresentation user = usersResource.get(dto.getId()).toRepresentation();
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setEnabled(dto.getEnabled());

    usersResource.get(dto.getId()).update(user);

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
    public boolean deleteUser(String id) {
        UsersResource usersResource = keycloakProvider.getUsersResource();
        try {
            usersResource.get(id).remove();
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
}
