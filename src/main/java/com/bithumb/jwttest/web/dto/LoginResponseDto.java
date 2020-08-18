package com.bithumb.jwttest.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LoginResponseDto {
    private String token;
    private String type = "Bearer";
    private String email;
    private String name;
    private LocalDateTime lastLoginAt;

    @Builder
    public LoginResponseDto(String token, String email, String name, LocalDateTime lastLoginAt) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.lastLoginAt = lastLoginAt;
    }
}
