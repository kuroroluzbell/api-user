package com.fv.billpay.api.user.service;

import java.util.List;

public interface IRoleMapService {
    void assignRealmRoleToUser(String userId, String roleName);
    void removeRealmRoleFromUser(String userId, String roleName);
    List<String> getRealmRolesOfUser(String userId);
}
