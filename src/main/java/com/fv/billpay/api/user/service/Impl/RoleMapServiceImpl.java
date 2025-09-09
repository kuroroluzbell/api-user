package com.fv.billpay.api.user.service.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

import com.fv.billpay.api.user.repository.IRoleMapRepository;
import com.fv.billpay.api.user.service.IRoleMapService;

@ApplicationScoped
public class RoleMapServiceImpl implements IRoleMapService {
    @Inject
    IRoleMapRepository roleMapRepository;

    @Override
    public void assignRealmRoleToUser(String userId, String roleName) {
        roleMapRepository.assignRealmRoleToUser(userId, roleName);
    }

    @Override
    public void removeRealmRoleFromUser(String userId, String roleName) {
        roleMapRepository.removeRealmRoleFromUser(userId, roleName);
    }

    @Override
    public List<String> getRealmRolesOfUser(String userId) {
        return roleMapRepository.getRealmRolesOfUser(userId);
    }
}
