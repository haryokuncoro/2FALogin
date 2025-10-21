package com.project._FALogin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username; // email or phone
    private String password; // store hashed in production (BCrypt)
    private String fullname;
    private String phone;
    private boolean enabled = true;
}
