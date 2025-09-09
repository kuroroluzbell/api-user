package com.fv.billpay.api.user.repository;

import java.util.List;

public interface IRoleMapRepository {
    void assignRealmRoleToUser(String userId, String roleName);
    void removeRealmRoleFromUser(String userId, String roleName);
    List<String> getRealmRolesOfUser(String userId);
}
