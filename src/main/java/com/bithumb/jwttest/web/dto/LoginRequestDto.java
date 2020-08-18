package com.bithumb.jwttest.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
