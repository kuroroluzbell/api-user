package com.fv.account.api.user.dto.response;

import lombok.Data;
import java.util.List;

/**
 * DTO de respuesta paginada para usuarios Keycloak.
 */
@Data
public class UserPageResponseDto {
    private List<UserResponseDto> users;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
}
