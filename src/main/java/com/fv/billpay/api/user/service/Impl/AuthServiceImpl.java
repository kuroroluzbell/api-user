package com.fv.billpay.api.user.service.Impl;

import com.fv.billpay.api.user.dto.request.LoginRequestDto;
import com.fv.billpay.api.user.dto.response.LoginResponseDto;
import com.fv.billpay.api.user.repository.IAuthRepository;
import com.fv.billpay.api.user.service.IAuthService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthServiceImpl implements IAuthService {
    @Inject
    IAuthRepository authRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        return authRepository.login(dto);
    }
}
