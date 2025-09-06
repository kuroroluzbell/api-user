package com.fv.account.api.user.util;

/**
 * Clase de constantes para queries SQL y otros valores globales.
 */
public class Constants {
    private Constants() {}

    // Ejemplo de queries SQL para usuarios (ajustar según implementación real)
    public static final String SQL_SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    public static final String SQL_SELECT_ALL_USERS = "SELECT * FROM users LIMIT ? OFFSET ?";
    public static final String SQL_INSERT_USER = "INSERT INTO users (username, email, first_name, last_name, enabled, password) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE_USER = "UPDATE users SET username = ?, email = ?, first_name = ?, last_name = ?, enabled = ? WHERE id = ?";
    public static final String SQL_DELETE_USER = "DELETE FROM users WHERE id = ?";
}
