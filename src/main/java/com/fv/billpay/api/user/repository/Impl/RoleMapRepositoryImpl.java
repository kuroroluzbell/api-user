package com.fv.billpay.api.user.repository.Impl;

import com.fv.billpay.api.user.repository.IRoleMapRepository;
import com.fv.billpay.api.user.util.KeycloakProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class RoleMapRepositoryImpl implements IRoleMapRepository {
    @Inject
    KeycloakProvider keycloakProvider;

    @Override
    public void assignRealmRoleToUser(String userId, String roleName) {
        keycloakProvider.assignRealmRoleToUser(userId, roleName);
    }

    @Override
    public void removeRealmRoleFromUser(String userId, String roleName) {
        keycloakProvider.removeRealmRoleFromUser(userId, roleName);
    }

    @Override
    public List<String> getRealmRolesOfUser(String userId) {
        return keycloakProvider.getRealmRolesOfUser(userId);
    }
}
