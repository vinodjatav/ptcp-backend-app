package com.vinod.ptcp_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;  // Store hashed password

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(Long id) {
        this.id = id;
    }

    public enum Role {
        ROLE_PARENT, ROLE_TEACHER, ROLE_ADMIN
    }
}
