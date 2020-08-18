package com.bithumb.jwttest.domain;

import com.bithumb.jwttest.domain.type.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    private String email;

    @Column(length = 50)
    private String name;

    @Column
    private String password;

    @Column
    private LocalDateTime lastLoginAt;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Builder
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }
}
