package com.fv.account.api.user.service;

import com.fv.account.api.user.dto.request.UserCreateRequestDto;
import com.fv.account.api.user.dto.request.UserUpdateRequestDto;
import com.fv.account.api.user.dto.response.UserPageResponseDto;
import com.fv.account.api.user.dto.response.UserResponseDto;
import com.fv.account.api.user.repository.IUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementación de IUserService.
 */
@ApplicationScoped
public class UserServiceImpl implements IUserService {
    @Inject
    IUserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserCreateRequestDto dto) {
        return userRepository.createUser(dto);
    }

    @Override
    public UserResponseDto updateUser(UserUpdateRequestDto dto) {
        return userRepository.updateUser(dto);
    }

    @Override
    public boolean deleteUser(String id) {
        return userRepository.deleteUser(id);
    }

    @Override
    public UserResponseDto getUserById(String id) {
        return userRepository.getUserById(id);
    }

    @Override
    public UserPageResponseDto getAllUsers(int page, int size) {
        return userRepository.getAllUsers(page, size);
    }
}
