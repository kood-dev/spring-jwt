package com.bithumb.jwttest.web.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpRequestDto {
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;
}
