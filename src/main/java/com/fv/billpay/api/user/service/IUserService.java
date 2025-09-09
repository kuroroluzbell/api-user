package com.fv.billpay.api.user.service;

import com.fv.billpay.api.user.dto.request.UserCreateRequestDto;
import com.fv.billpay.api.user.dto.request.UserUpdateRequestDto;
import com.fv.billpay.api.user.dto.response.UserPageResponseDto;
import com.fv.billpay.api.user.dto.response.UserResponseDto;

public interface IUserService {
    UserResponseDto createUser(UserCreateRequestDto dto);
    UserResponseDto updateUser(UserUpdateRequestDto dto);
    boolean deleteUser(String id);
    UserResponseDto getUserById(String id);
    UserPageResponseDto getAllUsers(int page, int size);

    void assignRealmRoleToUser(String userId, String roleName);
    void removeRealmRoleFromUser(String userId, String roleName);
    java.util.List<String> getRealmRolesOfUser(String userId);
}
