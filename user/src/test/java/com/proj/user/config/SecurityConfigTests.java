package com.proj.user.config;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SecurityConfigTests {

    private final MockMvc mockMvc;

    @Autowired
    public SecurityConfigTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testInjectedComponent() {
        AssertionsForClassTypes.assertThat(mockMvc).isNotNull();
    }

    @Test
    public void testSecuredUrlLogin() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .param("email", "admin@mail.co")
                        .param("password", "1111")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/users"));

    }

    @Test
    public void testInvalidSecuredUrlLogin() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .param("email", "admin@mail.co")
                        .param("password", "1123")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/auth/login?error"));

    }

    @Test
    public void testSecuredUrlRegisterWithTelegram() throws Exception {
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

    }

    @Test
    public void testSecuredUrlRegisterWithEmail() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .param("fullName", "Admin Korniev")
                        .param("email", "created@mail.co")
                        .param("telegram", "@user.test")
                        .param("password", "1111")
                        .param("age", "31")
                        .param("notificationType", "Email")
                )
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/api/v1/auth/login"));

    }

    @Test
    public void testSecuredUrlLoginWithOauth2() throws Exception  {
        mockMvc.perform(post("/oauth2/authorization/google"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "admin@mail.co", roles = "ADMIN")
    public void testSecuredLogoutUrl() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/auth/login"));
    }

    @Test
    public void testForbiddenUrlUserUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/api/v1/auth/login"));
    }
}
