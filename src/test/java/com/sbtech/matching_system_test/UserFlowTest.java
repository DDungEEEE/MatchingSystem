package com.sbtech.matching_system_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbtech.matching_system_test.dto.LoginRequest;
import com.sbtech.matching_system_test.dto.LoginResponse;
import com.sbtech.matching_system_test.dto.UserSignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserFlowTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 사용자_회원가입_로그인_수리요청() throws Exception {
        // 1. 회원가입
        var signUp = new UserSignUpRequest("홍길동", "user100", "1234", "010-5555-5555");

        mockMvc.perform(post("/api/signup/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUp)))
                .andExpect(status().isOk());

        // 2. 로그인
        var login = new LoginRequest("user100", "1234");

        String loginResponse = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LoginResponse tokens = objectMapper.readValue(loginResponse, LoginResponse.class);

        // 3. 수리요청 생성
        mockMvc.perform(post("/api/requests")
                        .header("Authorization", "Bearer " + tokens.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"description":"노트북 배터리 교체","latitude":37.5,"longitude":127.0}
                        """))
                .andExpect(status().isOk());
    }
}
