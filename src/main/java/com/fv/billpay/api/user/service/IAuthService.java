package com.fv.billpay.api.user.service;

import com.fv.billpay.api.user.dto.request.LoginRequestDto;
import com.fv.billpay.api.user.dto.response.LoginResponseDto;

public interface IAuthService {
    LoginResponseDto login(LoginRequestDto dto);
}
