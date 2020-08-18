package com.bithumb.jwttest.web;

import com.bithumb.jwttest.config.auth.LoginUser;
import com.bithumb.jwttest.config.auth.UserDetailsServiceImpl;
import com.bithumb.jwttest.config.auth.jwt.JwtUtils;
import com.bithumb.jwttest.domain.User;
import com.bithumb.jwttest.domain.type.Role;
import com.bithumb.jwttest.service.UserService;
import com.bithumb.jwttest.web.dto.LoginRequestDto;
import com.bithumb.jwttest.web.dto.LoginResponseDto;
import com.bithumb.jwttest.web.dto.MessageResponseDto;
import com.bithumb.jwttest.web.dto.SignUpRequestDto;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    private final PasswordEncoder encoder;

    @PostMapping("/v1/member/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {

        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser, loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(loginUser);

        // update last login at
        loginUser.getMember().updateLastLoginAt();

        return ResponseEntity.ok(LoginResponseDto.builder()
                .token(jwt)
                .email(loginUser.getMember().getEmail())
                .name(loginUser.getMember().getName())
                .lastLoginAt(loginUser.getMember().getLastLoginAt())
                .build());
    }

    @PostMapping("/v1/member/join")
    public ResponseEntity<MessageResponseDto> join(@Validated @RequestBody SignUpRequestDto signUpRequestDto) {

        // checked exist email
        if (userService.existsByEmail(signUpRequestDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Error: Email is already in use!"));
        }

        userService.save(User.builder()
                .email(signUpRequestDto.getEmail())
                .name(signUpRequestDto.getName())
                .password(encoder.encode(signUpRequestDto.getPassword()))
                .role(Role.USER)
                .build());

        return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    }

    @GetMapping("/v1/member/info")
    public ResponseEntity<LoginResponseDto> getUserInfo() {
        LoginUser loginUser = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(LoginResponseDto.builder()
                .token(null)
                .email(loginUser.getMember().getEmail())
                .name(loginUser.getMember().getName())
                .lastLoginAt(loginUser.getMember().getLastLoginAt())
                .build());
    }
}
