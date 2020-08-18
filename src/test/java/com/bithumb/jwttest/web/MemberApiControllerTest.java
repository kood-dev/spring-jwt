package com.bithumb.jwttest.web;

import com.bithumb.jwttest.domain.User;
import com.bithumb.jwttest.domain.type.Role;
import com.bithumb.jwttest.service.UserService;
import com.bithumb.jwttest.web.dto.LoginRequestDto;
import com.bithumb.jwttest.web.dto.SignUpRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    PasswordEncoder encoder;

    private MockMvc mvc;
    private User saveUser;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        String email = "test@bithumb.com";
        String name = "tester";
        String password = "test";
        saveUser = userService.save(User.builder()
                .name(name).email(email).password(encoder.encode(password)).role(Role.USER)
                .build());
    }

    @Test
    void test_join() throws Exception {
        String email = "iwana@bithumb.com";
        String name = "구기웅";
        String password = "test";
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(name, email, password);

        String url = "http://localhost:" + port + "/v1/member/join";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUpRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void login() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("test@bithumb.com", "test");
        String url = "http://localhost:" + port + "/v1/member/login";

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk());


        assertEquals(saveUser.getEmail(), loginRequest.getUsername());
//        assertEquals(user.getPassword(), password);
    }

    @Test
    void info() throws Exception {
        String url = "http://localhost:" + port + "/v1/member/info";

        mvc.perform(get(url)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGJpdGh1bWIuY29tIiwiaWF0IjoxNTk3Njc4ODY4LCJleHAiOjE1OTgwMzg4Njh9.sKu6hvWUngmD2ZhbfP615Ks3Z96TxVeCZenHuTC0R48")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

//        UserDto user = userDetailsService.findById(userId);
    }
}