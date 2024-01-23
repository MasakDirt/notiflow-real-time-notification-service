package com.proj.user;

import com.proj.user.config.SecurityConfig;
import com.proj.user.config.UserConfig;
import com.proj.user.controller.AuthController;
import com.proj.user.controller.UserController;
import com.proj.user.mapper.UserMapper;
import com.proj.user.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest
public class UserApplicationTests {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final AuthUserService authUserService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthController authController;
    private final UserController userController;
    private final UserConfig userConfig;
    private final SecurityConfig securityConfig;

    @Autowired
    public UserApplicationTests(UserService userService, RoleService roleService, UserMapper userMapper, AuthUserService authUserService,
                                     CustomOAuth2UserService customOAuth2UserService, CustomUserDetailsService customUserDetailsService,
                                     AuthController authController, UserController userController, UserConfig userConfig, SecurityConfig securityConfig) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.authUserService = authUserService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customUserDetailsService = customUserDetailsService;
        this.authController = authController;
        this.userController = userController;
        this.userConfig = userConfig;
        this.securityConfig = securityConfig;
    }

    @Test
    public void testInjectedComponents() {
        assertThat(userService).isNotNull();
        assertThat(roleService).isNotNull();
        assertThat(userMapper).isNotNull();
        assertThat(authUserService).isNotNull();
        assertThat(customOAuth2UserService).isNotNull();
        assertThat(customUserDetailsService).isNotNull();
        assertThat(authController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(userConfig).isNotNull();
        assertThat(securityConfig).isNotNull();
    }
}
