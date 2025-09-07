package com.fv.billpay.api.user.dto.response;

import lombok.Data;

/**
 * DTO de respuesta para usuario Keycloak.
 */
@Data
public class UserResponseDto {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
}
