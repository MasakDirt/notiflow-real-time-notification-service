package com.proj.user.controller;

import com.proj.user.model.NotificationType;
import com.proj.user.model.User;
import com.proj.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.proj.user.TestAdvice.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTests {

    private final MockMvc mockMvc;
    private final UserService userService;

    @Autowired
    public UserControllerTests(MockMvc mockMvc, UserService userService) {
        this.mockMvc = mockMvc;
        this.userService = userService;
    }

    @Test
    public void testInjectedComponents() {
        assertThat(mockMvc).isNotNull();
        assertThat(userService).isNotNull();
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testGetUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("email", "isAdmin",
                        "page", "sort_order", "sort_by", "users"))
                .andExpect(view().name("users-list"));
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testGetUpdateFormUser() throws Exception {
        long id = getUserId();
        mockMvc.perform(get("/api/v1/users/{id}/update", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("updateRequest"))
                .andExpect(view().name("user-update"));
    }

    @Test
    @WithMockUser(username = "admin@mail.co")
    public void testGetUpdateFormAdmin() throws Exception {
        long id = getUserId();
        mockMvc.perform(get("/api/v1/users/{id}/update", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("updateRequest"))
                .andExpect(view().name("user-update"));
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testForbiddenGetUpdateFormUser() throws Exception {
        long id = getAdminId();
        mockMvc.perform(get("/api/v1/users/{id}/update", id))
                .andExpect(status().isOk())
                .andExpect(getErrorModelAttributes())
                .andExpect(getErrorViewName());
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testUpdateUser() throws Exception {
        String fullName = "Updated Name";
        String telegram = "@updated";
        String age = "23";
        String notificationType = "telegram";
        performUserUpdate(fullName, telegram, age, notificationType);
    }

    @Test
    @WithMockUser(username = "admin@mail.co")
    public void testUpdateAdmin() throws Exception {
        String fullName = "Updated Name With Admin";
        String telegram = "@updatedad";
        String age = "20";
        String notificationType = "e-mail";
        performUserUpdate(fullName, telegram, age, notificationType);
    }

    private void performUserUpdate(String fullName, String telegram,
                                   String age, String notificationType) throws Exception {
        long id = getUserId();

        mockMvc.perform(post("/api/v1/users/{id}/update", id)
                        .param("fullName", fullName)
                        .param("telegram", telegram)
                        .param("age", age)
                        .param("notificationType", notificationType)
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/users"));

        assertionsForUserUpdate(fullName, telegram, age, notificationType);
    }

    private void assertionsForUserUpdate(String fullName, String telegram,
                                         String age, String notificationType) {
        User userAfterUpdate = userService.readByEmail("user@mail.co");
        assertEquals(fullName, userAfterUpdate.getFullName());
        assertEquals(telegram, userAfterUpdate.getTelegram());
        assertEquals(Integer.parseInt(age), userAfterUpdate.getAge());
        assertEquals(NotificationType.getTypeFromName(notificationType),
                userAfterUpdate.getNotificationType());
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testForbiddenUpdateUser() throws Exception {
        long id = getAdminId();
        mockMvc.perform(post("/api/v1/users/{id}/update", id)
                        .param("fullName", "Invalid")
                        .param("telegram", "@tele")
                        .param("age", "10")
                        .param("notificationType", "telegram")
                )
                .andExpect(status().isOk())
                .andExpect(getErrorModelAttributes())
                .andExpect(getErrorViewName());
    }

    @Test
    @WithMockUser(username = "admin@mail.co")
    public void testInvalidUpdateAdmin() throws Exception {
        long id = getAdminId();
        mockMvc.perform(post("/api/v1/users/{id}/update", id)
                        .param("fullName", "Empty may be")
                        .param("telegram", "@")
                        .param("age", "10")
                        .param("notificationType", "")
                )
                .andExpect(status().isOk())
                .andExpect(getErrorModelAttributes())
                .andExpect(getErrorViewName());
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testGetAddingDataFormUser() throws Exception {
        long id = getUserId(); // user has Google provider and access has users only with Google provider!!!
        mockMvc.perform(get("/api/v1/users/{id}/add-data", id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("addDataRequest", "id"))
                .andExpect(view().name("add-data"));
    }

    @Test
    @WithMockUser(username = "admin@mail.co")
    public void testForbiddenGetAddingDataFormAdmin() throws Exception {
        long id = getAdminId(); // admin has Local provider, but access has users only with Google provider!!!
        mockMvc.perform(get("/api/v1/users/{id}/add-data", id))
                .andExpect(status().isOk())
                .andExpect(getErrorModelAttributes())
                .andExpect(getErrorViewName());
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testValidAddingDataToOAuthUserUser() throws Exception {
        long id = getUserId(); // user has Google provider and access has users only with Google provider!!!
        mockMvc.perform(post("/api/v1/users/{id}/add-data", id)
                        .param("telegram", "@telegram")
                        .param("age", "12")
                        .param("notificationType", "E-mail")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/users"));
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testInvalidAddingDataToOAuthUserUser() throws Exception {
        long id = getUserId();
        mockMvc.perform(post("/api/v1/users/{id}/add-data", id)
                        .param("telegram", "@")
                        .param("age", "")
                        .param("notificationType", "tele")
                )
                .andExpect(status().isOk())
                .andExpect(getErrorModelAttributes())
                .andExpect(getErrorViewName());
    }

    @Test
    @WithMockUser(username = "admin@mail.co")
    public void testForbiddenAddingDataToOAuthUserAdmin() throws Exception {
        long id = getAdminId(); // admin has Local provider, but access has users only with Google provider!!!
        mockMvc.perform(post("/api/v1/users/{id}/add-data", id)
                        .param("telegram", "@your")
                        .param("age", "35")
                        .param("notificationType", "E-mail")
                )
                .andExpect(status().isOk())
                .andExpect(getErrorModelAttributes())
                .andExpect(getErrorViewName());
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testDeleteUser() throws Exception {
        long id = getUserId();
        performDeleteUser(id);
    }

    @Test
    @WithMockUser(username = "admin@mail.co")
    public void testDeleteAdminHimself() throws Exception {
        long id = getAdminId();
        performDeleteUser(id);
    }

    private void performDeleteUser(long id) throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}/delete", id))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/auth/login"));
    }

    @Test
    @WithMockUser(username = "admin@mail.co")
    public void testDeleteAdminAnotherUser() throws Exception {
        long id = getUserId();
        mockMvc.perform(get("/api/v1/users/{id}/delete", id))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/v1/users"));
    }

    @Test
    @WithMockUser(username = "user@mail.co")
    public void testForbiddenDeleteUser() throws Exception {
        long id = getAdminId();
        mockMvc.perform(get("/api/v1/users/{id}/delete", id))
                .andExpect(status().isOk())
                .andExpect(getErrorModelAttributes())
                .andExpect(getErrorViewName());
    }

    private long getUserId() {
        return userService.readByEmail("user@mail.co").getId();
    }

    private long getAdminId() {
        return userService.readByEmail("admin@mail.co").getId();
    }
}
