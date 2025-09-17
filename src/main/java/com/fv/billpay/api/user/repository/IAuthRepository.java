package com.fv.billpay.api.user.repository;

import com.fv.billpay.api.user.dto.request.LoginRequestDto;
import com.fv.billpay.api.user.dto.response.LoginResponseDto;

public interface IAuthRepository {
    LoginResponseDto login(LoginRequestDto dto);
}
