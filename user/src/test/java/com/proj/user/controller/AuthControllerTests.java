package com.proj.user.controller;

import com.proj.user.repository.UserRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.proj.user.TestAdvice.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthControllerTests {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;

    @Autowired
    public AuthControllerTests(MockMvc mockMvc, UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
    }

    @Test
    public void testInjectedComponents() {
        AssertionsForClassTypes.assertThat(mockMvc).isNotNull();
        AssertionsForClassTypes.assertThat(userRepository).isNotNull();
    }

    @Test
    public void testLoginGet() throws Exception{
        mockMvc.perform(get("/api/v1/auth/login"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("loginRequest"))
                .andExpect(view().name("login"));
    }

    @Test
    public void testValidLoginPost() throws Exception{
        mockMvc.perform(post("/api/v1/auth/login")
                        .param("email", "user@mail.co")
                        .param("password", "2222")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/users"));
    }

    @Test
    public void testInvalidPasswordLoginPost() throws Exception{
        mockMvc.perform(post("/api/v1/auth/login")
                        .param("email", "admin@mail.co")
                        .param("password", "2222")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/auth/login?error"));
    }

    @Test
    public void testInvalidEmailLoginPost() throws Exception{
        mockMvc.perform(post("/api/v1/auth/login")
                        .param("email", "")
                        .param("password", "2222")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/auth/login?error"));
    }

    @Test
    public void testRegisterGet() throws Exception{
        mockMvc.perform(get("/api/v1/auth/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registerRequest"))
                .andExpect(view().name("register"));
    }

    @Test
    public void testValidRegisterPost() throws Exception {
        int usersSizeBeforeRegister = userRepository.findAll().size();

        mockMvc.perform(post("/api/v1/auth/register")
                        .param("fullName", "Admin Korniev")
                        .param("email", "created@mail.co")
                        .param("telegram", "@user.test")
                        .param("password", "1111")
                        .param("age", "31")
                        .param("notificationType", "Telegram")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/telegram/bot-url"));

        int usersSizeAfterRegister = userRepository.findAll().size();
        Assertions.assertTrue(usersSizeBeforeRegister < usersSizeAfterRegister);
    }

    @Test
    public void testInvalidFullNameRegisterPost() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .param("fullName", "")
                        .param("email", "created@mail.co")
                        .param("telegram", "@user.test")
                        .param("password", "1111")
                        .param("age", "31")
                        .param("notificationType", "Telegram")
                )
                .andExpect(status().isOk())
                .andExpect(getErrorModelAttributes())
                .andExpect(getErrorViewName());
    }
}
