package com.fv.account.api.user.repository;

import com.fv.account.api.user.dto.request.UserCreateRequestDto;
import com.fv.account.api.user.dto.request.UserUpdateRequestDto;
import com.fv.account.api.user.dto.response.UserPageResponseDto;
import com.fv.account.api.user.dto.response.UserResponseDto;

public interface IUserRepository {
    UserResponseDto createUser(UserCreateRequestDto dto);
    UserResponseDto updateUser(UserUpdateRequestDto dto);
    boolean deleteUser(String id);
    UserResponseDto getUserById(String id);
    UserPageResponseDto getAllUsers(int page, int size);
}
