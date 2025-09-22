package com.fv.billpay.api.user.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad User para PostgreSQL
 */
@Entity
@Table(name = "\"user\"")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends PanacheEntityBase {
    
    @Id
    @Column(name = "id")
    private UUID keycloakId;
    
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "profile_image")
    private byte[] profileImage;
}