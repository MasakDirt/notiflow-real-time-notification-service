package com.proj.user.config;

import com.proj.user.exception.*;
import com.proj.user.model.CustomOAuth2User;
import com.proj.user.model.User;
import com.proj.user.service.CustomOAuth2UserService;
import com.proj.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        authorizeRequest(httpSecurity);
        loginRequest(httpSecurity);
        loginWithOauth2Request(httpSecurity);
        logoutRequest(httpSecurity);
        disableCsrf(httpSecurity);
        return httpSecurity.build();
    }

    private void authorizeRequest(HttpSecurity httpSecurity) {
        try {
            httpSecurity.authorizeHttpRequests(request ->
                    request.requestMatchers("/api/v1/auth/**", "/oauth2/**").permitAll()
                            .anyRequest().authenticated()
            );
        } catch (Exception exception) {
            log.error("Authorization exception - {}", exception.getCause().getMessage());
            throw new AuthorizationException("Something happen when you authorize, sorry for it," +
                    " we are already working on it!⚒️");
        }
    }

    private void loginRequest(HttpSecurity httpSecurity) {
        try {
            httpSecurity.formLogin(login -> login
                    .loginPage("/api/v1/auth/login")
                    .loginProcessingUrl("/api/v1/auth/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler((request, response, authentication) -> {
                        log.info("User authorize with email - {} == {}", authentication.getName(), LocalDateTime.now());
                        response.sendRedirect("/api/v1/users");
                    })
                    .failureUrl("/api/v1/auth/login?error")
                    .permitAll()
            );
        } catch (Exception exception) {
            log.error("Login exception - {}", exception.getCause().getMessage());
            throw new CustomLoginException("May be our login form does not filled in properly...\n" +
                    "If it okay, then it`s our mistake, we are already working on it!⚒️");
        }
    }

    private void loginWithOauth2Request(HttpSecurity httpSecurity) {
        try {
            httpSecurity.oauth2Login(oauth2 -> oauth2
                    .loginPage("/api/v1/auth/login")
                    .userInfoEndpoint(customOAuth2UserService)
                    .successHandler((request, response, authentication) -> {
                                CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                                String email = customOAuth2User.getName();
                                processOAuth2Authorization(customOAuth2User, email, response);
                            }
                    )
            );
        } catch (Exception exception) {
            log.error("Login with {Google} exception - {}", exception.getCause().getMessage());
            throw new GoogleLoginException("Something went wrong, sorry it`s our mistake," +
                    " we are already working on it!⚒️");
        }
    }

    private void processOAuth2Authorization(CustomOAuth2User customOAuth2User, String email, HttpServletResponse response) throws IOException {
        if (userService.isUserExist(email)) {
            loginOAuth2UserLogic(email, response);
        } else {
            createOAuth2UserLogic(customOAuth2User, email, response);
        }
    }

    private void createOAuth2UserLogic(CustomOAuth2User customOAuth2User, String email, HttpServletResponse response) throws IOException {
        User  createdUser = userService.createNewUserFromOAuth2(customOAuth2User);
        log.info("User register via google with email - {} == {}", email, LocalDateTime.now());
        response.sendRedirect("/api/v1/users/" + createdUser.getId() + "/add-data");
    }

    private void loginOAuth2UserLogic(String email, HttpServletResponse response)  throws IOException {
        log.info("User authorize via google with email - {} == {}", email, LocalDateTime.now());
        response.sendRedirect("/api/v1/users");
    }

    private void logoutRequest(HttpSecurity httpSecurity) {
        try {
            httpSecurity.logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/api/v1/auth/login")
                    .permitAll()
            );
        } catch (Exception exception) {
            log.error("Logout exception - {}", exception.getCause().getMessage());
            throw new LogoutException("Something went wrong, sorry it`s our mistake," +
                    " we are already working on it!⚒️");
        }
    }

    private void disableCsrf(HttpSecurity httpSecurity) {
        try {
            httpSecurity.csrf(AbstractHttpConfigurer::disable);
        } catch (Exception exception) {
            log.error("Csrf disabling exception - {}", exception.getCause().getMessage());
            throw new CsrfException("Something went wrong with our config," +
                    " sorry it`s our mistake, we are already working on it!⚒️");
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
